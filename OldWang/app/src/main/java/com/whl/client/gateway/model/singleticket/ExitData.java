package com.whl.client.gateway.model.singleticket;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/5/15.
 */

public class ExitData implements Serializable {
    private String exitName;
    private double exitLat;
    private double exitLong;
    private String exitContent;

    public String getExitName() {
        return exitName;
    }

    public void setExitName(String exitName) {
        this.exitName = exitName;
    }

    public double getExitLat() {
        return exitLat;
    }

    public void setExitLat(double exitLat) {
        this.exitLat = exitLat;
    }

    public double getExitLong() {
        return exitLong;
    }

    public void setExitLong(double exitLong) {
        this.exitLong = exitLong;
    }

    public String getExitContent() {
        return exitContent;
    }

    public void setExitContent(String exitContent) {
        this.exitContent = exitContent;
    }

    @Override
    public String toString() {
        return "ExitData{" +
                "exitName='" + exitName + '\'' +
                ", exitLat=" + exitLat +
                ", exitLong=" + exitLong +
                ", exitContent='" + exitContent + '\'' +
                '}';
    }
}
