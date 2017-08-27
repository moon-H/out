package com.whl.client.gateway.model.singleticket;

import java.io.Serializable;

/**
 * Created by liwx on 2015/10/22.
 */
public class SingelTicketFixedPricePool implements Serializable {
    private int singleTicketFixedPrice;

    public int getSingleTicketFixedPrice() {
        return singleTicketFixedPrice;
    }

    public void setSingleTicketFixedPrice(int singleTicketFixedPrice) {
        this.singleTicketFixedPrice = singleTicketFixedPrice;
    }

    @Override
    public String toString() {
        return "SingelTicketFixedPricePool{" +
            "singleTicketFixedPrice=" + singleTicketFixedPrice +
            '}';
    }
}
