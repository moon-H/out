package com.whl.client.gateway.model.singleticket;

import java.io.Serializable;

/**
 * Created by liwx on 2015/10/26.
 */
public class PurchaseOrder implements Serializable {
    private String orderNo;
    private int orderStatus;
    private String orderDate;
    private String paymentDate;
    private String cancelOrderDate;
    private String refundTicketDate;
    private int refundTicketAmount;
    private String takeTicketDate;
    private String pickupLineCode;
    private String pickupLineName;
    private String pickupStationCode;
    private String pickupStationNameZH;
    private String getoffLineCode;
    private String getoffLineName;
    private String getoffStationCode;
    private String getoffStationNameZH;
    private String singleTicketType;
    private int ticketPrice;
    private int ticketTotalAmount;
    private int singleTicketNum;
    private int completeTicketNum;
    private String takeTicketToken;
    private String takeTicketSeqNum;
    private String slineBgColor;
    private String slineShortName;
    private String elineBgColor;
    private String elineShortName;

    private float refundRate;
    private int refundAmount;
    private int ticketAmount;
    private String productName;
    private String cityCode;
    private String categoryCode;
    private String categoryName;
    private String cityName;

    private String entryDatetime;
    private String exitDatetime;
    private String actualExitStationCode;
    private String actualExitStationName;
    private String actualExitlineBgColor;
    private String actualExitlineShortName;
    private int payUponArrivalAmount;
    private String externalOrderNo;

    public String getExternalOrderNo() {
        return externalOrderNo;
    }

    public void setExternalOrderNo(String externalOrderNo) {
        this.externalOrderNo = externalOrderNo;
    }

    public int getPayUponArrivalAmount() {
        return payUponArrivalAmount;
    }

    public void setPayUponArrivalAmount(int payUponArrivalAmount) {
        this.payUponArrivalAmount = payUponArrivalAmount;
    }

    public float getRefundRate() {
        return refundRate;
    }

    public void setRefundRate(float refundRate) {
        this.refundRate = refundRate;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }


    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getCancelOrderDate() {
        return cancelOrderDate;
    }

    public void setCancelOrderDate(String cancelOrderDate) {
        this.cancelOrderDate = cancelOrderDate;
    }

    public String getRefundTicketDate() {
        return refundTicketDate;
    }

    public void setRefundTicketDate(String refundTicketDate) {
        this.refundTicketDate = refundTicketDate;
    }

    public int getRefundTicketAmount() {
        return refundTicketAmount;
    }

    public void setRefundTicketAmount(int refundTicketAmount) {
        this.refundTicketAmount = refundTicketAmount;
    }

    public String getTakeTicketDate() {
        return takeTicketDate;
    }

    public void setTakeTicketDate(String takeTicketDate) {
        this.takeTicketDate = takeTicketDate;
    }

    public String getPickupLineCode() {
        return pickupLineCode;
    }

    public void setPickupLineCode(String pickupLineCode) {
        this.pickupLineCode = pickupLineCode;
    }

    public String getPickupLineName() {
        return pickupLineName;
    }

    public void setPickupLineName(String pickupLineName) {
        this.pickupLineName = pickupLineName;
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

    public String getGetoffLineCode() {
        return getoffLineCode;
    }

    public void setGetoffLineCode(String getoffLineCode) {
        this.getoffLineCode = getoffLineCode;
    }

    public String getGetoffLineName() {
        return getoffLineName;
    }

    public void setGetoffLineName(String getoffLineName) {
        this.getoffLineName = getoffLineName;
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

    public String getSingleTicketType() {
        return singleTicketType;
    }

    public void setSingleTicketType(String singleTicketType) {
        this.singleTicketType = singleTicketType;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getTicketTotalAmount() {
        return ticketTotalAmount;
    }

    public void setTicketTotalAmount(int ticketTotalAmount) {
        this.ticketTotalAmount = ticketTotalAmount;
    }

    public int getSingleTicketNum() {
        return singleTicketNum;
    }

    public void setSingleTicketNum(int singleTicketNum) {
        this.singleTicketNum = singleTicketNum;
    }

    public int getCompleteTicketNum() {
        return completeTicketNum;
    }

    public void setCompleteTicketNum(int completeTicketNum) {
        this.completeTicketNum = completeTicketNum;
    }

    public String getTakeTicketToken() {
        return takeTicketToken;
    }

    public void setTakeTicketToken(String takeTicketToken) {
        this.takeTicketToken = takeTicketToken;
    }

    public String getTakeTicketSeqNum() {
        return takeTicketSeqNum;
    }

    public void setTakeTicketSeqNum(String takeTicketSeqNum) {
        this.takeTicketSeqNum = takeTicketSeqNum;
    }

    public int getTicketAmount() {
        return ticketAmount;
    }

    public void setTicketAmount(int ticketAmount) {
        this.ticketAmount = ticketAmount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSlineBgColor() {
        return slineBgColor;
    }

    public void setSlineBgColor(String slineBgColor) {
        this.slineBgColor = slineBgColor;
    }

    public String getSlineShortName() {
        return slineShortName;
    }

    public void setSlineShortName(String slineShortName) {
        this.slineShortName = slineShortName;
    }

    public String getElineBgColor() {
        return elineBgColor;
    }

    public void setElineBgColor(String elineBgColor) {
        this.elineBgColor = elineBgColor;
    }

    public String getElineShortName() {
        return elineShortName;
    }

    public void setElineShortName(String elineShortName) {
        this.elineShortName = elineShortName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(String entryDatetime) {
        this.entryDatetime = entryDatetime;
    }

    public String getExitDatetime() {
        return exitDatetime;
    }

    public void setExitDatetime(String exitDatetime) {
        this.exitDatetime = exitDatetime;
    }

    public String getActualExitStationCode() {
        return actualExitStationCode;
    }

    public void setActualExitStationCode(String actualExitStationCode) {
        this.actualExitStationCode = actualExitStationCode;
    }

    public String getActualExitStationName() {
        return actualExitStationName;
    }

    public void setActualExitStationName(String actualExitStationName) {
        this.actualExitStationName = actualExitStationName;
    }

    public String getActualExitlineBgColor() {
        return actualExitlineBgColor;
    }

    public void setActualExitlineBgColor(String actualExitlineBgColor) {
        this.actualExitlineBgColor = actualExitlineBgColor;
    }

    public String getActualExitlineShortName() {
        return actualExitlineShortName;
    }

    public void setActualExitlineShortName(String actualExitlineShortName) {
        this.actualExitlineShortName = actualExitlineShortName;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" + "orderNo='" + orderNo + '\'' + ", orderStatus=" + orderStatus + ", orderDate='" + orderDate + '\'' + ", paymentDate='" + paymentDate + '\'' + ", cancelOrderDate='" + cancelOrderDate + '\'' + ", refundTicketDate='" + refundTicketDate + '\'' + ", refundTicketAmount=" + refundTicketAmount + ", takeTicketDate='" + takeTicketDate + '\'' + ", pickupLineCode='" + pickupLineCode + '\'' + ", pickupLineName='" + pickupLineName + '\'' + ", pickupStationCode='" + pickupStationCode + '\'' + ", pickupStationNameZH='" + pickupStationNameZH + '\'' + ", getoffLineCode='" + getoffLineCode + '\'' + ", getoffLineName='" + getoffLineName + '\'' + ", getoffStationCode='" + getoffStationCode + '\'' + ", getoffStationNameZH='" + getoffStationNameZH + '\'' + ", singleTicketType='" + singleTicketType + '\'' + ", ticketPrice=" + ticketPrice + ", ticketTotalAmount=" + ticketTotalAmount + ", singleTicketNum=" + singleTicketNum + ", completeTicketNum=" + completeTicketNum + ", takeTicketToken='" + takeTicketToken + '\'' + ", takeTicketSeqNum='" + takeTicketSeqNum + '\'' + ", slineBgColor='" + slineBgColor + '\'' + ", slineShortName='" + slineShortName + '\'' + ", elineBgColor='" + elineBgColor + '\'' + ", elineShortName='" + elineShortName + '\'' + ", refundRate=" + refundRate + ", refundAmount=" + refundAmount + ", ticketAmount=" + ticketAmount + ", productName='" + productName + '\'' + ", cityCode='" + cityCode + '\'' + ", categoryCode='" + categoryCode + '\'' + ", categoryName='" + categoryName + '\'' + ", cityName='" + cityName + '\'' + ", entryDatetime='" + entryDatetime + '\'' + ", exitDatetime='" + exitDatetime + '\'' + ", actualExitStationCode='" + actualExitStationCode + '\'' + ", actualExitStationName='" + actualExitStationName + '\'' + ", actualExitlineBgColor='" + actualExitlineBgColor + '\'' + ", actualExitlineShortName='" + actualExitlineShortName + '\'' + ", payUponArrivalAmount=" + payUponArrivalAmount + ", externalOrderNo='" + externalOrderNo + '\'' + '}';
    }
}
