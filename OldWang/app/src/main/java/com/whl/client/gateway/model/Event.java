package com.whl.client.gateway.model;

import java.io.Serializable;

/**
 * Created by liwx on 2015/9/15.
 */
public class Event implements Serializable {

    public static final String OPEN_TYPE_NO_RES = "0";
    public static final String OPEN_TYPE_LOCAL = "1";
    public static final String OPEN_TYPE_HTML = "2";


    private String eventId;
    private String eventName;
    private String eventImageUrl;
    private String eventUrl;
    private String regDate;
    private String openType;
    private int location;
    private String shareContent;

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public void setEventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    @Override
    public String toString() {
        return "Event{" + "eventId='" + eventId + '\'' + ", eventName='" + eventName + '\'' + ", eventImageUrl='" + eventImageUrl + '\'' + ", eventUrl='" + eventUrl + '\'' + ", regDate='" + regDate + '\'' + ", openType='" + openType + '\'' + ", location=" + location + ", shareContent='" + shareContent + '\'' + '}';
    }
}
