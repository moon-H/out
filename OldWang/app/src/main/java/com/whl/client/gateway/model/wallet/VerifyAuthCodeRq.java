
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Request;


public class VerifyAuthCodeRq extends Request {

	private String msisdn;
	private String authCode;
	private String seId;

	
	public String getSeId() {
		return seId;
	}

	
	public void setSeId(String seId) {
		this.seId = seId;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	@Override
	public String toString() {
		return "VerifyAuthCodeRq [msisdn=" + msisdn + ", authCode=" + authCode + ", seId=" + seId + "]";
	}

}
