package com.whl.client.app;


import com.whl.client.BuildConfig;
import com.whl.client.view.ptr.PtrClassicFrameLayout;

public class CoffeeConstants {
    private static final String TAG = "CoffeeConstants";
    /**
     * SERVER ADDRESS
     **********************************************************************/
    public static final String BASE_URL = BuildConfig.COFFEE_SERVER_URL;

    /***********************************************************************/
    public static final String BASE_ADDRESS = BASE_URL;
    private static CoffeeConstants mInstance;
    public static final String OS_NAME = "ANDROID";

    public static final String ADD_TASTE_AVAILABLE = "1";//可选择口味
    public static final String ADD_TASTE_UN_AVAILABLE = "0";//不可选择口味

    public static CoffeeConstants getInstance() {
        if (mInstance == null) {
            mInstance = new CoffeeConstants();
        }
        return mInstance;
    }

    public void configPullToRefresh(PtrClassicFrameLayout layout) {
        layout.setResistance(1.7f);
        layout.setRatioOfHeaderHeightToRefresh(1.2f);
        layout.setDurationToClose(200);
        layout.setDurationToCloseHeader(800);
        // default is false
        layout.setPullToRefresh(false);
        // default is true
        layout.setKeepHeaderWhenRefresh(true);
        layout.disableWhenHorizontalMove(true);
    }

    //购物车数据变化
    public static final String ACTION_SHOPPING_CART_DATA_CHANGED = "com.cssweb.com.cssweb.shankephonecoffee.ACTION_SHOPPING_CART_DATA_CHANGED";

    public static final String KEY_GOODS_TYPE = "goods_type";//商品类型 0-套餐 1-商品
    public static final String KEY_GOODS = "goods";

}
