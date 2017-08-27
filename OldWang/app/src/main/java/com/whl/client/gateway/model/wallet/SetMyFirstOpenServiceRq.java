
package com.whl.client.gateway.model.wallet;


import com.whl.framework.http.model.Request;


public class SetMyFirstOpenServiceRq extends Request {

	private String serviceId;
	private String firstOpenYn;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	
	public String getFirstOpenYn() {
		return firstOpenYn;
	}

	
	public void setFirstOpenYn(String firstOpenYn) {
		this.firstOpenYn = firstOpenYn;
	}

	@Override
	public String toString() {
		return "SetMyFirstOpenServiceRq [serviceId=" + serviceId + ", firstOpenYn=" + firstOpenYn + "]";
	}
	


}
