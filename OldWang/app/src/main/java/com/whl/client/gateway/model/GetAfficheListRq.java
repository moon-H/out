package com.whl.client.gateway.model;

import com.whl.framework.http.model.Request;

/**
 * Created by lenovo on 2016/12/8.
 */

public class GetAfficheListRq extends Request {
    private String walletId;
    private String cityCode;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return "GetAfficheListRq{" +
            "walletId='" + walletId + '\'' +
            ", cityCode='" + cityCode + '\'' +
            '}';
    }
}
