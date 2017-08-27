package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Response;


public class RequestWalletLoginRs extends Response {

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
        return "RequestWalletLoginRs{" +
            "walletSubscriptionState='" + walletSubscriptionState + '\'' +
            ", mno='" + mno + '\'' +
            '}';
    }
}
