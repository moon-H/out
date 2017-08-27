
package com.whl.client.gateway.model.inbox;

import android.os.Parcel;
import android.os.Parcelable;

public class InboxMessage implements Parcelable {

    private String messageId;
    private String messageType;
    private String messageCategory;
    private String providerName;
    private String title;
    private String inboxMessage;
    private String cretDtim;
    private String serviceId;
    private String readYn;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageCategory() {
        return messageCategory;
    }

    public void setMessageCategory(String messageCategory) {
        this.messageCategory = messageCategory;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInboxMessage() {
        return inboxMessage;
    }

    public void setInboxMessage(String inboxMessage) {
        this.inboxMessage = inboxMessage;
    }

    public String getCretDtim() {
        return cretDtim;
    }

    public void setCretDtim(String cretDtim) {
        this.cretDtim = cretDtim;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getReadYn() {
        return readYn;
    }

    public void setReadYn(String readYn) {
        this.readYn = readYn;
    }

    @Override
    public String toString() {
        return "InboxMessage{" +
                "messageId='" + messageId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", messageCategory='" + messageCategory + '\'' +
                ", providerName='" + providerName + '\'' +
                ", title='" + title + '\'' +
                ", inboxMessage='" + inboxMessage + '\'' +
                ", cretDtim='" + cretDtim + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", readYn='" + readYn + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public static Creator<InboxMessage> CREATOR = new Creator<InboxMessage>() {

        @Override
        public InboxMessage createFromParcel(Parcel source) {
            InboxMessage message = new InboxMessage();
            message.setMessageId(source.readString());
            message.setMessageType(source.readString());
            message.setMessageCategory(source.readString());
            message.setProviderName(source.readString());
            message.setTitle(source.readString());
            message.setInboxMessage(source.readString());
            message.setCretDtim(source.readString());
            message.setServiceId(source.readString());
            message.setReadYn(source.readString());
            message.setContent(source.readString());

            return message;
        }

        @Override
        public InboxMessage[] newArray(int size) {
            // TODO Auto-generated method stub
            return new InboxMessage[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.messageId);
        dest.writeString(this.messageType);
        dest.writeString(this.messageCategory);
        dest.writeString(this.providerName);
        dest.writeString(this.title);
        dest.writeString(this.inboxMessage);
        dest.writeString(this.cretDtim);
        dest.writeString(this.serviceId);
        dest.writeString(this.readYn);
        dest.writeString(this.content);

    }

}
