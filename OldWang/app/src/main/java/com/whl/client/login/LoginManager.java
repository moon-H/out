package com.whl.client.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.whl.framework.http.model.Result;
import com.whl.framework.utils.DeviceInfo;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BizApplication;
import com.whl.client.app.CssConstant;
import com.whl.client.gateway.MobileGateway;
import com.whl.client.gateway.WalletGateway;
import com.whl.client.gateway.model.login.RequestWalletLoginBySmsRs;
import com.whl.client.gateway.model.login.RequestWalletLoginByTokenRs;
import com.whl.client.gateway.model.wallet.RegisterThirdpartyUserRs;
import com.whl.client.gateway.model.wallet.RequestThirdpartyLoginRs;
import com.whl.client.preference.MPreference;
import com.whl.client.utils.DesThree;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liwx on 2016/4/19.
 */
public class LoginManager {
    private static final String TAG = "LoginManager";

    public static final String LOGIN_TYPE_QQ = "qq";
    public static final String LOGIN_TYPE_WEIXIN = "weixin";
    public static final String LOGIN_TYPE_SINA = "sina";

    public static final int ERROR_CODE_USER_STATUS_INVALID = 1;
    public static final int ERROR_CODE_DEFAULT = 2;


    private Activity context;
    private WalletGateway mWalletGateway;
    private HandleLoginListener listener;
    private Handler myHandler;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());

    public LoginManager(Activity context) {
        this.context = context;
        mWalletGateway = new WalletGateway(context);
        myHandler = new Handler(Looper.getMainLooper());
    }

    public void setHandleLoginListener(HandleLoginListener loginListener) {
        this.listener = loginListener;
    }

    /**
     * 是否已绑定手机号
     **/
    public static boolean allNumberEmpty(Context context) {
        if (TextUtils.isEmpty(MPreference.getString(context, MPreference.THIRD_RIGESTER_PHONENUMBER)) && TextUtils.isEmpty(MPreference.getLoginId(context))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取社交账号类型微信、qq、sina
     */
    public static String getThirdPartyType(Context context) {
        return MPreference.getString(context, MPreference.THIRD_LOGIN_UIDTYPE);
    }

    /**
     * 获取社交账号ID
     */
    public static String getThirdPartyID(Context context) {
        return MPreference.getString(context, MPreference.THIRD_LOGIN_UID);
    }

    /**
     * 获得绑定的手机号
     **/
    public static String getPhoneNumber(Context context) {
        if (!hasShankePhoneId(context)) {
            return MPreference.getString(context, MPreference.THIRD_RIGESTER_PHONENUMBER);
        } else {
            return MPreference.getLoginId(context);
        }
    }

    /**
     * 注册手机号或者社交账号绑定手机号
     **/
    public static boolean hasPhoneNumber(Context context) {
        if (!hasShankePhoneId(context)) {
            return !TextUtils.isEmpty(MPreference.getString(context, MPreference.THIRD_RIGESTER_PHONENUMBER));
        } else {
            return !TextUtils.isEmpty(MPreference.getLoginId(context));
        }
    }

    /**
     * 注册手机号或者社交账号绑定手机号
     **/
    public static String getShankePhoneId(Context context) {
        return MPreference.getLoginId(context);
    }

    /**
     * 第三方登录ID是否为空
     **/
    public static boolean hasThirdPartyId(Context context) {
        return !TextUtils.isEmpty(MPreference.getString(context, MPreference.THIRD_LOGIN_UID));
    }

    /**
     * 手机号+验证码登录同时token有效时返回true
     **/
    public static boolean hasShankePhoneId(Context context) {
        String phoneNumber = MPreference.getLoginId(context);
        String token = MPreference.getString(context, MPreference.USER_TOKEN);
        boolean isValid = isTokenValid(token);
        MLog.d(TAG, "is token valid = " + isValid);
        return !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(token) && isValid;
    }

    /**
     * 本地是否有登录过的账号
     **/
    public static boolean isLocalSaveLoginId(Context context) {
        return hasShankePhoneId(context) && hasThirdPartyId(context);
    }

    public static boolean isTokenValid(String token) {
        try {
            String orgToken = DesThree.decryptThreeDESECB(token, DesThree.KEY);
            String date = orgToken.substring(orgToken.length() - 17, orgToken.length());
            MLog.d(TAG, " orgToken = " + orgToken);
            Date dt1 = dateFormat.parse(date);
            if (dt1.getTime() > System.currentTimeMillis()) {
                return true;
            }
        } catch (Exception e) {
            MLog.d(TAG, "decryptThreeDESECB OCCUR ERROR  ", e);
            return false;
        }
        return false;
    }

    /**
     * 第三方登录
     */
    public void requestThirdLogin(final String uid, final String nickName, final String gender, final String iconUrl, final String uidType) {
        //        final String nickName = Base64.encodeToString(nickNameOrg.getBytes(), Base64.DEFAULT);
        mWalletGateway.requestThirdLogin(uid, nickName, gender, iconUrl, new MobileGateway.MobileGatewayListener<RequestThirdpartyLoginRs>() {
            @Override
            public void onSuccess(RequestThirdpartyLoginRs response) {
                MPreference.removeShankePhoneLoginInfo(context);
                String state = response.getWalletSubscriptionState();
                if (state != null) {
                    if (state.equals(MobileGateway.LOGIN_STATUS_ACTIVATED)) {
                        BizApplication.getInstance().setIsLoginClient(true);
                        MLog.d(TAG, "LOGIN STATUS ACTIVATE");
                        bindPushClientId();
                        onLoginLogicSuccess(response.getMno());
                        BizApplication.getInstance().setCurrentMno(response.getMno());
                    } else if (state.equals(MobileGateway.LOGIN_NOT_REGISTERED)) {
                        //第三方登陆 = 为注册 = 调用注册接口
                        registerThirdUser(uid, nickName, gender, iconUrl, uidType);//String uid,String nickName,String gender,String iconUrl,String uidType
                    } else if (state.equals(MobileGateway.LOGIN_NOT_PHONENUMBER)) {
                        //未绑定手机号
                        BizApplication.getInstance().setIsLoginClient(true);
                        MLog.d(TAG, "LOGIN STATUS ACTIVATE BUT NO PHONENUMBER");
                        bindPushClientId();
                        onLoginLogicSuccess(response.getMno());
                    } else {
                        MLog.d(TAG, "LOGIN STATUS NOT ACTIVATE");
                        onLoginLogicFailed(getCustomResult(ERROR_CODE_USER_STATUS_INVALID, context.getString(R.string.login_status_error)));
                    }
                    saveStateAndUserInfo(response, uid, nickName, gender, iconUrl, uidType);
                }

            }

            @Override
            public void onFailed(Result result) {
                onLoginLogicFailed(result);
            }

            @Override
            public void onHttpFailed(int statusCode, Header[] headers) {
                onLoginLogicFailed(getCustomResult(ERROR_CODE_DEFAULT, context.getString(R.string.connect_server_failed)));
            }

            @Override
            public void onNoNetwork() {
                onLoginLogicFailed(getCustomResult(ERROR_CODE_DEFAULT, context.getString(R.string.network_exception)));
            }

            @Override
            public void onAutoLoginSuccess() {

            }

            @Override
            public void onAutoLoginFailed(Result result) {

            }

        });
    }

    private void saveStateAndUserInfo(RequestThirdpartyLoginRs response, String uid, String nickName, String gender, String iconUrl, String uidType) {
        MPreference.putString(context, MPreference.THIRD_LOGIN_STATUE, response.getWalletSubscriptionState());
        MPreference.putString(context, MPreference.THIRD_RIGESTER_PHONENUMBER, response.getMsisdn());//手机号
        MPreference.putString(context, MPreference.THIRD_LOGIN_UID, uid);
        MPreference.putString(context, MPreference.THIRD_LOGIN_NICKNAME, nickName);
        MPreference.putString(context, MPreference.THIRD_LOGIN_GENDER, gender);
        MPreference.putString(context, MPreference.THIRD_LOGIN_ICONURL, iconUrl);
        MPreference.putString(context, MPreference.THIRD_LOGIN_UIDTYPE, uidType);
        String statue = MPreference.getString(context, MPreference.THIRD_LOGIN_STATUE);
        String saveUid = MPreference.getString(context, MPreference.THIRD_LOGIN_UID);
        MLog.d(TAG, "保存的用户信息和1004是：" + statue + "  uid" + saveUid);
    }

    /**
     * 第三方注册
     */
    public void registerThirdUser(final String uid, final String nickName, final String gender, final String iconUrl, final String uidType) {
        mWalletGateway.registerThirdUser(uid, nickName, gender, iconUrl, uidType, new MobileGateway.MobileGatewayListener<RegisterThirdpartyUserRs>() {
            @Override
            public void onSuccess(RegisterThirdpartyUserRs response) {
                MLog.d(TAG, "第三方注册服务器返回response:" + response.toString());
                if (response.getResult().getCode() == Result.OK) {
                    //注册成功 调用登陆
                    requestThirdLogin(uid, nickName, gender, iconUrl, uidType);
                }
            }

            @Override
            public void onFailed(Result result) {
                //                CommonToast.onFailed(context, result);
                onLoginLogicFailed(result);
            }

            @Override
            public void onHttpFailed(int statusCode, Header[] headers) {
                //                CommonToast.onHttpFailed(context);
                onLoginLogicFailed(getCustomResult(ERROR_CODE_DEFAULT, context.getString(R.string.connect_server_failed)));
            }

            @Override
            public void onNoNetwork() {
                //                CommonToast.onNoNetwork(context);
                onLoginLogicFailed(getCustomResult(ERROR_CODE_DEFAULT, context.getString(R.string.network_exception)));
            }

            @Override
            public void onAutoLoginSuccess() {

            }

            @Override
            public void onAutoLoginFailed(Result result) {

            }

        });
    }


    /**
     * 绑定推送id
     */
    private void bindPushClientId() {
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WalletGateway gateway = new WalletGateway(context);
                //        String cid = MPreference.getString(context, MPreference.PUSH_CLIENT_ID);
                String pushId = BizApplication.getInstance().getPushId();
                MLog.d(TAG, "cid = " + pushId);
                if (!TextUtils.isEmpty(pushId))
                    gateway.bindPushClientId(pushId, null);
            }
        }, 1500);
    }

    /**
     * 短信验证码登录客户端
     */
    public void requestWalletLoginBySms(final String phoneNumber, String authCode) {
        mWalletGateway.requestWalletLoginBySms(phoneNumber, authCode, new MobileGateway.MobileGatewayListener<RequestWalletLoginBySmsRs>() {
            @Override
            public void onFailed(Result result) {
                onLoginLogicFailed(result);
            }

            @Override
            public void onHttpFailed(int statusCode, Header[] headers) {
                //                CommonToast.onHttpFailed(context);
                onLoginLogicFailed(getCustomResult(ERROR_CODE_DEFAULT, context.getString(R.string.connect_server_failed)));
            }

            @Override
            public void onSuccess(RequestWalletLoginBySmsRs response) {
                String state = response.getWalletSubscriptionState();
                MPreference.removeThirdLoginInfo(context);
                //                MLog.d(TAG, "TOKEN = " + DES3.decryptMode(context, HexConverter.hexStringToBytes("LH5S5AID7U5DGDF1RHMIBPBJYDRTR22P"), response.getToken()));
                if (state != null) {
                    if (state.equals(MobileGateway.LOGIN_STATUS_ACTIVATED)) {
                        MLog.d(TAG, "LOGIN STATUS ACTIVATE");
                        BizApplication.getInstance().setIsLoginClient(true);
                        bindPushClientId();
                        onLoginLogicSuccess(response.getMno());
                        MPreference.saveLoginID(context, phoneNumber);
                        MPreference.putString(context, MPreference.USER_TOKEN, response.getToken());
                    } else {
                        MLog.d(TAG, "LOGIN STATUS NOT ACTIVATE");
                        onLoginLogicFailed(getCustomResult(ERROR_CODE_USER_STATUS_INVALID, context.getString(R.string.login_status_error)));
                    }
                }
            }

            @Override
            public void onNoNetwork() {
                onLoginLogicFailed(getCustomResult(ERROR_CODE_DEFAULT, context.getString(R.string.network_exception)));
            }

            @Override
            public void onAutoLoginSuccess() {

            }

            @Override
            public void onAutoLoginFailed(Result result) {

            }

        });
    }

    /**
     * token登录
     */
    public void requestWalletLoginByToken() {
        final String phoneNumber = MPreference.getLoginId(context);
        final String token = MPreference.getString(context, MPreference.USER_TOKEN);
        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(token)) {
            MLog.d(TAG, "requestWalletLoginByToken phoneNumber = " + phoneNumber + " token = " + token);
            onLoginLogicFailed(getCustomResult(ERROR_CODE_DEFAULT, context.getString(R.string.login_failed_7001)));
            return;
        }

        mWalletGateway.requestWalletLoginByToken(new MobileGateway.MobileGatewayListener<RequestWalletLoginByTokenRs>() {
            @Override
            public void onFailed(Result result) {
                //                CommonToast.onFailed(context, result);
                onLoginLogicFailed(result);
                MPreference.removeString(context, MPreference.USER_TOKEN);
            }

            @Override
            public void onHttpFailed(int statusCode, Header[] headers) {
                //                CommonToast.onHttpFailed(context);
                onLoginLogicFailed(getCustomResult(ERROR_CODE_DEFAULT, context.getString(R.string.connect_server_failed)));
            }

            @Override
            public void onSuccess(RequestWalletLoginByTokenRs response) {
                String state = response.getWalletSubscriptionState();
                MPreference.removeThirdLoginInfo(context);
                if (state != null) {
                    if (state.equals(MobileGateway.LOGIN_STATUS_ACTIVATED)) {
                        BizApplication.getInstance().setIsLoginClient(true);
                        MLog.d(TAG, "LOGIN STATUS ACTIVATE");
                        bindPushClientId();
                        onLoginLogicSuccess(response.getMno());
                        MPreference.saveLoginID(context, phoneNumber);
                        MPreference.putString(context, MPreference.USER_TOKEN, token);
                    } else {
                        MLog.d(TAG, "LOGIN STATUS NOT ACTIVATE");
                        onLoginLogicFailed(getCustomResult(ERROR_CODE_USER_STATUS_INVALID, context.getString(R.string.login_status_error)));
                    }
                }
            }

            @Override
            public void onNoNetwork() {
                //                CommonToast.onNoNetwork(context);
                onLoginLogicFailed(getCustomResult(ERROR_CODE_DEFAULT, context.getString(R.string.network_exception)));
            }

            @Override
            public void onAutoLoginSuccess() {

            }

            @Override
            public void onAutoLoginFailed(Result result) {

            }

        });
    }

    private void onLoginLogicSuccess(String mno) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(CssConstant.Action.ACTION_LOGIN_SUCCESS));
        MPreference.putString(context, MPreference.ICCID, DeviceInfo.getICCID(context));
        BizApplication.getInstance().setCurrentMno(mno);
        if (listener != null)
            listener.onLoginLogicSuccess();
    }

    private void onLoginLogicFailed(Result result) {
        if (listener != null)
            listener.onLoginLogicFailed(result);
    }

    private Result getCustomResult(int code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

}
