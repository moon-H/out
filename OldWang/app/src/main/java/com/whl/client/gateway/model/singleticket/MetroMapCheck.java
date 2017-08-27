package com.whl.client.gateway.model.singleticket;

/**
 * Created by liwx on 2016/1/6.
 */
public class MetroMapCheck {
    private String cityCode;
    private String currentMetroMapVersion;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCurrentMetroMapVersion() {
        return currentMetroMapVersion;
    }

    public void setCurrentMetroMapVersion(String currentMetroMapVersion) {
        this.currentMetroMapVersion = currentMetroMapVersion;
    }

    @Override
    public String toString() {
        return "MetroMapCheck{" +
            "cityCode='" + cityCode + '\'' +
            ", currentMetroMapVersion='" + currentMetroMapVersion + '\'' +
            '}';
    }
}
