
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Response;


public class VerifyWalletLoginRs extends Response {

	private String mobileClientState;

	public String getMobileClientState() {
		return mobileClientState;
	}

	public void setMobileClientState(String mobileClientState) {
		this.mobileClientState = mobileClientState;
	}

	@Override
	public String toString() {
		return "VerifyWalletLoginRs [mobileClientState=" + mobileClientState + "]";
	}

}
