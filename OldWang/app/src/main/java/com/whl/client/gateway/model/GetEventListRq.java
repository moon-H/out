package com.whl.client.gateway.model;

import com.whl.framework.http.model.Request;

/**
 * Created by liwx on 2015/9/15.
 */
public class GetEventListRq extends Request {
    private String walletId;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    @Override
    public String toString() {
        return "GetEventListRq{" +
            "walletId='" + walletId + '\'' +
            '}';
    }
}
