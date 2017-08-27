package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;

import java.util.List;

/**
 * Created by liwx on 2016/1/6.
 */
public class GetMetroMapListRq extends Request {
    private List<MetroMapCheck> metroMapCheckList;
    private String osName;

    public List<MetroMapCheck> getMetroMapCheckList() {
        return metroMapCheckList;
    }

    public void setMetroMapCheckList(List<MetroMapCheck> metroMapCheckList) {
        this.metroMapCheckList = metroMapCheckList;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    @Override
    public String toString() {
        return "GetMetroMapListRq{" +
            "metroMapCheckList=" + metroMapCheckList +
            ", osName='" + osName + '\'' +
            '}';
    }
}
