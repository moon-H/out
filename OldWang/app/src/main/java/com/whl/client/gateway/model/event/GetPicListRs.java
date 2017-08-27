package com.whl.client.gateway.model.event;

import com.whl.framework.http.model.Response;
import com.whl.client.gateway.model.Event;

import java.util.List;

/**
 * Created by lenovo on 2016/12/13.
 */

public class GetPicListRs extends Response {
    private List<Event> eventList;

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public String toString() {
        return "GetPicListRs{" +
            "eventList=" + eventList +
            '}';
    }
}
