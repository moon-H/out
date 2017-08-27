package com.whl.client.gateway.model.wallet;

import com.whl.framework.http.model.Response;

/**
 * Created by lenovo on 2017/3/19.
 */
public class UploadUserHeadPicRs extends Response {
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "UploadUserHeadPicRs{" +
            "imageUrl='" + imageUrl + '\'' +
            '}';
    }
}
