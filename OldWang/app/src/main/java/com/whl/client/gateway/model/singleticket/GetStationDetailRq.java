package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by lenovo on 2017/5/15.
 */

public class GetStationDetailRq extends Request {
    private String cityCode;
    private String stationCode;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    @Override
    public String toString() {
        return "GetSupportServiceYnRq{" + "cityCode='" + cityCode + '\'' + ", stationCode='" + stationCode + '\'' + '}';
    }
}
