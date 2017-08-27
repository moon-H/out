package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by liwx on 2015/10/22.
 */
public class GetLineCodeListRq extends Request {
    private String cityCode;
    private String walletId;

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
        return "GetLineCodeListRq{" +
            "cityCode='" + cityCode + '\'' +
            ", walletId='" + walletId + '\'' +
            '}';
    }
}
