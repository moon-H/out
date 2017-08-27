package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by lenovo on 2017/4/13.
 * 获取二维码信息
 */

public class GetQrCodeSjtRq extends Request {
    private String orderNo;
    private String cityCode;
    private String serviceId;

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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "GetQrCodeSjtRq{" + "orderNo='" + orderNo + '\'' + ", cityCode='" + cityCode + '\'' + ", serviceId='" + serviceId + '\'' + '}';
    }
}
