package com.whl.client.app;

import com.whl.client.gateway.model.singleticket.StationCode;
import com.whl.client.home.pickimage.ImageItem;

import java.util.List;

/**
 * Created by liwx on 2017/2/9.
 * EventBus 回调事件类
 */

public class IEvent {
    /**
     * 选择站点事件
     */
    public static class SelectStationEvent {
        private StationCode station;
        private int type;

        public SelectStationEvent(int type, StationCode station) {
            this.type = type;
            this.station = station;
        }

        public StationCode getStation() {
            return station;
        }

        public int getType() {
            return type;
        }

        @Override
        public String toString() {
            return "SelectStationEvent{" + "station=" + station + ", type=" + type + '}';
        }
    }

    /**
     * 订单fragment传递状态事件
     */
    public static class OrderFragmentEvent {
        private String statusName;
        private int statusCode;

        public OrderFragmentEvent(String statusName, int statusCode) {
            this.statusName = statusName;
            this.statusCode = statusCode;
        }

        public String getStatusName() {
            return statusName;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }

    public static class PickPreviewImageEvent {
        List<ImageItem> mImageItemList;

        public PickPreviewImageEvent(List<ImageItem> list) {
            mImageItemList = list;
        }

        public List<ImageItem> getImageItemList() {
            return mImageItemList;
        }
    }

    public static class PickImageEventRs {
        List<ImageItem> mImageItemList;

        public PickImageEventRs(List<ImageItem> list) {
            mImageItemList = list;
        }

        public List<ImageItem> getImageItemList() {
            return mImageItemList;
        }
    }

    /**
     * 选择用户头像事件
     */
    public static class PickUserHeadEvent {
        ImageItem mImageItem;

        public PickUserHeadEvent(ImageItem item) {
            mImageItem = item;
        }

        public ImageItem getImageItem() {
            return mImageItem;
        }
    }

    /**
     * 勾选用户条款
     */
    public static class AgreeClause {
        boolean isAgree;

        public AgreeClause(boolean isAgree) {
            this.isAgree = isAgree;
        }

        public boolean isAgree() {
            return isAgree;
        }

        public void setAgree(boolean agree) {
            isAgree = agree;
        }
    }
}
