
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Response;


public class CheckWalletUpdateRs extends Response {

	private String existsUpdateVersion;
	private String mandatoryYn;
	private String updateVersion;
	private String downloadUrl;
	private String appversionDesc;

	public String getAppversionDesc() {
		return appversionDesc;
	}

	public void setAppversionDesc(String appversionDesc) {
		this.appversionDesc = appversionDesc;
	}

	public String getExistsUpdateVersion() {
		return existsUpdateVersion;
	}

	public void setExistsUpdateVersion(String existsUpdateVersion) {
		this.existsUpdateVersion = existsUpdateVersion;
	}

	public String getMandatoryYn() {
		return mandatoryYn;
	}

	public void setMandatoryYn(String mandatoryYn) {
		this.mandatoryYn = mandatoryYn;
	}

	public String getUpdateVersion() {
		return updateVersion;
	}

	public void setUpdateVersion(String updateVersion) {
		this.updateVersion = updateVersion;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	@Override
	public String toString() {
		return "CheckWalletUpdateRs [existsUpdateVersion=" + existsUpdateVersion + ", mandatoryYn=" + mandatoryYn
				+ ", updateVersion=" + updateVersion + ", downloadUrl=" + downloadUrl + "]";
	}

}
