package com.whl.client.gateway.model.wallet;

import com.whl.framework.http.model.Response;

/**
 * Created by yangwei on 2016/7/4 0004.
 */
public class RequestThirdpartyLoginRs extends Response {
    private String walletSubscriptionState;
    private String mno;
    private String msisdn;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

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
        return "RequestThirdpartyLoginRs{" +
                "walletSubscriptionState='" + walletSubscriptionState + '\'' +
                ", mno='" + mno + '\'' +
                ", msisdn='" + msisdn + '\'' +
                '}';
    }
}
