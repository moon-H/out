package com.whl.client.gateway.model.wallet;

import com.whl.framework.http.model.Response;

/**
 * Created by Administrator on 2016/7/7 0007.
 */
public class RegistertPanchanUserRs extends Response {
    private String realAuth;
    private String token;
    private String mno;

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    public String getRealAuth() {
        return realAuth;
    }

    public void setRealAuth(String realAuth) {
        this.realAuth = realAuth;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "RegistertPanchanUserRs{" +
            "realAuth='" + realAuth + '\'' +
            ", token='" + token + '\'' +
            ", mno='" + mno + '\'' +
            '}';
    }
}
