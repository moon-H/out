
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Request;


public class RequestCommitClientExceptionRq extends Request {

	private String deviceAppId;
	private String appVersionName;
	private String modelName;
	private String imei;
	private String exceptionContent;

	public String getDeviceAppId() {
		return deviceAppId;
	}

	public void setDeviceAppId(String deviceAppId) {
		this.deviceAppId = deviceAppId;
	}

	public String getAppVersionName() {
		return appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getExceptionContent() {
		return exceptionContent;
	}

	public void setExceptionContent(String exceptionContent) {
		this.exceptionContent = exceptionContent;
	}

	@Override
	public String toString() {
		return "RequestCommitClientExceptionRq [deviceAppId=" + deviceAppId + ", appVersionName=" + appVersionName
				+ ", modelName=" + modelName + ", imei=" + imei + ", exceptionContent=" + exceptionContent + "]";
	}

}
