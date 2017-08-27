package com.whl.client.gateway.model.inbox;

import com.whl.framework.http.model.Request;

import java.util.List;

public class DeleteInboxMessageRq extends Request {
	private List<String> messageIdList;

	public List<String> getMessageIdList() {
		return messageIdList;
	}

	public void setMessageIdList(List<String> messageIdList) {
		this.messageIdList = messageIdList;
	}

	@Override
	public String toString() {
		return "DeleteInboxMessageRq [messageIdList=" + messageIdList + "]";
	}
	


}
