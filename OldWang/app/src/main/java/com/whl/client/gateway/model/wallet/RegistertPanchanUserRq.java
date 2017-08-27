package com.whl.client.gateway.model.wallet;

import com.whl.framework.http.model.Request;

/**
 * Created by Administrator on 2016/7/7 0007.
 */
public class RegistertPanchanUserRq extends Request {
    private String msisdn;
    private String udid;
    private String appVersion;
    private String osName;
    private String authCode;
    private String seId;

    public String getSeId() {
        return seId;
    }

    public void setSeId(String seId) {
        this.seId = seId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String toString() {
        return "RegistertPanchanUserRq{" +
            "msisdn='" + msisdn + '\'' +
            ", udid='" + udid + '\'' +
            ", appVersion='" + appVersion + '\'' +
            ", osName='" + osName + '\'' +
            ", authCode='" + authCode + '\'' +
            ", seId='" + seId + '\'' +
            '}';
    }
}

