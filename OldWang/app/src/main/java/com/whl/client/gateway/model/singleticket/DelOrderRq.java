package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by liwx on 2015/10/26.
 */
public class DelOrderRq extends Request {
    private String orderNo;
    private String cityCode;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "DelOrderRq{" +
                "orderNo='" + orderNo + '\'' +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }
}
