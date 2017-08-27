package com.whl.client.gateway.model.login;

import com.whl.framework.http.model.Request;

/**
 * Created by lenovo on 2017/7/18.
 */

public class RequestWalletLoginBySmsRq extends Request {

    private String walletId;
    private String msisdn;
    private String authCode;
    private String modelName;
    private String imei;
    private String seId;
    private String imsi;
    private String mobileId;
    private String osName;
    private String mno;
    private String channelCode;
    private String udid;
    private String versionName;

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

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
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

    public String getSeId() {
        return seId;
    }

    public void setSeId(String seId) {
        this.seId = seId;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

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

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Override
    public String toString() {
        return "RequestWalletLoginBySmsRq{" + "walletId='" + walletId + '\'' + ", msisdn='" + msisdn + '\'' + ", authCode='" + authCode + '\'' + ", modelName='" + modelName + '\'' + ", imei='" + imei + '\'' + ", seId='" + seId + '\'' + ", imsi='" + imsi + '\'' + ", mobileId='" + mobileId + '\'' + ", osName='" + osName + '\'' + ", mno='" + mno + '\'' + ", channelCode='" + channelCode + '\'' + ", udid='" + udid + '\'' + ", versionName='" + versionName + '\'' + '}';
    }
}
