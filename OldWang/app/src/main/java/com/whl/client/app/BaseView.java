package com.whl.client.app;

import com.whl.framework.http.model.Result;

/**
 * Created by liwx on 2017/7/25.
 */
public interface BaseView<T> {

    void setPresenter(T presenter);

    void onNoNetwork();

    void onLogicFailed(Result result);//连接服务器失败，或服务器返回失败时回调

    void onAutoLoginFailed(Result result);

    void onShowProgress(String msg);

    void onDismissProgress();

}