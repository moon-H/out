package com.whl.client.gateway.model.singleticket;

import com.whl.framework.http.model.Response;

/**
 * Created by lenovo on 2016/10/28.
 */
public class CheckStationCodeVersionRs extends Response {
    private String existsUpdateStationVersion;
    private String existsUpdateLineVersion;

    public String getExistsUpdateStationVersion() {
        return existsUpdateStationVersion;
    }

    public void setExistsUpdateStationVersion(String existsUpdateStationVersion) {
        this.existsUpdateStationVersion = existsUpdateStationVersion;
    }

    public String getExistsUpdateLineVersion() {
        return existsUpdateLineVersion;
    }

    public void setExistsUpdateLineVersion(String existsUpdateLineVersion) {
        this.existsUpdateLineVersion = existsUpdateLineVersion;
    }

    @Override
    public String toString() {
        return "CheckStationCodeVersionRs{" +
            "existsUpdateStationVersion='" + existsUpdateStationVersion + '\'' +
            ", existsUpdateLineVersion='" + existsUpdateLineVersion + '\'' +
            '}';
    }
}
