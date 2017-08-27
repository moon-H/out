package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by lenovo on 2016/10/28.
 */
public class CheckStationCodeVersionRq extends Request {
    private String cityCode;
    private int currentStationVersion;
    private int currentLineVersion;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getCurrentStationVersion() {
        return currentStationVersion;
    }

    public void setCurrentStationVersion(int currentStationVersion) {
        this.currentStationVersion = currentStationVersion;
    }

    public int getCurrentLineVersion() {
        return currentLineVersion;
    }

    public void setCurrentLineVersion(int currentLineVersion) {
        this.currentLineVersion = currentLineVersion;
    }

    @Override
    public String toString() {
        return "CheckStationCodeVersionRq{" +
            "cityCode='" + cityCode + '\'' +
            ", currentStationVersion=" + currentStationVersion +
            ", currentLineVersion=" + currentLineVersion +
            '}';
    }
}
