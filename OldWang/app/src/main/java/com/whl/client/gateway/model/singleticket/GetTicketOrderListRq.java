package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Request;
import com.whl.client.gateway.model.PageInfo;

/**
 * Created by liwx on 2015/10/26.
 */
public class GetTicketOrderListRq extends Request {
    private int orderStatus;
    private PageInfo pageInfo;
    private String cityCode;
    private String categoryCode;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @Override
    public String toString() {
        return "GetTicketOrderListRq{" +
                "orderStatus=" + orderStatus +
                ", pageInfo=" + pageInfo +
                ", cityCode='" + cityCode + '\'' +
                ", categoryCode='" + categoryCode + '\'' +
                '}';
    }
}
