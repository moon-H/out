package com.whl.client.gateway.model.wallet;

import com.whl.framework.http.model.Response;
import com.whl.client.gateway.model.PageInfo;

import java.util.List;

public class GetWalletFaqListRs extends Response {
	private List<WalletFaq> walletFaqList;
	private PageInfo pageInfo;
	public List<WalletFaq> getWalletFaqList() {
		return walletFaqList;
	}
	public void setWalletFaqList(List<WalletFaq> walletFaqList) {
		this.walletFaqList = walletFaqList;
	}
	public PageInfo getPageInfo() {
		return pageInfo;
	}
	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
	@Override
	public String toString() {
		return "GetWalletFaqListRs [walletFaqList=" + walletFaqList
				+ ", pageInfo=" + pageInfo + "]";
	}
	

}
