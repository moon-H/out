package com.whl.client.gateway.model.wallet;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/3/20.
 * 用户在其他设备登录信息
 */
public class UserDeviceChangeInfo implements Serializable {
    private String loginDate;
    private String loginType;
    private String loginDevice;

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getLoginDevice() {
        return loginDevice;
    }

    public void setLoginDevice(String loginDevice) {
        this.loginDevice = loginDevice;
    }

    @Override
    public String toString() {
        return "UserDeviceChangeInfo{" +
            "loginDate='" + loginDate + '\'' +
            ", loginType='" + loginType + '\'' +
            ", loginDevice='" + loginDevice + '\'' +
            '}';
    }
}
