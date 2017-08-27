package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by liwx on 2015/10/22.
 */
public class GetBuySinlgeTicketMaxNumRq extends Request {
    private String cityCode;
    private String walletId;
    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    @Override
    public String toString() {
        return "GetBuySinlgeTicketMaxNumRq{" +
            "cityCode='" + cityCode + '\'' +
            ", walletId='" + walletId + '\'' +
            '}';
    }
}
