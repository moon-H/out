package com.whl.client.gateway.model.myorder;

import com.whl.framework.http.model.Request;

/**
 * Created by liwx on 2015/10/22.
 */
public class GetProductCategoryRq extends Request {
    private String osName;

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    @Override
    public String toString() {
        return "GetProductCategoryRq{" +
            "osName='" + osName + '\'' +
            '}';
    }
}
