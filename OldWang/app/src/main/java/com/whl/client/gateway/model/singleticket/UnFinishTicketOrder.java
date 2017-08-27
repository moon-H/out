package com.whl.client.gateway.model.singleticket;

/**
 * Created by lenovo on 2017/7/4.
 */

public class UnFinishTicketOrder {

    private String unFinishTicketOrderList;
    private String orderNo;
    private String externalOrderNo;
    private int orderStatus;
    private String pickupStationCode;
    private String pickupStationNameZH;
    private String getoffStationCode;
    private String getoffStationNameZH;
    private String getUponStationCode;
    private String getUponStationNameZH;
    private String categoryCode;

    public String getUnFinishTicketOrderList() {
        return unFinishTicketOrderList;
    }

    public void setUnFinishTicketOrderList(String unFinishTicketOrderList) {
        this.unFinishTicketOrderList = unFinishTicketOrderList;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getExternalOrderNo() {
        return externalOrderNo;
    }

    public void setExternalOrderNo(String externalOrderNo) {
        this.externalOrderNo = externalOrderNo;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPickupStationCode() {
        return pickupStationCode;
    }

    public void setPickupStationCode(String pickupStationCode) {
        this.pickupStationCode = pickupStationCode;
    }

    public String getPickupStationNameZH() {
        return pickupStationNameZH;
    }

    public void setPickupStationNameZH(String pickupStationNameZH) {
        this.pickupStationNameZH = pickupStationNameZH;
    }

    public String getGetoffStationCode() {
        return getoffStationCode;
    }

    public void setGetoffStationCode(String getoffStationCode) {
        this.getoffStationCode = getoffStationCode;
    }

    public String getGetoffStationNameZH() {
        return getoffStationNameZH;
    }

    public void setGetoffStationNameZH(String getoffStationNameZH) {
        this.getoffStationNameZH = getoffStationNameZH;
    }

    public String getGetUponStationCode() {
        return getUponStationCode;
    }

    public void setGetUponStationCode(String getUponStationCode) {
        this.getUponStationCode = getUponStationCode;
    }

    public String getGetUponStationNameZH() {
        return getUponStationNameZH;
    }

    public void setGetUponStationNameZH(String getUponStationNameZH) {
        this.getUponStationNameZH = getUponStationNameZH;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @Override
    public String toString() {
        return "UnFinishTicketOrder{" + "unFinishTicketOrderList='" + unFinishTicketOrderList + '\'' + ", orderNo='" + orderNo + '\'' + ", externalOrderNo='" + externalOrderNo + '\'' + ", orderStatus=" + orderStatus + ", pickupStationCode='" + pickupStationCode + '\'' + ", pickupStationNameZH='" + pickupStationNameZH + '\'' + ", getoffStationCode='" + getoffStationCode + '\'' + ", getoffStationNameZH='" + getoffStationNameZH + '\'' + ", getUponStationCode='" + getUponStationCode + '\'' + ", getUponStationNameZH='" + getUponStationNameZH + '\'' + ", categoryCode='" + categoryCode + '\'' + '}';
    }
}
