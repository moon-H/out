package com.whl.client.login;

import com.whl.framework.http.model.Result;

/**
 * Created by liwx on 2016/4/19.
 */
public interface HandleLoginListener {
    /**
     * 登录逻辑执行成功，包括同步服务、删除服务等
     */
    void onLoginLogicSuccess();

    /**
     * 登录逻辑执行失败，包括同步服务、删除服务等
     */
    void onLoginLogicFailed(Result result);

    /**
     * 登录成功
     */
//    void onLoginStateNormal();

//    void onThirdLoginStateNormal();
//
//    /**
//     * 登录失败
//     */
//    void onLoginStateError(Result result);
}
