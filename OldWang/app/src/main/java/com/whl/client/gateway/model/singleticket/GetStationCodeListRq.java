package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by liwx on 2015/10/22.
 */
public class GetStationCodeListRq extends Request {
    private String cityCode;
    private String lineCode;
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

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    @Override
    public String toString() {
        return "GetStationCodeListRq{" +
            "cityCode='" + cityCode + '\'' +
            ", lineCode='" + lineCode + '\'' +
            ", walletId='" + walletId + '\'' +
            '}';
    }
}
