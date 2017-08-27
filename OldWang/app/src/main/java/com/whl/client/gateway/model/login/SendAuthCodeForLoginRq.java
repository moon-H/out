package com.whl.client.gateway.model.login;

import com.whl.framework.http.model.Request;

/**
 * Created by lenovo on 2017/7/18.
 */

public class SendAuthCodeForLoginRq extends Request {
    private String msisdn;
    private String walletId;
    private String seId;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getSeId() {
        return seId;
    }

    public void setSeId(String seId) {
        this.seId = seId;
    }

    @Override
    public String toString() {
        return "SendAuthCodeForLoginRq{" + "msisdn='" + msisdn + '\'' + ", walletId='" + walletId + '\'' + ", seId='" + seId + '\'' + '}';
    }
}
