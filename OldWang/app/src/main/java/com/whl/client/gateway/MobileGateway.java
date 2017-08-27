package com.whl.client.gateway;


import android.content.Context;
import android.text.TextUtils;

import com.whl.framework.http.BaseGateway;
import com.whl.framework.http.ResponseMappingHandler;
import com.whl.framework.http.model.Response;
import com.whl.framework.http.model.Result;
import com.whl.framework.utils.DeviceInfo;
import com.whl.framework.utils.MLog;
import com.whl.client.app.BizApplication;
import com.whl.client.gateway.model.PageInfo;
import com.whl.client.gateway.model.login.RequestWalletLoginByTokenRq;
import com.whl.client.gateway.model.wallet.BindingPushClientIdRq;
import com.whl.client.gateway.model.wallet.BindingPushClientIdRs;
import com.whl.client.gateway.model.wallet.GetWalletFaqListRq;
import com.whl.client.gateway.model.wallet.GetWalletFaqListRs;
import com.whl.client.gateway.model.wallet.RequestCommitClientExceptionRq;
import com.whl.client.gateway.model.wallet.RequestCommitClientExceptionRs;
import com.whl.client.gateway.model.wallet.RequestCommitFeedbackRq;
import com.whl.client.gateway.model.wallet.RequestCommitFeedbackRs;
import com.whl.client.gateway.model.wallet.RequestThirdpartyLoginRq;
import com.whl.client.gateway.model.wallet.RequestThirdpartyLoginRs;
import com.whl.client.gateway.model.wallet.RequestWalletLoginRs;
import com.whl.client.login.LoginManager;
import com.whl.client.preference.MPreference;

import org.apache.http.Header;


public class MobileGateway {

    private static final String TAG = MobileGateway.class.getSimpleName();

    public static final String LOGIN_NOT_REGISTERED = "10001";//未注册
    public static final String LOGIN_NOT_PHONENUMBER = "10002";//已注册未绑定手机号
    public static final String LOGIN_STATUS_ACTIVATED = "10004";//已绑定手机
    public static final String LOGIN_STATUS_UICCSWAPED = "10008";
    public static final String LOGIN_STATUS_SUSPENDED = "10005";//账号已锁定

    public BizApplication mApp;
    public Context mContext;
    public BaseGateway mGateway;

    public MobileGateway(Context context) {
        mContext = context;
        mApp = (BizApplication) mContext.getApplicationContext();
        mGateway = mApp.getBaseGateway(BizApplication.BASE_ADDRESS);
    }

    /**
     * 提交用户反馈
     */
    public void requestCommitFeedback(String appUserName, String feedbackContent, final MobileGatewayListener<RequestCommitFeedbackRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/requestCommitFeedback";
        RequestCommitFeedbackRq request = new RequestCommitFeedbackRq();
        request.setAppUserName(appUserName);
        request.setDeviceAppId(BizApplication.WALLET_ID);
        request.setAppVersionName(DeviceInfo.getAppVersionName(mContext));
        request.setFeedbackContent(feedbackContent);
        request.setModelName(DeviceInfo.getDeviceModelName());
        request.setImei(DeviceInfo.getIMEI(mContext));
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<RequestCommitFeedbackRs>(RequestCommitFeedbackRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                onFailed(statusCode, headers, listener);
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, RequestCommitFeedbackRs response) {
                if (listener != null) {
                    if (response != null && response.getResult().getCode() == Result.OK) {
                        listener.onSuccess(response);
                    } else {
                        listener.onFailed(response.getResult());
                    }
                }
            }

            @Override
            public void handleNoNetwork() {
                onNoNetwork(listener);
            }
        });
    }

    /**
     * 发送异常日志
     */
    public void sendCrashLog(String log) {
        String url = BizApplication.BASE_ADDRESS + "/activate/requestCommitClientException";
        RequestCommitClientExceptionRq request = new RequestCommitClientExceptionRq();
        request.setAppVersionName(DeviceInfo.getAppVersionName(mContext));
        request.setDeviceAppId(BizApplication.WALLET_ID);
        request.setExceptionContent(log);
        request.setImei(DeviceInfo.getIMEI(mContext));
        request.setModelName(DeviceInfo.getDeviceModelName());
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<RequestCommitClientExceptionRs>(RequestCommitClientExceptionRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, RequestCommitClientExceptionRs response) {
            }

            @Override
            public void handleNoNetwork() {
            }
        });
    }


    /**
     * 获取帮助信息
     */
    public void getWalletFaqList(int pageNumber, int pageSize, final MobileGatewayListener<GetWalletFaqListRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/getWalletFaqList";
        GetWalletFaqListRq request = new GetWalletFaqListRq();
        request.setWalletId(BizApplication.WALLET_ID);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNumber(pageNumber);
        pageInfo.setPageSize(pageSize);
        request.setPageInfo(pageInfo);
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetWalletFaqListRs>(GetWalletFaqListRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                onFailed(statusCode, headers, listener);
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, GetWalletFaqListRs response) {
                if (listener != null) {
                    if (response != null && response.getResult().getCode() == Result.OK) {
                        listener.onSuccess(response);
                    } else {
                        listener.onFailed(response.getResult());
                    }
                }
            }

            @Override
            public void handleNoNetwork() {
                if (listener != null) {
                    listener.onNoNetwork();
                }
            }
        });
    }

    public void onNoNetwork(final MobileGatewayListener listener) {
        if (listener != null) {
            listener.onNoNetwork();
        }
    }

    public void onFailed(final int statusCode, Header[] headers, final MobileGatewayListener listener) {
        {
            if (listener != null) {
                if (statusCode == 401) {
                    autoLoginClient(new AutoLoginListener<RequestWalletLoginRs>() {

                        @Override
                        public void onLoginFailed(Result result) {
                            listener.onAutoLoginFailed(result);
                        }

                        @Override
                        public void onLoginSuccess() {
                            listener.onAutoLoginSuccess();
                        }

                    });
                } else {
                    listener.onHttpFailed(statusCode, headers);
                }
            }
        }
    }

    /**
     * 登录超时时自动登录
     */
    public void autoLoginClient(final AutoLoginListener<RequestWalletLoginRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/requestWalletLoginByToken";
        String thirdUrl = BizApplication.BASE_ADDRESS + "/client/requestThirdpartyLogin";

        if (LoginManager.hasShankePhoneId(mContext)) {
            MLog.d(TAG, "normal login");
            RequestWalletLoginByTokenRq request = new RequestWalletLoginByTokenRq();
            request.setToken(MPreference.getString(mApp, MPreference.USER_TOKEN));
            request.setWalletId(BizApplication.WALLET_ID);
            request.setMsisdn(MPreference.getLoginId(mApp));
            request.setModelName(DeviceInfo.getDeviceModelName());
            request.setImei(DeviceInfo.getIMEI(mContext));
            request.setSeId(DeviceInfo.getICCID(mContext));
            request.setImsi(DeviceInfo.getIMSI(mContext));
            request.setOsName(BizApplication.OS_NAME);
            request.setVersionName(DeviceInfo.getAppVersionName(mApp));
            mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<RequestWalletLoginRs>(RequestWalletLoginRs.class) {

                @Override
                public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                    if (listener != null) {
                        listener.onLoginFailed(null);
                    }
                }

                @Override
                public void handleSuccess(int statusCode, Header[] headers, RequestWalletLoginRs response) {
                    if (listener != null) {
                        if (response != null && response.getResult().getCode() == Result.OK) {
                            BizApplication.getInstance().setCurrentMno(response.getMno());
                            String state = response.getWalletSubscriptionState();
                            MPreference.putString(mApp, MPreference.ICCID, DeviceInfo.getICCID(mApp));
                            if (state.equals(LOGIN_STATUS_ACTIVATED)) {
                                listener.onLoginSuccess();
                                //                            syncMyPresetService(listener);
                                //                            checkEligibility("", null);
                                //                            listener.onLoginLogicSuccess();
                                String pushId = BizApplication.getInstance().getPushId();
                                MLog.d(TAG, "auto login bind pushId = " + pushId);
                                if (!TextUtils.isEmpty(pushId))
                                    bindPushClientId(pushId, null);
                            } else if (state.equals(LOGIN_STATUS_UICCSWAPED)) {
                                listener.onLoginFailed(response.getResult());
                            } else if (state.equals(LOGIN_STATUS_SUSPENDED)) {
                                listener.onLoginFailed(response.getResult());
                            }
                        } else {
                            listener.onLoginFailed(response.getResult());
                        }
                    }
                }

                @Override
                public void handleNoNetwork() {
                    if (listener != null) {
                        listener.onLoginFailed(null);
                    }
                }
            });
        } else if (LoginManager.hasThirdPartyId(mContext)) {
            MLog.d(TAG, "third party login");
            RequestThirdpartyLoginRq req = new RequestThirdpartyLoginRq();
            req.setUid(MPreference.getString(mContext, MPreference.THIRD_LOGIN_UID));
            req.setWalletId(BizApplication.WALLET_ID);
            req.setModelName(DeviceInfo.getDeviceModelName());
            req.setImei(DeviceInfo.getIMEI(mContext));
            req.setOsName(BizApplication.OS_NAME);
            req.setSeId(DeviceInfo.getICCID(mContext));
            req.setImsi(DeviceInfo.getIMSI(mContext));
            mGateway.sendRequset(mApp, thirdUrl, req, new ResponseMappingHandler<RequestThirdpartyLoginRs>(RequestThirdpartyLoginRs.class) {
                @Override
                public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                    if (listener != null) {
                        listener.onLoginFailed(null);
                    }
                }

                @Override
                public void handleSuccess(int statusCode, Header[] headers, RequestThirdpartyLoginRs response) {
                    if (listener != null)
                        if (response != null && response.getResult().getCode() == Result.OK) {
                            MPreference.putString(mApp, MPreference.ICCID, DeviceInfo.getICCID(mApp));
                            BizApplication.getInstance().setCurrentMno(response.getMno());
                            String state = response.getWalletSubscriptionState();
                            if (state.equals(LOGIN_STATUS_ACTIVATED)) {
                                listener.onLoginSuccess();
                            } else if (state.equals(LOGIN_STATUS_UICCSWAPED)) {
                            } else if (state.equals(LOGIN_STATUS_SUSPENDED)) {

                            }
                        } else {
                            listener.onLoginFailed(response.getResult());
                        }
                }

                @Override
                public void handleNoNetwork() {
                    if (listener != null) {
                        listener.onLoginFailed(null);
                    }
                }
            });
        } else {
            if (listener != null) {
                listener.onLoginFailed(null);
            }
        }

    }

    /**
     * 绑定推送cId
     */
    public void bindPushClientId(String clientId, final MobileGatewayListener<BindingPushClientIdRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/bindingPushClientId";
        BindingPushClientIdRq request = new BindingPushClientIdRq();
        request.setClientId(clientId);
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<BindingPushClientIdRs>(BindingPushClientIdRs.class) {

                @Override
                public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                    onFailed(statusCode, headers, listener);
                }

                @Override
                public void handleSuccess(int statusCode, Header[] headers, BindingPushClientIdRs response) {
                    if (listener != null) {
                        if (response.getResult().getCode() == Result.OK) {
                            listener.onSuccess(response);
                        } else {
                            listener.onFailed(response.getResult());
                        }
                    }
                }

                @Override
                public void handleNoNetwork() {
                    onNoNetwork(listener);
                }
            }

        );
    }

    //----------------------------------------------------------------------------------------------------------

    /**
     * 取消所有请求
     */
    public void cancelAllRequest() {
        mGateway.cancelRequest();
    }

    public void setMaxRetriesAndTimeout(int retries, int timeout) {
        mGateway.setMaxRetriesAndTimeout(retries, timeout);
    }

    public interface AutoLoginListener<T extends Response> {

        void onLoginFailed(Result result);

        void onLoginSuccess();

    }

    public interface MobileGatewayListener<T extends Response> {

        /**
         * Biz logic faild
         */
        void onFailed(Result result);

        /**
         * http connection faild
         */
        void onHttpFailed(int statusCode, Header[] headers);

        /**
         * Biz logic success
         */
        void onSuccess(T response);

        /**
         * No available network
         */
        void onNoNetwork();

        /**
         * 自动登录成功
         */
        void onAutoLoginSuccess();

        /**
         * 自动登录失败
         */
        void onAutoLoginFailed(Result result);

        //        /**
        //         * 自动登录时检测到换卡
        //         */
        //        @Deprecated
        //        void onAutoLoginChangeCard();

    }
}
