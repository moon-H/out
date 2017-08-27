
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Request;


public class SendAuthCodeBySmsRq extends Request {

	private String msisdn;
	private String walletId;
	private String seId;

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

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

	@Override
	public String toString() {
		return "SendAuthCodeBySmsRq [msisdn=" + msisdn + ", walletId=" + walletId + ", seId=" + seId + "]";
	}

}
