
package com.whl.client.gateway.model.inbox;


import com.whl.framework.http.model.Response;


public class GetMessageUnreadCntRs extends Response {

    private int unreadCount;

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    @Override
    public String toString() {
        return "GetMessageUnreadCntRs [unreadCount=" + unreadCount + "]";
    }

}
