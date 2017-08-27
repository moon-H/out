package com.whl.client.gateway.model.singleticket;

import org.litepal.crud.DataSupport;

/**
 * Created by liwx on 2015/10/22.
 */
public class LineCode extends DataSupport {
    private int id;
    private String lineCode;
    private String lineNameZH;
    private String lineNameEN;
    private int stationsCnt;
    private String isLoop;
    private String startStationCode;
    private String startStationNameZh;
    private String endStationCode;
    private String endStationNameZh;
    private int openStationCnt;
    private int orderIndex;
    private String bgColor;
    private String shortName;
    private String toAirportYn;

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getLineNameZH() {
        return lineNameZH;
    }

    public void setLineNameZH(String lineNameZH) {
        this.lineNameZH = lineNameZH;
    }

    public String getLineNameEN() {
        return lineNameEN;
    }

    public void setLineNameEN(String lineNameEN) {
        this.lineNameEN = lineNameEN;
    }

    public int getStationsCnt() {
        return stationsCnt;
    }

    public void setStationsCnt(int stationsCnt) {
        this.stationsCnt = stationsCnt;
    }

    public String getIsLoop() {
        return isLoop;
    }

    public void setIsLoop(String isLoop) {
        this.isLoop = isLoop;
    }

    public String getStartStationCode() {
        return startStationCode;
    }

    public void setStartStationCode(String startStationCode) {
        this.startStationCode = startStationCode;
    }

    public String getStartStationNameZh() {
        return startStationNameZh;
    }

    public void setStartStationNameZh(String startStationNameZh) {
        this.startStationNameZh = startStationNameZh;
    }

    public String getEndStationCode() {
        return endStationCode;
    }

    public void setEndStationCode(String endStationCode) {
        this.endStationCode = endStationCode;
    }

    public String getEndStationNameZh() {
        return endStationNameZh;
    }

    public void setEndStationNameZh(String endStationNameZh) {
        this.endStationNameZh = endStationNameZh;
    }

    public int getOpenStationCnt() {
        return openStationCnt;
    }

    public void setOpenStationCnt(int openStationCnt) {
        this.openStationCnt = openStationCnt;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getToAirportYn() {
        return toAirportYn;
    }

    public void setToAirportYn(String toAirportYn) {
        this.toAirportYn = toAirportYn;
    }

    @Override
    public String toString() {
        return "LineCode{" +
            "id=" + id +
            ", lineCode='" + lineCode + '\'' +
            ", lineNameZH='" + lineNameZH + '\'' +
            ", lineNameEN='" + lineNameEN + '\'' +
            ", stationsCnt=" + stationsCnt +
            ", isLoop='" + isLoop + '\'' +
            ", startStationCode='" + startStationCode + '\'' +
            ", startStationNameZh='" + startStationNameZh + '\'' +
            ", endStationCode='" + endStationCode + '\'' +
            ", endStationNameZh='" + endStationNameZh + '\'' +
            ", openStationCnt=" + openStationCnt +
            ", orderIndex=" + orderIndex +
            ", bgColor='" + bgColor + '\'' +
            ", shortName='" + shortName + '\'' +
            ", toAirportYn='" + toAirportYn + '\'' +
            '}';
    }
}
