package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by liwx on 2015/10/22.
 */
public class GetTicketPriceByStationRq extends Request {

    private String cityCode;
    private String startStationCode;
    private String endStationCode;
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

    public String getStartStationCode() {
        return startStationCode;
    }

    public void setStartStationCode(String startStationCode) {
        this.startStationCode = startStationCode;
    }

    public String getEndStationCode() {
        return endStationCode;
    }

    public void setEndStationCode(String endStationCode) {
        this.endStationCode = endStationCode;
    }

    @Override
    public String toString() {
        return "GetTicketPriceByStationRq{" +
            "cityCode='" + cityCode + '\'' +
            ", startStationCode='" + startStationCode + '\'' +
            ", endStationCode='" + endStationCode + '\'' +
            ", walletId='" + walletId + '\'' +
            '}';
    }
}
