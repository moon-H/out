package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

/**
 * Created by lh on 2015/1/29.
 */
public class GetCityInfoRq extends Request {
    private String originalCityCode;//城市代码
    private String isGdYn;

    public String getOriginalCityCode() {
        return originalCityCode;
    }

    public void setOriginalCityCode(String originalCityCode) {
        this.originalCityCode = originalCityCode;
    }

    public String getIsGdYn() {
        return isGdYn;
    }

    public void setIsGdYn(String isGdYn) {
        this.isGdYn = isGdYn;
    }

    @Override
    public String toString() {
        return "GetCityInfoRq{" + "originalCityCode='" + originalCityCode + '\'' + ", isGdYn='" + isGdYn + '\'' + '}';
    }
}
