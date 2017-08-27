package com.whl.framework.http;


import android.text.TextUtils;

import com.whl.framework.app.MApplication;
import com.whl.framework.http.model.Request;
import com.whl.framework.http.model.Response;
import com.whl.framework.preference.MSharedPreference;
import com.whl.framework.utils.MLog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;


public class BaseGateway {

    private static final String TAG = "BaseGateway";
    private AsyncHttpClient mClient;
    private Gson mGson;
    private MApplication mApp;

    public BaseGateway(MApplication app, String url) {
        if (url.trim().startsWith("https")) {
            mClient = new AsyncHttpClient(true, 80, 443);
        } else {
            mClient = new AsyncHttpClient();
        }
        mApp = app;
        mClient.setTimeout(15000);
        mClient.setConnectTimeout(15000);
        mClient.setResponseTimeout(30000);
        mGson = new Gson();
    }

    public void addHeaders() {
        String headerStr = MSharedPreference.getCookie(mApp);
        if (!TextUtils.isEmpty(headerStr)) {
            mClient.addHeader("Cookie", headerStr.trim());
        }
    }

    public void cancelRequest() {
        // mClient.cancelRequests(context, mayInterruptIfRunning);
        mClient.cancelAllRequests(true);
    }

    public void setMaxRetriesAndTimeout(int retries, int timeout) {
        mClient.setMaxRetriesAndTimeout(retries, timeout);
    }

    ;

    //    public void setTimeout(int timeout) {
    //        mClient.setTimeout(timeout);
    //    }

    private StringEntity converRequest(Request request) throws UnsupportedEncodingException {
        JsonElement localJsonElement = this.mGson.toJsonTree(request);
        JsonObject requstJsonObject = new JsonObject();
        requstJsonObject.add(request.getClass().getSimpleName(), localJsonElement);
        MLog.d(TAG, "sendRequest::request :: " + requstJsonObject.toString());
        return new StringEntity(requstJsonObject.toString(), "UTF-8");
    }

    public void sendRequset(MApplication context, final String url, Request request, final ResponseMappingHandler<? extends Response> resHandler) {
        if (!context.getNetworkManager().isNetworkAvailable()) {
            MLog.e(TAG, "sendRequset:: network state is not available url = " + url);
            resHandler.handleNoNetwork();
            return;
        }
        StringEntity strEntity;
        try {
            MLog.d(TAG, "sendRequest:: URL :: " + url);
            strEntity = converRequest(request);
            addHeaders();
            mClient.post(context, url, strEntity, "application/json; charset=UTF-8", new AsyncHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    MLog.e(TAG, "sendRequest::onFailuer : url = " + url + "  HttpCode :: " + statusCode);
                    if (responseBody != null) {
                        MLog.e(TAG, "sendRequest::onFailuer responseBody :: " + new String(responseBody));
                    }
                    if (error != null) {
                        MLog.e(TAG, "sendRequest::onFailuer error :: " + error.getMessage());
                    }
                    if (resHandler != null)
                        resHandler.onFailure(statusCode, headers, responseBody, error, url);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    MLog.d(TAG, "sendRequest::onSuccess:: HttpCode :: " + statusCode);
                    if (responseBody != null) {
                        MLog.d(TAG, "sendRequest::onSuccess:: Response :: " + new String(responseBody));
                    }
                    if (resHandler != null)
                        if (responseBody != null) {
                            resHandler.onSuccess(statusCode, headers, new String(responseBody));
                        }
                }
            });
        } catch (UnsupportedEncodingException error) {
            error.printStackTrace();
            resHandler.onFailure(9876, null, null, error, url);
        }
    }

    public void uploadPhoto(MApplication context, final String url, RequestParams params, final ResponseMappingHandler<? extends Response> resHandler) {
        if (!context.getNetworkManager().isNetworkAvailable()) {
            MLog.e(TAG, "sendRequset:: network state is not available url = " + url);
            resHandler.handleNoNetwork();
            return;
        }
        try {
            MLog.d(TAG, "sendRequest:: URL :: " + url);
            MLog.d(TAG, "sendRequest:: params :: " + params.toString());
            //            strEntity = converRequest(request);
            addHeaders();
            mClient.post(url, params, new AsyncHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    MLog.e(TAG, "sendRequest::onFailuer : url = " + url + "  HttpCode :: " + statusCode);
                    if (responseBody != null) {
                        MLog.e(TAG, "sendRequest::onFailuer responseBody :: " + new String(responseBody));
                    }
                    if (error != null) {
                        MLog.e(TAG, "sendRequest::onFailuer error :: " + error.getMessage());
                    }
                    if (resHandler != null)
                        resHandler.onFailure(statusCode, headers, responseBody, error, url);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    MLog.d(TAG, "sendRequest::onSuccess:: HttpCode :: " + statusCode);
                    if (responseBody != null) {
                        MLog.d(TAG, "sendRequest::onSuccess:: Response :: " + new String(responseBody));
                    }
                    if (resHandler != null)
                        resHandler.onSuccess(statusCode, headers, new String(responseBody));
                }
            });
        } catch (Exception error) {
            error.printStackTrace();
            resHandler.onFailure(9876, null, null, error, url);
        }
    }

}
