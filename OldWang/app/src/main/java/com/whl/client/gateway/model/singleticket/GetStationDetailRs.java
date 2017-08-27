package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

import java.util.List;

/**
 * Created by liwx on 2017/5/15.
 */

public class GetStationDetailRs extends Response {
    private StationCode stationCode;
    private List<ExitData> exitDataList;

    public StationCode getStationCode() {
        return stationCode;
    }

    public void setStationCode(StationCode stationCode) {
        this.stationCode = stationCode;
    }

    public List<ExitData> getExitDataList() {
        return exitDataList;
    }

    public void setExitDataList(List<ExitData> exitDataList) {
        this.exitDataList = exitDataList;
    }

    @Override
    public String toString() {
        return "GetSupportServiceYnRs{" + "stationCode=" + stationCode + ", exitDataList=" + exitDataList + '}';
    }
}
