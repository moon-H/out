package com.whl.client.gateway.model.wallet;

import com.whl.framework.http.model.Request;
import com.whl.client.gateway.model.PageInfo;

public class GetWalletFaqListRq extends Request {
	private String walletId;
	private PageInfo pageInfo;
	public String getWalletId() {
		return walletId;
	}
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}
	public PageInfo getPageInfo() {
		return pageInfo;
	}
	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
	@Override
	public String toString() {
		return "GetWalletFaqListRq [walletId=" + walletId + ", pageInfo="
				+ pageInfo + "]";
	}
	
	

}
