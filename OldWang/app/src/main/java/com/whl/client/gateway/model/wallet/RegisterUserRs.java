
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Response;


public class RegisterUserRs extends Response {

	private String mobileId;

	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}

	@Override
	public String toString() {
		return "RegisterUserRs [mobileId=" + mobileId + "]";
	}

}
