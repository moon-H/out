package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

/**
 * Created by lenovo on 2017/4/13.
 * 获取二维码信息
 */

public class GetQrCodeSjtRs extends Response {
    private String qrCodeData;
    private String timestamp;
    private int sjtId;

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getSjtId() {
        return sjtId;
    }

    public void setSjtId(int sjtId) {
        this.sjtId = sjtId;
    }

    @Override
    public String toString() {
        return "GetQrCodeSjtRs{" + "qrCodeData='" + qrCodeData + '\'' + ", timestamp='" + timestamp + '\'' + ", sjtId=" + sjtId + '}';
    }
}
