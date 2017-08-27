
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Request;


public class RequestChangeLoginPwdRq extends Request {

	private String msisdn;
	private String loginPasswordOriginal;
	private String loginPasswordNew;

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getLoginPasswordOriginal() {
		return loginPasswordOriginal;
	}

	public void setLoginPasswordOriginal(String loginPasswordOriginal) {
		this.loginPasswordOriginal = loginPasswordOriginal;
	}

	public String getLoginPasswordNew() {
		return loginPasswordNew;
	}

	public void setLoginPasswordNew(String loginPasswordNew) {
		this.loginPasswordNew = loginPasswordNew;
	}

	@Override
	public String toString() {
		return "RequestChangeLoginPwdRq [msisdn=" + msisdn + ", loginPasswordOriginal=" + loginPasswordOriginal
				+ ", loginPasswordNew=" + loginPasswordNew + "]";
	}

}
