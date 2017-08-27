package com.whl.client.gateway.model.singleticket;

import com.amap.api.services.core.PoiItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/5/30.
 * 出口周边信息
 */

public class ExitPoi implements Serializable {
    private ExitData exitData;
    private List<PoiItem> poiItemList;// poi数据

    public ExitData getExitData() {
        return exitData;
    }

    public void setExitData(ExitData exitData) {
        this.exitData = exitData;
    }

    public List<PoiItem> getPoiItemList() {
        return poiItemList;
    }

    public void setPoiItemList(List<PoiItem> poiItemList) {
        this.poiItemList = poiItemList;
    }

    @Override
    public String toString() {
        return "ExitPoi{" + "exitData=" + exitData + ", poiItemList=" + poiItemList + '}';
    }
}
