
package com.whl.client.gateway.model.inbox;


import com.whl.framework.http.model.Response;
import com.whl.client.gateway.model.PageInfo;

import java.util.List;


public class GetInboxMessageListRs extends Response {

	private PageInfo pageInfo;
	private int unreadCount;
	private List<InboxMessage> inboxMessageList;

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public List<InboxMessage> getInboxMessageList() {
		return inboxMessageList;
	}

	public void setInboxMessageList(List<InboxMessage> inboxMessageList) {
		this.inboxMessageList = inboxMessageList;
	}

	@Override
	public String toString() {
		return "GetInboxMessageListRs [pageInfo=" + pageInfo + ", unreadCount=" + unreadCount + ", inboxMessageList="
				+ inboxMessageList + "]";
	}

}
