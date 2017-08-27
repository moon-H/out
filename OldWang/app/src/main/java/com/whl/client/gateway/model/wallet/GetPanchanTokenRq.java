package com.whl.client.gateway.model.wallet;

import com.whl.framework.http.model.Request;

/**
 * Created by liwx on 2015/9/8.
 */
public class GetPanchanTokenRq extends Request {
    private String udid;
    private String appVersion;
    private String osName;

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

    @Override
    public String toString() {
        return "GetPanchanTokenRq{" +
            "udid='" + udid + '\'' +
            ", appVersion='" + appVersion + '\'' +
            ", osName='" + osName + '\'' +
            '}';
    }
}
