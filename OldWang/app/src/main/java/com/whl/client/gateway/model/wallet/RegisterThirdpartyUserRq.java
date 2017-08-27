package com.whl.client.gateway.model.wallet;

import com.whl.framework.http.model.Request;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class RegisterThirdpartyUserRq extends Request {
    private String walletId;//000001
    private String uid;
    private String uidType;
    private String email;
    private String seId;
    private String imsi;
    private String modelName;//手机型号
    private String imei;
    private String mno;
    private String osName;//ANDROID
    private String udid;
    private String nickname;//昵称
    private String gender;//性别
    private String iconUrl;
    private String channelCode;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
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

    public String getUidType() {
        return uidType;
    }

    public void setUidType(String uidType) {
        this.uidType = uidType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public String toString() {
        return "RegisterThirdpartyUserRq{" +
            "walletId='" + walletId + '\'' +
            ", uid='" + uid + '\'' +
            ", uidType='" + uidType + '\'' +
            ", email='" + email + '\'' +
            ", seId='" + seId + '\'' +
            ", imsi='" + imsi + '\'' +
            ", modelName='" + modelName + '\'' +
            ", imei='" + imei + '\'' +
            ", mno='" + mno + '\'' +
            ", osName='" + osName + '\'' +
            ", udid='" + udid + '\'' +
            ", nickname='" + nickname + '\'' +
            ", gender='" + gender + '\'' +
            ", iconUrl='" + iconUrl + '\'' +
            ", channelCode='" + channelCode + '\'' +
            '}';
    }
}
