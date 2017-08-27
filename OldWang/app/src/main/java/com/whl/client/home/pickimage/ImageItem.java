package com.whl.client.home.pickimage;

import java.io.Serializable;

/**
 * Created by liwx on 2017/3/2.
 */
public class ImageItem implements Serializable {
    private String imageId;//id
    private String thumbnailPath;//缩略图
    private String imagePath;//大图

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "ImageItem{" +
            "imageId='" + imageId + '\'' +
            ", thumbnailPath='" + thumbnailPath + '\'' +
            ", imagePath='" + imagePath + '\'' +
            '}';
    }
}
