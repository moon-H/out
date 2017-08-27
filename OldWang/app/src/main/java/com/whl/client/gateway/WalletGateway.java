package com.whl.client.gateway;

import android.app.Activity;

import com.whl.framework.http.ResponseMappingHandler;
import com.whl.framework.http.model.Result;
import com.whl.framework.utils.DeviceInfo;
import com.whl.framework.utils.Utils;
import com.whl.client.BuildConfig;
import com.whl.client.app.BizApplication;
import com.whl.client.gateway.model.login.RequestWalletLoginBySmsRq;
import com.whl.client.gateway.model.login.RequestWalletLoginBySmsRs;
import com.whl.client.gateway.model.login.RequestWalletLoginByTokenRq;
import com.whl.client.gateway.model.login.RequestWalletLoginByTokenRs;
import com.whl.client.gateway.model.login.SendAuthCodeForLoginRq;
import com.whl.client.gateway.model.wallet.CheckWalletUpdateRq;
import com.whl.client.gateway.model.wallet.CheckWalletUpdateRs;
import com.whl.client.gateway.model.wallet.GetUserHeadPicRq;
import com.whl.client.gateway.model.wallet.GetUserHeadPicRs;
import com.whl.client.gateway.model.wallet.RegisterThirdpartyUserRq;
import com.whl.client.gateway.model.wallet.RegisterThirdpartyUserRs;
import com.whl.client.gateway.model.wallet.RegisterUserRq;
import com.whl.client.gateway.model.wallet.RegisterUserRs;
import com.whl.client.gateway.model.wallet.RegistertPanchanUserRq;
import com.whl.client.gateway.model.wallet.RegistertPanchanUserRs;
import com.whl.client.gateway.model.wallet.RequestChangeLoginPwdRq;
import com.whl.client.gateway.model.wallet.RequestChangeLoginPwdRs;
import com.whl.client.gateway.model.wallet.RequestLogoutClientRq;
import com.whl.client.gateway.model.wallet.RequestLogoutClientRs;
import com.whl.client.gateway.model.wallet.RequestThirdpartyLoginRq;
import com.whl.client.gateway.model.wallet.RequestThirdpartyLoginRs;
import com.whl.client.gateway.model.wallet.ResetUserLoginPwdRq;
import com.whl.client.gateway.model.wallet.ResetUserLoginPwdRs;
import com.whl.client.gateway.model.wallet.SendActiveSubAuthCodeRq;
import com.whl.client.gateway.model.wallet.SendActiveSubAuthCodeRs;
import com.whl.client.gateway.model.wallet.SendAuthCodeBySmsRq;
import com.whl.client.gateway.model.wallet.SendAuthCodeBySmsRs;
import com.whl.client.gateway.model.wallet.UploadUserHeadPicRs;
import com.whl.client.login.LoginManager;
import com.whl.client.preference.MPreference;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.InputStream;

/**
 * Created by liwx on 2015/9/8.
 */
public class WalletGateway extends MobileGateway {
    private static final String TAG = WalletGateway.class.getSimpleName();

    private Activity mActivity;
    //    private PanChanManager mPanchanManager;

    public WalletGateway(Activity context) {
        super(context);
        mActivity = context;
        //        mPanchanManager = new PanChanManager(context, null);
    }

    /**
     * 检查版本
     */
    public void checkUpdateClient(final MobileGatewayListener<CheckWalletUpdateRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/activate/checkWalletUpdate";
        CheckWalletUpdateRq req = new CheckWalletUpdateRq();
        req.setCurrentVersion(DeviceInfo.getAppVersionName(mContext));
        req.setOsName(BizApplication.OS_NAME);
        req.setWalletId(BizApplication.WALLET_ID_CHECK_UPDATE);
        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<CheckWalletUpdateRs>(CheckWalletUpdateRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                if (listener != null) {
                    listener.onHttpFailed(statusCode, headers);
                }
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, CheckWalletUpdateRs response) {
                if (listener != null) {
                    if (response != null && response.getResult().getCode() == Result.OK) {
                        listener.onSuccess(response);
                    } else {
                        if (response != null)
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
     * 第三方登陆接口
     */
    public void requestThirdLogin(String uid, String nickName, String gender, String iconUrl, final MobileGatewayListener<RequestThirdpartyLoginRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/requestThirdpartyLogin";
        RequestThirdpartyLoginRq request = new RequestThirdpartyLoginRq();
        request.setUid(uid);
        request.setWalletId(BizApplication.WALLET_ID);
        request.setModelName(DeviceInfo.getDeviceModelName());
        request.setImei(DeviceInfo.getIMEI(mContext));
        request.setOsName(BizApplication.OS_NAME);
        request.setSeId(DeviceInfo.getICCID(mContext));
        request.setImsi(DeviceInfo.getIMSI(mContext));
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<RequestThirdpartyLoginRs>(RequestThirdpartyLoginRs.class) {
            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                if (listener != null) {
                    listener.onHttpFailed(statusCode, headers);
                }
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, RequestThirdpartyLoginRs response) {
                if (response != null && response.getResult().getCode() == Result.OK) {
                    MPreference.putString(mApp, MPreference.ICCID, DeviceInfo.getICCID(mApp));
                    BizApplication.getInstance().setCurrentMno(response.getMno());
                    if (listener != null)
                        listener.onSuccess(response);
                    //                        checkEligibility("", null);
                } else {
                    if (listener != null && response != null)
                        listener.onFailed(response.getResult());
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

    /**
     * 第三方注册接口
     */
    public void registerThirdUser(String uid, String nickName, String gender, String iconUrl, String uidType, final MobileGatewayListener<RegisterThirdpartyUserRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/activate/registerThirdpartyUser";
        RegisterThirdpartyUserRq request = new RegisterThirdpartyUserRq();
        request.setUid(uid);
        request.setNickName(nickName);
        request.setGender(gender);
        request.setIconUrl(iconUrl);
        request.setUidType(uidType);
        request.setWalletId(BizApplication.WALLET_ID);
        request.setEmail("");
        request.setImei(DeviceInfo.getIMEI(mContext));
        request.setModelName(DeviceInfo.getDeviceModelName());
        request.setSeId(DeviceInfo.getICCID(mContext));
        request.setMno("");
        request.setImsi(DeviceInfo.getIMSI(mContext));
        request.setOsName(BizApplication.OS_NAME);
        request.setChannelCode(Utils.getChannelId(mContext));

        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<RegisterThirdpartyUserRs>(RegisterThirdpartyUserRs.class) {
            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                if (listener != null) {
                    listener.onHttpFailed(statusCode, headers);
                }
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, RegisterThirdpartyUserRs response) {
                if (listener != null) {
                    if (response != null && response.getResult().getCode() == Result.OK) {
                        listener.onSuccess(response);
                    } else {
                        if (response != null)
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
     * 注册闪客蜂用户
     */
    public void registerUser(String msisdn, String password, String authCode, final MobileGatewayListener<RegisterUserRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/activate/registerUser";
        RegisterUserRq request = new RegisterUserRq();
        request.setMsisdn(msisdn);
        request.setEmail("");
        request.setImei(DeviceInfo.getIMEI(mContext));
        request.setModelName(DeviceInfo.getDeviceModelName());
        request.setLoginPassword(password);
        request.setSeId(DeviceInfo.getICCID(mContext));
        request.setMno("");
        request.setWalletId(BizApplication.WALLET_ID);
        request.setAuthCode(authCode);
        request.setImsi(DeviceInfo.getIMSI(mContext));
        request.setChannelCode(Utils.getChannelId(mContext));
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<RegisterUserRs>(RegisterUserRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                if (listener != null) {
                    listener.onHttpFailed(statusCode, headers);
                }
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, RegisterUserRs response) {
                if (listener != null) {
                    if (response != null && response.getResult().getCode() == Result.OK) {
                        listener.onSuccess(response);
                    } else {
                        if (response != null)
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
     * 第三方绑定手机号 注册闪客蜂用户
     */
    public void registertPanchanUser(String msisdn, String authCode, final MobileGatewayListener<RegistertPanchanUserRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/panchan/registertPanchanUser";
        RegistertPanchanUserRq request = new RegistertPanchanUserRq();
        request.setMsisdn(msisdn);//手机号
        request.setAuthCode(authCode);
        request.setOsName(BizApplication.OS_NAME);
        request.setAppVersion(DeviceInfo.getAppVersionName(mApp));
        request.setSeId(DeviceInfo.getICCID(mContext));
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<RegistertPanchanUserRs>(RegistertPanchanUserRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                onFailed(statusCode, headers, listener);
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, RegistertPanchanUserRs response) {
                if (listener != null) {
                    if (response != null && response.getResult().getCode() == Result.OK) {
                        listener.onSuccess(response);
                    } else {
                        if (response != null)
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
     * 修改登录密码
     */
    public void requestChangeLoginPwd(String msisdn, String newPwd, String orgPwd, final MobileGatewayListener<RequestChangeLoginPwdRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/requestChangeLoginPwd";
        RequestChangeLoginPwdRq request = new RequestChangeLoginPwdRq();
        request.setLoginPasswordNew(newPwd);
        request.setLoginPasswordOriginal(orgPwd);
        request.setMsisdn(msisdn);
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<RequestChangeLoginPwdRs>(RequestChangeLoginPwdRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                onFailed(statusCode, headers, listener);
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, RequestChangeLoginPwdRs response) {
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
     * 发送短信验证码
     */
    public void sendActiveSubAuthCode(String msisdn, final MobileGatewayListener<SendActiveSubAuthCodeRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/sendActiveSubAuthCode";
        SendActiveSubAuthCodeRq request = new SendActiveSubAuthCodeRq();
        request.setMsisdn(msisdn);
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<SendActiveSubAuthCodeRs>(SendActiveSubAuthCodeRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                if (listener != null) {
                    listener.onHttpFailed(statusCode, headers);
                }
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, SendActiveSubAuthCodeRs response) {
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
     * 重置密码
     */
    public void resetUserLoginPwd(String msisdn, final MobileGatewayListener<ResetUserLoginPwdRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/forgetPassword/resetUserLoginPwd";
        ResetUserLoginPwdRq request = new ResetUserLoginPwdRq();
        request.setSeId(DeviceInfo.getICCID(mContext));
        request.setMsisdn(msisdn);
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<ResetUserLoginPwdRs>(ResetUserLoginPwdRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                if (listener != null) {
                    listener.onHttpFailed(statusCode, headers);
                }
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, ResetUserLoginPwdRs response) {
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
     * 退出客户端
     */
    public void requestLogoutClient(final MobileGatewayListener<RequestLogoutClientRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/requestLogoutClient";
        RequestLogoutClientRq req = new RequestLogoutClientRq();
        req.setMsisdn(LoginManager.getPhoneNumber(mApp));
        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<RequestLogoutClientRs>(RequestLogoutClientRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                if (listener != null) {
                    listener.onHttpFailed(statusCode, headers);
                }
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, RequestLogoutClientRs response) {
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
     * 发送短信验证码
     */
    public void sendAuthCodeBySms(String msisdn, final MobileGatewayListener<SendAuthCodeBySmsRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/activate/sendAuthCodeBySms";
        SendAuthCodeBySmsRq request = new SendAuthCodeBySmsRq();
        request.setMsisdn(msisdn);
        request.setSeId(DeviceInfo.getICCID(mContext));
        request.setWalletId(BizApplication.WALLET_ID);
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<SendAuthCodeBySmsRs>(SendAuthCodeBySmsRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                if (listener != null) {
                    listener.onHttpFailed(statusCode, headers);
                }
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, SendAuthCodeBySmsRs response) {
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
     * 上传图片接口
     * 图片类型(0-帖子图片,2-回帖图片)
     */
    public void uploadUserPhoto(int imageType, String isThirdPartyLogin, String thirdPartyType, String userName, InputStream photo, final MobileGatewayListener<UploadUserHeadPicRs> listener) {
        String url = BuildConfig.USER_HEAD_URL + "/svlt/uploadUserHeadPic";
        RequestParams params = new RequestParams();
        params.put("imageType", imageType);
        params.put("isThirdPartyLogin", isThirdPartyLogin);
        params.put("thirdPartyType", thirdPartyType);
        params.put("userName", userName);
        params.put("photo", photo);
        mGateway.uploadPhoto(mApp, url, params, new ResponseMappingHandler<UploadUserHeadPicRs>(UploadUserHeadPicRs.class) {
            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                onFailed(statusCode, headers, listener);
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, UploadUserHeadPicRs response) {
                if (listener != null) {
                    if (response != null && response.getResult().getCode() == Result.OK) {
                        listener.onSuccess(response);
                    } else {
                        if (response != null)
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

    /**
     * 获取用户头像
     */

    public void getUserHeadPic(final MobileGatewayListener<GetUserHeadPicRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/getUserHeadPic";
        GetUserHeadPicRq params = new GetUserHeadPicRq();
        mGateway.sendRequset(mApp, url, params, new ResponseMappingHandler<GetUserHeadPicRs>(GetUserHeadPicRs.class) {

                @Override
                public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                    onFailed(statusCode, headers, listener);
                }

                @Override
                public void handleSuccess(int statusCode, Header[] headers, GetUserHeadPicRs response) {
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

    /**
     * 发送登录短信验证码
     */

    public void sendAuthCodeForLogin(String phoneNumber, final MobileGatewayListener<SendActiveSubAuthCodeRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/sendAuthCodeForLogin";
        SendAuthCodeForLoginRq req = new SendAuthCodeForLoginRq();
        req.setMsisdn(phoneNumber);
        req.setSeId(DeviceInfo.getICCID(mContext));
        req.setWalletId(BizApplication.WALLET_ID);
        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<SendActiveSubAuthCodeRs>(SendActiveSubAuthCodeRs.class) {

                @Override
                public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                    onFailed(statusCode, headers, listener);
                }

                @Override
                public void handleSuccess(int statusCode, Header[] headers, SendActiveSubAuthCodeRs response) {
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

    /**
     * 短信验证码登录客户端
     */

    public void requestWalletLoginBySms(String phoneNumber, String authCode, final MobileGatewayListener<RequestWalletLoginBySmsRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/requestWalletLoginBySms";
        RequestWalletLoginBySmsRq request = new RequestWalletLoginBySmsRq();
        request.setWalletId(BizApplication.WALLET_ID);
        request.setMsisdn(phoneNumber);
        request.setAuthCode(authCode);
        request.setModelName(DeviceInfo.getDeviceModelName());
        request.setImei(DeviceInfo.getIMEI(mContext));
        request.setSeId(DeviceInfo.getICCID(mContext));
        request.setImsi(DeviceInfo.getIMSI(mContext));
        request.setOsName(BizApplication.OS_NAME);
        request.setMno("");
        request.setChannelCode(Utils.getChannelId(mContext));
        request.setVersionName(DeviceInfo.getAppVersionName(mApp));
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<RequestWalletLoginBySmsRs>(RequestWalletLoginBySmsRs.class) {

                @Override
                public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                    onFailed(statusCode, headers, listener);
                }

                @Override
                public void handleSuccess(int statusCode, Header[] headers, RequestWalletLoginBySmsRs response) {
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

    /**
     * Token登录客户端
     */

    public void requestWalletLoginByToken(final MobileGatewayListener<RequestWalletLoginByTokenRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/client/requestWalletLoginByToken";
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
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<RequestWalletLoginByTokenRs>(RequestWalletLoginByTokenRs.class) {

                @Override
                public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                    onFailed(statusCode, headers, listener);
                }

                @Override
                public void handleSuccess(int statusCode, Header[] headers, RequestWalletLoginByTokenRs response) {
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

}
