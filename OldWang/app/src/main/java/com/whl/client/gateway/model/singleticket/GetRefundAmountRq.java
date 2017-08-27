package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by liwx on 2015/10/26.
 */
public class GetRefundAmountRq extends Request {
    private String orderNo;

    private String cityCode;

    @Override
    public String toString() {
        return "GetRefundAmountRq{" +
                "orderNo='" + orderNo + '\'' +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }

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
}
