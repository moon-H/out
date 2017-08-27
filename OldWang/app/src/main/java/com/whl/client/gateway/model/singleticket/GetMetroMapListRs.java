package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

import java.util.List;

/**
 * Created by liwx on 2016/1/6.
 */
public class GetMetroMapListRs extends Response {
    private List<MetroMap> metroMapList;

    public List<MetroMap> getMetroMapList() {
        return metroMapList;
    }

    public void setMetroMapList(List<MetroMap> metroMapList) {
        this.metroMapList = metroMapList;
    }

    @Override
    public String toString() {
        return "GetMetroMapListRs{" +
            "metroMapList=" + metroMapList +
            '}';
    }
}
