package com.whl.client.gateway.model.wallet;

import com.whl.framework.http.model.Response;

/**
 * Created by lenovo on 2017/3/20.
 */
public class GetUserHeadPicRs extends Response {
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "GetUserHeadPicRs{" +
            "imageUrl='" + imageUrl + '\'' +
            '}';
    }
}
