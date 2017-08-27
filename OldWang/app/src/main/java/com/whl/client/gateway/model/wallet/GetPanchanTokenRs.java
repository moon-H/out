package com.whl.client.gateway.model.wallet;

import com.whl.framework.http.model.Response;

/**
 * Created by liwx on 2015/9/8.
 */
public class GetPanchanTokenRs extends Response {
    private boolean realAuth;
    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isRealAuth() {
        return realAuth;
    }

    public void setRealAuth(boolean realAuth) {
        this.realAuth = realAuth;
    }

    @Override
    public String toString() {
        return "GetPanchanTokenRs{" +
            "realAuth=" + realAuth +
            ", token='" + token + '\'' +
            '}';
    }
}
