package com.whl.client.gateway.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/10/18.
 */
public class CssPushInboxMessage implements Serializable {
    private String messageType;
    private String messageId;
    private String messageTitle;
    private String url;
    private String messageContent;
    private String messageTime;

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    @Override
    public String toString() {
        return "CssPushInboxMessage{" +
            "messageType='" + messageType + '\'' +
            ", messageId='" + messageId + '\'' +
            ", messageTitle='" + messageTitle + '\'' +
            ", url='" + url + '\'' +
            ", messageContent='" + messageContent + '\'' +
            ", messageTime='" + messageTime + '\'' +
            '}';
    }
}
