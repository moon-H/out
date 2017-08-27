package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

/**
 * Created by liwx on 2015/10/26.
 */
public class GetCityInfoRs extends Response {
    private String cityCode;
    private String cityName;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "GetCityInfoRs{" +
                "cityCode='" + cityCode + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
