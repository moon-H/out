package com.whl.client.gateway.model;

import java.io.Serializable;

public class CustomerInfo implements Serializable{
	private String msisdn;
	private String realName;
	private String gender;
	private String email;
	private String userIdType;
	private String userId;
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserIdType() {
		return userIdType;
	}
	public void setUserIdType(String userIdType) {
		this.userIdType = userIdType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "CustomerInfo [msisdn=" + msisdn + ", realName=" + realName
				+ ", gender=" + gender + ", email=" + email + ", userIdType="
				+ userIdType + ", userId=" + userId + "]";
	}

}
