package com.whl.client.gateway.model;

import com.whl.framework.http.model.Response;

import java.util.List;

/**
 * Created by lenovo on 2016/12/8.
 */

public class GetAfficheListRs extends Response {
    private List<Affiche> afficheList;

    public List<Affiche> getAfficheList() {
        return afficheList;
    }

    public void setAfficheList(List<Affiche> afficheList) {
        this.afficheList = afficheList;
    }

    @Override
    public String toString() {
        return "GetAfficheListRs{" +
            "afficheList=" + afficheList +
            '}';
    }
}
