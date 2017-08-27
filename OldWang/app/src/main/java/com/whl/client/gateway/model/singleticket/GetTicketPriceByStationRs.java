package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

/**
 * Created by liwx on 2015/10/22.
 */
public class GetTicketPriceByStationRs extends Response {

    private int ticketPrice;

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    @Override
    public String toString() {
        return "GetTicketPriceByStationRs{" +
            "ticketPrice=" + ticketPrice +
            '}';
    }
}
