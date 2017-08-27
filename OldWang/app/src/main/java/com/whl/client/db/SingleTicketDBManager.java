package com.whl.client.db;

import android.content.Context;
import android.text.TextUtils;

import com.whl.client.app.BizApplication;
import com.whl.client.gateway.model.singleticket.LineCode;
import com.whl.client.gateway.model.singleticket.MetroMap;
import com.whl.client.gateway.model.singleticket.StationCode;
import com.whl.client.home.ticket.SingleTicketManager;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by liwx on 2016/1/13.
 */
public class SingleTicketDBManager {
    private String TAG = "SingleTicketDBManager";

    private Context context;

    public SingleTicketDBManager(Context context) {
        this.context = context;
    }

    public MetroMap getMetroMap(String cityCode) {
        List<MetroMap> list = DataSupport.where("cityCode = ?", cityCode).find(MetroMap.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public void removeAllData(Class<?> clazz) {
        DataSupport.deleteAll(clazz);
    }

    public void saveAllData(List<? extends DataSupport> list) {
        DataSupport.saveAll(list);
    }

    public List<StationCode> getStationCodeByName(String stationName) {
        if (BizApplication.getInstance().getCityCode().equals(BizApplication.CITY_CODE_GUANGZHOU)) {
            return DataSupport.where("stationNameZH = ? and lineCode <> ?", stationName, SingleTicketManager.LINE_CODE_APM).find(StationCode.class);
        } else
            return DataSupport.where("stationNameZH = ?", stationName).find(StationCode.class);
    }

    public static LineCode getLineInfoByLineCode(String lineCode) {
        if (TextUtils.isEmpty(lineCode)) {
            return null;
        }
        List<LineCode> list = DataSupport.where("lineCode = ?", lineCode).find(LineCode.class);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public static List<StationCode> getAllStationCode() {
        if (BizApplication.getInstance().getCityCode().equals(BizApplication.CITY_CODE_GUANGZHOU)) {
            return DataSupport.order("orderIndex asc").where("lineCode <> ?", SingleTicketManager.LINE_CODE_APM).find(StationCode.class);
        } else
            return DataSupport.order("orderIndex asc").find(StationCode.class);

    }

    public static List<StationCode> getStationCodeListByLineCode(String lineCode) {
        return DataSupport.where("lineCode = ?", lineCode).find(StationCode.class);
    }

    public static StationCode getStationInfoByStationName(String stationName) {
        List<StationCode> list = DataSupport.where("stationNameZH = ?", stationName).find(StationCode.class);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public static List<StationCode> getStationInfoListByStationName(String stationName) {
        List<StationCode> list = DataSupport.where("stationNameZH = ?", stationName).find(StationCode.class);
        return list;
    }

    public static StationCode getStationInfoByStationCode(String stationCode) {
        List<StationCode> list = DataSupport.where("stationCode = ?", stationCode).find(StationCode.class);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }


}
