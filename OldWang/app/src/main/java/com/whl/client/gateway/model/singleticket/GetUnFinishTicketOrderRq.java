package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by lenovo on 2017/7/4.
 */

public class GetUnFinishTicketOrderRq extends Request {
    private String cityCode;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return "GetUnFinishTicketOrderRq{" + "cityCode='" + cityCode + '\'' + '}';
    }
}
