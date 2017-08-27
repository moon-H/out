package com.whl.client.gateway.model.login;

import com.whl.framework.http.model.Response;

/**
 * Created by lenovo on 2017/7/18.
 */

public class RequestWalletLoginBySmsRs extends Response {
    private String walletSubscriptionState;
    private String token;
    private String mno;

    public String getWalletSubscriptionState() {
        return walletSubscriptionState;
    }

    public void setWalletSubscriptionState(String walletSubscriptionState) {
        this.walletSubscriptionState = walletSubscriptionState;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    @Override
    public String toString() {
        return "RequestWalletLoginBySmsRs{" + "walletSubscriptionState='" + walletSubscriptionState + '\'' + ", token='" + token + '\'' + ", mno='" + mno + '\'' + '}';
    }
}
