package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

/**
 * Created by lenovo on 2017/4/15.
 */

public class GetQrCodeSjtStatusRs extends Response {
    private String qrCodeStatus;
    private String handleDate;

    public String getQrCodeStatus() {
        return qrCodeStatus;
    }

    public void setQrCodeStatus(String qrCodeStatus) {
        this.qrCodeStatus = qrCodeStatus;
    }

    public String getHandleDate() {
        return handleDate;
    }

    public void setHandleDate(String handleDate) {
        this.handleDate = handleDate;
    }

    @Override
    public String toString() {
        return "GetQrCodeSjtStatusRs{" + "qrCodeStatus='" + qrCodeStatus + '\'' + ", handleDate='" + handleDate + '\'' + '}';
    }
}
