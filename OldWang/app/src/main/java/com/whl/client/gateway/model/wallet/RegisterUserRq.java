package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Request;


public class RegisterUserRq extends Request {

    private String msisdn;
    private String loginPassword;
    private String email;
    private String seId;
    private String modelName;
    private String imei;
    private String mno;
    private String walletId;
    private String authCode;
    private String imsi;
    private String udid;
    private String channelCode;

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSeId() {
        return seId;
    }

    public void setSeId(String seId) {
        this.seId = seId;
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

    @Override
    public String toString() {
        return "RegisterUserRq{" +
            "msisdn='" + msisdn + '\'' +
            ", loginPassword='" + loginPassword + '\'' +
            ", email='" + email + '\'' +
            ", seId='" + seId + '\'' +
            ", modelName='" + modelName + '\'' +
            ", imei='" + imei + '\'' +
            ", mno='" + mno + '\'' +
            ", walletId='" + walletId + '\'' +
            ", authCode='" + authCode + '\'' +
            ", imsi='" + imsi + '\'' +
            ", udid='" + udid + '\'' +
            ", channelCode='" + channelCode + '\'' +
            '}';
    }
}
