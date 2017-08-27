package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

import java.util.List;

/**
 * Created by liwx on 2015/10/22.
 */
public class GetLineCodeListRs extends Response {
    private List<LineCode> lineCodeList;
    private int updateLineVersion;

    public List<LineCode> getLineCodeList() {
        return lineCodeList;
    }

    public void setLineCodeList(List<LineCode> lineCodeList) {
        this.lineCodeList = lineCodeList;
    }

    public int getUpdateLineVersion() {
        return updateLineVersion;
    }

    public void setUpdateLineVersion(int updateLineVersion) {
        this.updateLineVersion = updateLineVersion;
    }

    @Override
    public String toString() {
        return "GetLineCodeListRs{" +
            "lineCodeList=" + lineCodeList +
            ", updateLineVersion=" + updateLineVersion +
            '}';
    }
}
