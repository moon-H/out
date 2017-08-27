package com.whl.client.gateway.model.wallet;

import com.whl.framework.http.model.Request;

/**
 * Created by liwx on 2016/7/7.
 */
public class BindingPushClientIdRq extends Request {
    private String clientId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "BindingPushClientIdRq{" +
            "clientId='" + clientId + '\'' +
            '}';
    }
}
