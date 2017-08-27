
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Request;


public class CheckWalletUpdateRq extends Request {

	private String walletId;
	private String currentVersion;
	private String osName;

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	@Override
	public String toString() {
		return "CheckWalletUpdateRq [walletId=" + walletId + ", currentVersion=" + currentVersion + ", osName="
				+ osName + "]";
	}

}
