package com.whl.client.gateway.model.login;

import com.whl.framework.http.model.Request;

/**
 * Created by lenovo on 2017/7/19.
 */

public class RequestWalletLoginByTokenRq extends Request {
    private String walletId;
    private String msisdn;
    private String token;
    private String modelName;
    private String imei;
    private String seId;
    private String imsi;
    private String osName;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Override
    public String toString() {
        return "RequestWalletLoginByTokenRq{" + "walletId='" + walletId + '\'' + ", msisdn='" + msisdn + '\'' + ", token='" + token + '\'' + ", modelName='" + modelName + '\'' + ", imei='" + imei + '\'' + ", seId='" + seId + '\'' + ", imsi='" + imsi + '\'' + ", osName='" + osName + '\'' + ", versionName='" + versionName + '\'' + '}';
    }
}
