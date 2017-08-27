package com.whl.client.gateway;

import android.content.Context;

import com.whl.framework.http.ResponseMappingHandler;
import com.whl.framework.http.model.Result;
import com.whl.client.app.BizApplication;
import com.whl.client.gateway.model.GetAfficheListRq;
import com.whl.client.gateway.model.GetAfficheListRs;
import com.whl.client.gateway.model.GetEventListRq;
import com.whl.client.gateway.model.GetEventListRs;
import com.whl.client.gateway.model.event.GetPicListRq;
import com.whl.client.gateway.model.event.GetPicListRs;

import org.apache.http.Header;

/**
 * Created by liwx on 2015/9/15.
 */
public class EventGateway extends MobileGateway {
    public EventGateway(Context context) {
        super(context);
    }

    public void getEventList(final MobileGatewayListener<GetEventListRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/event/getEventList";
        GetEventListRq request = new GetEventListRq();
        request.setWalletId(BizApplication.WALLET_ID);
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetEventListRs>(GetEventListRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                onFailed(statusCode, headers, listener);
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, GetEventListRs response) {
                if (listener != null) {
                    if (response != null && response.getResult().getCode() == Result.OK) {
                        listener.onSuccess(response);
                    } else {
                        listener.onFailed(response.getResult());
                    }
                }
            }

            @Override
            public void handleNoNetwork() {
                onNoNetwork(listener);
            }
        });
    }

    /**
     * 获取公告列表
     */
    public void getAfficheList(String cityCode, final MobileGatewayListener<GetAfficheListRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/event/getAfficheList";
        GetAfficheListRq request = new GetAfficheListRq();
        request.setCityCode(cityCode);
        request.setWalletId(BizApplication.WALLET_ID);
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetAfficheListRs>(GetAfficheListRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                onFailed(statusCode, headers, listener);
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, GetAfficheListRs response) {
                if (listener != null) {
                    if (response != null) {
                        if (response.getResult().getCode() == Result.OK) {
                            listener.onSuccess(response);
                        } else {
                            listener.onFailed(response.getResult());
                        }
                    } else {
                        listener.onFailed(null);
                    }
                }
            }

            @Override
            public void handleNoNetwork() {
                onNoNetwork(listener);
            }
        });
    }

    /**
     * 获取活动列表
     */
    public void getPicList(int flag, final MobileGatewayListener<GetPicListRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/event/getPicList";
        GetPicListRq request = new GetPicListRq();
        request.setFlag(flag);
        request.setWalletId(BizApplication.WALLET_ID);
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetPicListRs>(GetPicListRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                onFailed(statusCode, headers, listener);
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, GetPicListRs response) {
                if (listener != null) {
                    if (response != null) {
                        if (response.getResult().getCode() == Result.OK) {
                            listener.onSuccess(response);
                        } else {
                            listener.onFailed(response.getResult());
                        }
                    } else {
                        listener.onFailed(null);
                    }
                }
            }

            @Override
            public void handleNoNetwork() {
                onNoNetwork(listener);
            }
        });
    }
}
