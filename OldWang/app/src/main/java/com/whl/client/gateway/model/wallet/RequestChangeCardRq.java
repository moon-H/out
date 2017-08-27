
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Request;


public class RequestChangeCardRq extends Request {

	private String msisdn;
	private String loginPassword;
	private String seId;
	private String imsi;

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getSeId() {
		return seId;
	}

	public void setSeId(String seId) {
		this.seId = seId;
	}

	@Override
	public String toString() {
		return "RequestChangeCardRq [msisdn=" + msisdn + ", loginPassword=" + loginPassword + ", seId=" + seId
				+ ", imsi=" + imsi + "]";
	}

}
