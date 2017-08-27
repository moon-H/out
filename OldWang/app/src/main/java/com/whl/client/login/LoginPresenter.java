package com.whl.client.login;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.whl.framework.http.model.Result;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.gateway.MobileGateway;
import com.whl.client.gateway.WalletGateway;
import com.whl.client.gateway.model.wallet.SendActiveSubAuthCodeRs;
import com.whl.client.umengShare.UMengShare;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by liwx on 2017//30.
 * 登录presenter
 */

public class LoginPresenter implements LoginContract.Presenter {
    private static final String TAG = "LoginPresenter";
    private Activity mContext;
    private LoginContract.LoginView mLoginView;
    private LoginManager mLoginManager;
    private UMShareAPI mShareAPI = null;
    private WalletGateway mWalletGateway;

    private String mUid = null;
    private String mNickName = null;//nickName,gender,iconUrl
    private String mGender = null;
    private String mIconUrl = null;
    private String mUidType = null;


    public LoginPresenter(Activity context, LoginContract.LoginView view) {
        mContext = checkNotNull(context);
        mLoginView = checkNotNull(view);
        mLoginView.setPresenter(this);
        mLoginManager = new LoginManager(mContext);
        mShareAPI = UMShareAPI.get(mContext);//友盟
        mWalletGateway = new WalletGateway(mContext);
        UMengShare.configPlatform();
        HandleLoginListener handleLoginListener = new HandleLoginListener() {
            @Override
            public void onLoginLogicSuccess() {
                mLoginView.onLoginSuccess();
            }

            @Override
            public void onLoginLogicFailed(Result result) {
                mLoginView.onLoginFailed(result);
            }

        };
        mLoginManager.setHandleLoginListener(handleLoginListener);
    }

    private void handleNoNetwork() {
        mLoginView.onNoNetwork();
    }

    private void handleAutoLoginFailed(Result result) {
        mLoginView.onAutoLoginFailed(result);
    }

    private void handleLogicFailed(Result result) {
        mLoginView.onLogicFailed(result);
    }

    @Override
    public void onRequestAuthCode(String phoneNumber) {

    }

    @Override
    public void onRequestLogin(String phoneNumber, String authCode) {
        MLog.d(TAG, "onRequestLogin phoneNumber = " + phoneNumber + " authCode = " + authCode);
        if (!isMobileNO(phoneNumber)) {
            mLoginView.onLocalCheckFailed(mContext.getString(R.string.login_input_valid_phone_number));
            return;
        } else if (TextUtils.isEmpty(authCode)) {
            mLoginView.onLocalCheckFailed(mContext.getString(R.string.login_input_valid_auth_code));
        }
        showProgress("");
        mLoginManager.requestWalletLoginBySms(phoneNumber, authCode);
    }

    @Override
    public void onThirdPartyLogin(final SHARE_MEDIA platform) {
        MLog.d(TAG, "onThirdPartyLogin : " + platform.toString());
        mShareAPI.doOauthVerify(mContext, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if (map == null) {
                    mLoginView.onThirdPartyAuthFailed();
                    return;
                }
                if (share_media == SHARE_MEDIA.WEIXIN) {
                    mUidType = LoginManager.LOGIN_TYPE_WEIXIN;
                    mUid = map.get("unionid"); //uid nickName,gender,iconUrl

                } else if (share_media == SHARE_MEDIA.SINA) {
                    mUidType = LoginManager.LOGIN_TYPE_SINA;
                    mUid = map.get("uid");
                    //            nickName = map.get("userName").toString();
                } else {
                    mUidType = LoginManager.LOGIN_TYPE_QQ;
                    mUid = map.get("uid");
                }
                mShareAPI.getPlatformInfo(mContext, share_media, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        if (map != null) {
                            MLog.d(TAG, "用户action信息是 " + map.toString());
                            if (share_media == SHARE_MEDIA.WEIXIN) {
                                mNickName = map.get("nickname");//uid,nickName,gender,iconUrl
                                mGender = map.get("sex");
                                mIconUrl = map.get("headimgurl");

                            } else if (share_media == SHARE_MEDIA.SINA) {
                                String result = map.get("result");
                                try {
                                    JSONObject object = new JSONObject(result);
                                    mGender = object.getString("gender");
                                    mIconUrl = object.getString("profile_image_url");
                                    mNickName = object.getString("screen_name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else if (share_media == SHARE_MEDIA.QQ) {
                                mNickName = map.get("screen_name");
                                mGender = map.get("gender");
                                mIconUrl = map.get("profile_image_url");
                            }
                            showProgress("");
                            mLoginManager.requestThirdLogin(mUid, mNickName, mGender, mIconUrl, mUidType);
                        }


                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        mLoginView.onThirdPartyAuthFailed();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        dismissProgress();
                    }
                });

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                mLoginView.onThirdPartyAuthFailed();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                dismissProgress();
            }
        });
    }

    @Override
    public void onSendAuthCodeForLogin(String phoneNumber) {
        MLog.d(TAG, "onSendAuthCodeForLogin : " + phoneNumber);
        if (!isMobileNO(phoneNumber)) {
            mLoginView.onLocalCheckFailed(mContext.getString(R.string.login_input_valid_phone_number));
            return;
        }
        showProgress("");
        mWalletGateway.sendAuthCodeForLogin(phoneNumber, new MobileGateway.MobileGatewayListener<SendActiveSubAuthCodeRs>() {
            @Override
            public void onFailed(Result result) {
                handleLogicFailed(result);
            }

            @Override
            public void onHttpFailed(int statusCode, Header[] headers) {
                handleLogicFailed(null);
            }

            @Override
            public void onSuccess(SendActiveSubAuthCodeRs response) {
                dismissProgress();
                mLoginView.onRequestAuthCodeSuccess();
            }

            @Override
            public void onNoNetwork() {
                handleNoNetwork();
            }

            @Override
            public void onAutoLoginSuccess() {

            }

            @Override
            public void onAutoLoginFailed(Result result) {

            }

        });


    }

    @Override
    public void onHandleActivityResult(int requestCode, int resultCode, Intent data) {
        MLog.d(TAG, "onHandleActivityResult");
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }
    private boolean isMobileNO(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        } else if (number.length() == 11) {
            return true;
        } else {
            return false;
        }
    }
    private void showProgress(String msg) {
        mLoginView.onShowProgress(msg);
    }

    private void dismissProgress() {
        mLoginView.onDismissProgress();
    }

}
