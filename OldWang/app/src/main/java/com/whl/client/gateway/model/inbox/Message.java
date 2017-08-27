package com.whl.client.gateway.model.inbox;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/19 0019.
 */
public class Message implements Serializable {
    private String messageId;
    private String msgIconImageUrl;
    private String msgTitle;
    private String msgContent;
    private String msgUrl;
    private String reserve;
    private String cretDtim;
    private String readYn;
    private String msgType;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMsgIconImageUrl() {
        return msgIconImageUrl;
    }

    public void setMsgIconImageUrl(String msgIconImageUrl) {
        this.msgIconImageUrl = msgIconImageUrl;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgUrl() {
        return msgUrl;
    }

    public void setMsgUrl(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getCretDtim() {
        return cretDtim;
    }

    public void setCretDtim(String cretDtim) {
        this.cretDtim = cretDtim;
    }

    public String getReadYn() {
        return readYn;
    }

    public void setReadYn(String readYn) {
        this.readYn = readYn;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "Message{" + "messageId='" + messageId + '\'' + ", msgIconImageUrl='" + msgIconImageUrl + '\'' + ", msgTitle='" + msgTitle + '\'' + ", msgContent='" + msgContent + '\'' + ", msgUrl='" + msgUrl + '\'' + ", reserve='" + reserve + '\'' + ", cretDtim='" + cretDtim + '\'' + ", readYn='" + readYn + '\'' + ", msgType='" + msgType + '\'' + '}';
    }
}
