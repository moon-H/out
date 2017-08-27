package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by liwx on 2015/10/26.
 */
public class RequestRefundTicketRq extends Request {
    private String orderNo;
    private int orderAmount;
    private int refundAmount;
    private String cityCode;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "RequestRefundTicketRq{" +
                "orderNo='" + orderNo + '\'' +
                ", orderAmount=" + orderAmount +
                ", refundAmount=" + refundAmount +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }
}
