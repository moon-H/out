package com.whl.client.gateway.model.login;

import com.whl.framework.http.model.Response;

/**
 * Created by lenovo on 2017/7/19.
 */

public class RequestWalletLoginByTokenRs extends Response {
    private String walletSubscriptionState;
    private String mno;

    public String getWalletSubscriptionState() {
        return walletSubscriptionState;
    }

    public void setWalletSubscriptionState(String walletSubscriptionState) {
        this.walletSubscriptionState = walletSubscriptionState;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    @Override
    public String toString() {
        return "RequestWalletLoginByTokenRs{" + "walletSubscriptionState='" + walletSubscriptionState + '\'' + ", mno='" + mno + '\'' + '}';
    }
}
