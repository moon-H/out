package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

import java.util.List;

/**
 * Created by lenovo on 2015/10/22.
 */
public class GetTicketPriceListRs extends Response {
    private List<SingelTicketFixedPricePool> ticketPriceList;

    public List<SingelTicketFixedPricePool> getTicketPriceList() {
        return ticketPriceList;
    }

    public void setTicketPriceList(List<SingelTicketFixedPricePool> ticketPriceList) {
        this.ticketPriceList = ticketPriceList;
    }

    @Override
    public String toString() {
        return "GetTicketPriceListRs{" +
            "ticketPriceList=" + ticketPriceList +
            '}';
    }
}
