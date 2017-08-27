
package com.whl.client.gateway.model.inbox;


import com.whl.framework.http.model.Request;
import com.whl.client.gateway.model.PageInfo;


public class GetInboxMessageListRq extends Request {

	private PageInfo pageInfo;
	private String lastMessageId;
	private String messageType;
	private String messageCategory;
	private String serviceId;

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public String getLastMessageId() {
		return lastMessageId;
	}

	public void setLastMessageId(String lastMessageId) {
		this.lastMessageId = lastMessageId;
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String toString() {
		return "GetInboxMessageListRq [pageInfo=" + pageInfo + ", lastMessageId=" + lastMessageId + ", messageType="
				+ messageType + ", messageCategory=" + messageCategory + ", serviceId=" + serviceId + "]";
	}

}
