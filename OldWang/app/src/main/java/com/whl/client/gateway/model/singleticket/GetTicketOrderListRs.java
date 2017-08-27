package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;
import com.whl.client.gateway.model.PageInfo;

import java.util.List;

/**
 * Created by liwx on 2015/10/26.
 */
public class GetTicketOrderListRs extends Response {
    private List<PurchaseOrder> purchaseOrderList;
    private PageInfo pageInfo;

    public List<PurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public void setPurchaseOrderList(List<PurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }

    @Override
    public String toString() {
        return "GetTicketOrderListRs{" +
            "purchaseOrderList=" + purchaseOrderList +
            ", pageInfo=" + pageInfo +
            '}';
    }
}
