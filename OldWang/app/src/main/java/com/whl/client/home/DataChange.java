package com.whl.client.home;

import java.util.Observable;

/**
 * Created by liwx on 2016/9/29.
 */
public class DataChange extends Observable {
    public static DataChange sInstance;

    public static DataChange getInstance() {
        if (sInstance == null)
            sInstance = new DataChange();
        return sInstance;
    }

    public void notifyDataChange(Object data) {
        //被观察者怎么通知观察者数据有改变了呢？？这里的两个方法是关键。
        setChanged();
        notifyObservers(data);
    }
}
