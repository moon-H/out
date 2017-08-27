package com.whl.client.gateway.model.inbox;

import com.whl.framework.http.model.Request;
import com.whl.client.gateway.model.PageInfo;

/**
 * Created by Administrator on 2017/6/19 0019.
 */
public class GetMessageListRq extends Request {
    private PageInfo pageInfo;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "GetMessageListRq{" +
                "pageInfo=" + pageInfo +
                '}';
    }
}
