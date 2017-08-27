/**
 *
 */
package com.whl.client.home.ticket.distance;

import com.whl.client.gateway.model.singleticket.StationCode;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * desc：地铁站对象
 *
 * @author chaisson
 * @since 2015-5-31 上午10:22:44
 */
public class CssStation {

    private String name; //地铁站名称，假设具备唯一性

    public CssStation prev; //本站在lineNo线上面的前一个站

    public CssStation next; //本站在lineNo线上面的后一个站

    private StationCode stationCode;

    //本站到某一个目标站(key)所经过的所有站集合(value)，保持前后顺序
    private Map<CssStation, LinkedHashSet<CssStation>> orderSetMap = new HashMap<CssStation, LinkedHashSet<CssStation>>();

    public CssStation(StationCode stationCode) {
        this.name = stationCode.getStationNameZH();
        this.stationCode = stationCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedHashSet<CssStation> getAllPassedStations(CssStation station) {
        if (orderSetMap.get(station) == null) {
            LinkedHashSet<CssStation> set = new LinkedHashSet<CssStation>();
            set.add(this);
            orderSetMap.put(station, set);
        }
        return orderSetMap.get(station);
    }

    public Map<CssStation, LinkedHashSet<CssStation>> getOrderSetMap() {
        return orderSetMap;
    }

    public StationCode getStationCode() {
        return stationCode;
    }

    public void setStationCode(StationCode stationCode) {
        this.stationCode = stationCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof CssStation) {
            CssStation s = (CssStation) obj;
            if (s.getName().equals(this.getName())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
