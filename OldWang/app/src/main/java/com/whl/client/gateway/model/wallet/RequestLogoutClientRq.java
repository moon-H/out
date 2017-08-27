
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Request;


public class RequestLogoutClientRq extends Request {

	private String msisdn;

	
	public String getMsisdn() {
		return msisdn;
	}

	
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}


	@Override
	public String toString() {
		return "RequestLogoutClientRq [msisdn=" + msisdn + "]";
	}

	
}
