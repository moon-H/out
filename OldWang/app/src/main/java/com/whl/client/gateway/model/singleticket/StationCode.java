package com.whl.client.gateway.model.singleticket;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by liwx on 2015/10/22.
 */
public class StationCode extends DataSupport implements Serializable {
    /*******/
    private int id;
    private String lineCode;
    private String stationCode;
    private String stationNameZH;
    private String stationNameEN;
    private double stationLat;
    private double stationLong;
    private String stationInitial;
    private double dotX;
    private double dotY;
    private double btnOriginX;
    private double btnOriginY;
    private double btnWidth;
    private double btnHeight;
    private String useYn;
    private int orderIndex;
    private String lineBgColor;
    private String lineShortName;
    private String transferYn;
    private String toAirportYn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDotX() {
        return dotX;
    }

    public void setDotX(double dotX) {
        this.dotX = dotX;
    }

    public double getDotY() {
        return dotY;
    }

    public void setDotY(double dotY) {
        this.dotY = dotY;
    }

    public double getBtnOriginX() {
        return btnOriginX;
    }

    public void setBtnOriginX(double btnOriginX) {
        this.btnOriginX = btnOriginX;
    }

    public double getBtnOriginY() {
        return btnOriginY;
    }

    public void setBtnOriginY(double btnOriginY) {
        this.btnOriginY = btnOriginY;
    }

    public double getBtnWidth() {
        return btnWidth;
    }

    public void setBtnWidth(double btnWidth) {
        this.btnWidth = btnWidth;
    }

    public double getBtnHeight() {
        return btnHeight;
    }

    public void setBtnHeight(double btnHeight) {
        this.btnHeight = btnHeight;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getStationNameZH() {
        return stationNameZH;
    }

    public void setStationNameZH(String stationNameZH) {
        this.stationNameZH = stationNameZH;
    }

    public String getStationNameEN() {
        return stationNameEN;
    }

    public void setStationNameEN(String stationNameEN) {
        this.stationNameEN = stationNameEN;
    }

    public String getStationInitial() {
        return stationInitial;
    }

    public void setStationInitial(String stationInitial) {
        this.stationInitial = stationInitial;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getLineBgColor() {
        return lineBgColor;
    }

    public void setLineBgColor(String lineBgColor) {
        this.lineBgColor = lineBgColor;
    }

    public String getLineShortName() {
        return lineShortName;
    }

    public void setLineShortName(String lineShortName) {
        this.lineShortName = lineShortName;
    }

    public String getTransferYn() {
        return transferYn;
    }

    public void setTransferYn(String transferYn) {
        this.transferYn = transferYn;
    }

    public String getToAirportYn() {
        return toAirportYn;
    }

    public void setToAirportYn(String toAirportYn) {
        this.toAirportYn = toAirportYn;
    }

    public double getStationLat() {
        return stationLat;
    }

    public void setStationLat(double stationLat) {
        this.stationLat = stationLat;
    }

    public double getStationLong() {
        return stationLong;
    }

    public void setStationLong(double stationLong) {
        this.stationLong = stationLong;
    }

    @Override
    public String toString() {
        return "StationCode{" + "id=" + id + ", lineCode='" + lineCode + '\'' + ", stationCode='" + stationCode + '\'' + ", stationNameZH='" + stationNameZH + '\'' + ", stationNameEN='" + stationNameEN + '\'' + ", stationLat=" + stationLat + ", stationLong=" + stationLong + ", stationInitial='" + stationInitial + '\'' + ", dotX=" + dotX + ", dotY=" + dotY + ", btnOriginX=" + btnOriginX + ", btnOriginY=" + btnOriginY + ", btnWidth=" + btnWidth + ", btnHeight=" + btnHeight + ", useYn='" + useYn + '\'' + ", orderIndex=" + orderIndex + ", lineBgColor='" + lineBgColor + '\'' + ", lineShortName='" + lineShortName + '\'' + ", transferYn='" + transferYn + '\'' + ", toAirportYn='" + toAirportYn + '\'' + '}';
    }
}
