
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Request;


public class ResetUserLoginPwdRq extends Request {

	private String msisdn;
	private String seId;

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getSeId() {
		return seId;
	}

	public void setSeId(String seId) {
		this.seId = seId;
	}

	@Override
	public String toString() {
		return "ResetUserLoginPwdRq [msisdn=" + msisdn + ", seId=" + seId + "]";
	}

}
