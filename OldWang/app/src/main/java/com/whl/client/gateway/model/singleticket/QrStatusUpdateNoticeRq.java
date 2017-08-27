package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by lenovo on 2017/6/7.
 */

public class QrStatusUpdateNoticeRq extends Request {
    private String cityCode;
    private String deviceId;
    private int sjtId;
    private String sjtStatus;
    private String handleDateTime;
    private String trxType;
    private int trxAmount;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getSjtId() {
        return sjtId;
    }

    public void setSjtId(int sjtId) {
        this.sjtId = sjtId;
    }

    public String getSjtStatus() {
        return sjtStatus;
    }

    public void setSjtStatus(String sjtStatus) {
        this.sjtStatus = sjtStatus;
    }

    public String getHandleDateTime() {
        return handleDateTime;
    }

    public void setHandleDateTime(String handleDateTime) {
        this.handleDateTime = handleDateTime;
    }

    public String getTrxType() {
        return trxType;
    }

    public void setTrxType(String trxType) {
        this.trxType = trxType;
    }

    public int getTrxAmount() {
        return trxAmount;
    }

    public void setTrxAmount(int trxAmount) {
        this.trxAmount = trxAmount;
    }

    @Override
    public String toString() {
        return "QrStatusUpdateNoticeRq{" + "cityCode='" + cityCode + '\'' + ", deviceId='" + deviceId + '\'' + ", sjtId=" + sjtId + ", sjtStatus='" + sjtStatus + '\'' + ", handleDateTime='" + handleDateTime + '\'' + ", trxType='" + trxType + '\'' + ", trxAmount=" + trxAmount + '}';
    }
}
