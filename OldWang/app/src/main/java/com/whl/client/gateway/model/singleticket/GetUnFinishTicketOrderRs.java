package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

import java.util.List;

/**
 * Created by lenovo on 2017/7/4.
 */

public class GetUnFinishTicketOrderRs extends Response {
    private List<UnFinishTicketOrder> unFinishTicketOrderList;

    public List<UnFinishTicketOrder> getUnFinishTicketOrderList() {
        return unFinishTicketOrderList;
    }

    public void setUnFinishTicketOrderList(List<UnFinishTicketOrder> unFinishTicketOrderList) {
        this.unFinishTicketOrderList = unFinishTicketOrderList;
    }

    @Override
    public String toString() {
        return "GetUnFinishTicketOrderRs{" + "unFinishTicketOrderList=" + unFinishTicketOrderList + '}';
    }
}
