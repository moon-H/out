package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Request;


public class RequestWalletLoginRq extends Request {

    private String msisdn;
    private String loginPassword;
    private String modelName;
    private String imei;
    private String mobileId;
    private String osName;
    private String seId;
    private String imsi;
    private String walletId;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getSeId() {
        return seId;
    }

    public void setSeId(String seId) {
        this.seId = seId;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }

    @Override
    public String toString() {
        return "RequestWalletLoginRq [msisdn=" + msisdn + ", loginPassword=" + loginPassword + ", modelName=" + modelName + ", imei=" + imei + ", mobileId=" + mobileId + ", osName=" + osName + ", seId=" + seId + ", imsi=" + imsi + "]";
    }

}
