package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

import java.util.List;

/**
 * Created by liwx on 2015/10/22.
 */
public class GetStationCodeListRs extends Response {
    private int updateStationVersion;
    private List<StationCode> stationCodeList;

    public List<StationCode> getStationCodeList() {
        return stationCodeList;
    }

    public void setStationCodeList(List<StationCode> stationCodeList) {
        this.stationCodeList = stationCodeList;
    }

    public int getUpdateStationVersion() {
        return updateStationVersion;
    }

    public void setUpdateStationVersion(int updateStationVersion) {
        this.updateStationVersion = updateStationVersion;
    }

    @Override
    public String toString() {
        return "GetStationCodeListRs{" +
            "updateStationVersion=" + updateStationVersion +
            ", stationCodeList=" + stationCodeList +
            '}';
    }
}
