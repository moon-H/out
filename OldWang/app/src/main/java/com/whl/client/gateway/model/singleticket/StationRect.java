package com.whl.client.gateway.model.singleticket;

import android.graphics.Rect;

/**
 * Created by liwx on 2015/11/24.
 */
public class StationRect {
    private StationCode stationCode;
    private Rect btnRect;
    private Rect dotRect;

    public StationCode getStationCode() {
        return stationCode;
    }

    public void setStationCode(StationCode stationCode) {
        this.stationCode = stationCode;
    }

    public Rect getBtnRect() {
        return btnRect;
    }

    public void setBtnRect(Rect btnRect) {
        this.btnRect = btnRect;
    }

    public Rect getDotRect() {
        return dotRect;
    }

    public void setDotRect(Rect dotRect) {
        this.dotRect = dotRect;
    }

    @Override
    public String toString() {
        return "StationRect{" +
            "stationCode=" + stationCode +
            ", btnRect=" + btnRect +
            ", dotRect=" + dotRect +
            '}';
    }
}
