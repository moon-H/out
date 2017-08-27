package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by lenovo on 2017/4/15.
 */

public class GetQrCodeSjtStatusRq extends Request {
    private String orderNo;
    private String cityCode;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return "GetQrCodeSjtStatusRq{" + "orderNo='" + orderNo + '\'' + ", cityCode='" + cityCode + '\'' + '}';
    }
}
