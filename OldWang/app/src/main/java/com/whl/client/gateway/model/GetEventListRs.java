package com.whl.client.gateway.model;

import com.whl.framework.http.model.Response;

import java.util.List;

/**
 * Created by liwx on 2015/9/15.
 */
public class GetEventListRs extends Response {
    private List<Event> topEvent;
    private List<Event> bottomEvent;

    public List<Event> getTopEvent() {
        return topEvent;
    }

    public void setTopEvent(List<Event> topEvent) {
        this.topEvent = topEvent;
    }

    public List<Event> getBottomEvent() {
        return bottomEvent;
    }

    public void setBottomEvent(List<Event> bottomEvent) {
        this.bottomEvent = bottomEvent;
    }

    @Override
    public String toString() {
        return "GetEventListRs{" +
            "topEvent=" + topEvent +
            ", bottomEvent=" + bottomEvent +
            '}';
    }
}
