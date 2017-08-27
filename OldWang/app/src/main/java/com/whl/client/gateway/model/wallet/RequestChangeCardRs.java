
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Response;


public class RequestChangeCardRs extends Response {

	@Override
	public String toString() {
		return "RequestChangeCardRs [mobileId=" + mobileId + "]";
	}

	private String mobileId;

	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}

}
