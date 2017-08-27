package com.whl.client.gateway.model.wallet;

import com.whl.framework.http.model.Request;

/**
 * Created by yangwei on 2016/7/4 0004.
 */
public class RequestThirdpartyLoginRq extends Request {
    private String walletId;//000001
    private String uid;
    private String modelName;//手机型号
    private String imei;
    private String seId;
    private String imsi;
    private String osName;//ANDROID
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSeId() {
        return seId;
    }

    public void setSeId(String seId) {
        this.seId = seId;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    @Override
    public String toString() {
        return "RequestThirdpartyLoginRq{" +
            "walletId='" + walletId + '\'' +
            ", uid='" + uid + '\'' +
            ", modelName='" + modelName + '\'' +
            ", imei='" + imei + '\'' +
            ", seId='" + seId + '\'' +
            ", imsi='" + imsi + '\'' +
            ", osName='" + osName + '\'' +
            ", nickName='" + nickName + '\'' +
            '}';
    }
}

