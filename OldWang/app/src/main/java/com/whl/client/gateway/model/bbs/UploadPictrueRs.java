package com.whl.client.gateway.model.bbs;

import com.whl.framework.http.model.Response;

/**
 * Created by lenovo on 2017/3/6.
 */
public class UploadPictrueRs extends Response {
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        this.imageUrl3 = imageUrl3;
    }

    @Override
    public String toString() {
        return "UploadPictrueRs{" +
            "imageUrl1='" + imageUrl1 + '\'' +
            ", imageUrl2='" + imageUrl2 + '\'' +
            ", imageUrl3='" + imageUrl3 + '\'' +
            '}';
    }
}
