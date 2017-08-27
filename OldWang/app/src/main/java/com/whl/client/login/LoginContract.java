package com.whl.client.login;

import android.content.Intent;

import com.whl.framework.http.model.Result;
import com.whl.client.app.BasePresenter;
import com.whl.client.app.BaseView;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by liwx on 2017/7/30.
 */

public class LoginContract {
    interface LoginView extends BaseView<Presenter> {
        void onLoginSuccess();//登录成功

        void onLoginFailed(Result result);//登录失败

        void onLocalCheckFailed(String msg);//本地检查登录逻辑失败

        void onThirdPartyAuthFailed();//第三方授权失败

        void onRequestAuthCodeSuccess();//获取验证码成功
    }

    interface Presenter extends BasePresenter {
        void onRequestAuthCode(String phoneNumber);//请求验证码

        void onRequestLogin(String phoneNumber, String authCode);//验证码登录

        void onThirdPartyLogin(SHARE_MEDIA platform);//第三方登录

        void onSendAuthCodeForLogin(String phoneNumber);//获取登录验证码

        void onHandleActivityResult(int requestCode, int resultCode, Intent data);//处理activity result方法

    }
}
