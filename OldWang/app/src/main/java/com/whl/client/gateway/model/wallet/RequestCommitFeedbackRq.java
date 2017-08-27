
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Request;


public class RequestCommitFeedbackRq extends Request {

	private String appUserName;
	private String deviceAppId;
	private String appVersionName;
	private String modelName;
	private String imei;
	private String feedbackContent;

	public String getAppUserName() {
		return appUserName;
	}

	public void setAppUserName(String appUserName) {
		this.appUserName = appUserName;
	}

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

	public String getFeedbackContent() {
		return feedbackContent;
	}

	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	@Override
	public String toString() {
		return "RequestCommitFeedbackRq [appUserName=" + appUserName + ", deviceAppId=" + deviceAppId
				+ ", appVersionName=" + appVersionName + ", modelName=" + modelName + ", imei=" + imei
				+ ", feedbackContent=" + feedbackContent + "]";
	}

}
