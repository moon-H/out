package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

/**
 * Created by liwx on 2015/10/22.
 */
public class GetBuySinlgeTicketMaxNumRs extends Response {
    private int buySinlgeTicketMaxNum;

    public int getBuySinlgeTicketMaxNum() {
        return buySinlgeTicketMaxNum;
    }

    public void setBuySinlgeTicketMaxNum(int buySinlgeTicketMaxNum) {
        this.buySinlgeTicketMaxNum = buySinlgeTicketMaxNum;
    }

    @Override
    public String toString() {
        return "GetBuySinlgeTicketMaxNumRs{" +
            "buySinlgeTicketMaxNum=" + buySinlgeTicketMaxNum +
            '}';
    }
}
