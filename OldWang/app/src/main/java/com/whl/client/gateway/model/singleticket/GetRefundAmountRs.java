package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

/**
 * Created by liwx on 2015/10/26.
 */
public class GetRefundAmountRs extends Response {
    private PurchaseOrder purchaseOrder;

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @Override
    public String toString() {
        return "GetRefundAmountRs{" +
            "purchaseOrder=" + purchaseOrder +
            '}';
    }
}
