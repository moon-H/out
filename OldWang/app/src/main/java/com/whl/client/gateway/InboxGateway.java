package com.whl.client.gateway;

import android.content.Context;

import com.whl.framework.http.ResponseMappingHandler;
import com.whl.framework.http.model.Result;
import com.whl.client.app.BizApplication;
import com.whl.client.gateway.model.PageInfo;
import com.whl.client.gateway.model.inbox.DeleteInboxMessageRq;
import com.whl.client.gateway.model.inbox.DeleteInboxMessageRs;
import com.whl.client.gateway.model.inbox.GetMessageListRq;
import com.whl.client.gateway.model.inbox.GetMessageListRs;
import com.whl.client.gateway.model.inbox.GetMessageUnreadCntRq;
import com.whl.client.gateway.model.inbox.GetMessageUnreadCntRs;
import com.whl.client.gateway.model.inbox.UpdateMessageStatusRq;
import com.whl.client.gateway.model.inbox.UpdateMessageStatusRs;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by liwx on 2015/9/8.
 */
public class InboxGateway extends MobileGateway {
    public InboxGateway(Context context) {
        super(context);
    }

    public void deleteInboxMessage(List<String> messageIdList, final MobileGatewayListener<DeleteInboxMessageRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/inbox/deleteInboxMessage";
        DeleteInboxMessageRq request = new DeleteInboxMessageRq();
        request.setMessageIdList(messageIdList);
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<DeleteInboxMessageRs>(DeleteInboxMessageRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                onFailed(statusCode, headers, listener);
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, DeleteInboxMessageRs response) {
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
     * 获取信息列表
     */
    //    public void getMessageList(int pageNumber, int pageSize, String lastMessageId, String messageType, String messageCategory, String serviceId, final MobileGatewayListener<GetInboxMessageListRs> listener) {
    //        String url = BizApplication.BASE_ADDRESS + "/inbox/getMessageList";
    //        PageInfo pageInfo = new PageInfo();
    //        pageInfo.setPageNumber(pageNumber);
    //        pageInfo.setPageSize(pageSize);
    //        GetInboxMessageListRq request = new GetInboxMessageListRq();
    //        request.setLastMessageId(lastMessageId);
    //        request.setMessageCategory(messageCategory);
    //        request.setMessageType(messageType);
    //        request.setPageInfo(pageInfo);
    //        request.setServiceId(serviceId);
    //        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetInboxMessageListRs>(GetInboxMessageListRs.class) {
    //
    //            @Override
    //            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
    //                onFailed(statusCode, headers, listener);
    //            }
    //
    //            @Override
    //            public void handleSuccess(int statusCode, Header[] headers, GetInboxMessageListRs response) {
    //                if (listener != null) {
    //                    if (response != null && response.getResult().getCode() == Result.OK) {
    //                        listener.onSuccess(response);
    //                    } else {
    //                        listener.onFailed(response.getResult());
    //                    }
    //                }
    //            }
    //
    //            @Override
    //            public void handleNoNetwork() {
    //                onNoNetwork(listener);
    //            }
    //        });
    //    }

    /**
     * 获取新消息列表
     */
    public void getMessageList(int pageNumber, int pageSize, final MobileGatewayListener<GetMessageListRs> listener) {
        String url = BizApplication.BASE_ADDRESS + "/inbox/getMessageList";
        //        String url = "http://192.168.2.135:8180/corpay/ci/inbox/getMessageList";
        //http://192.168.2.135:8180/corpay/ci/inbox/getMessageList
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNumber(pageNumber);
        pageInfo.setPageSize(pageSize);
        GetMessageListRq request = new GetMessageListRq();
        request.setPageInfo(pageInfo);
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetMessageListRs>(GetMessageListRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                onFailed(statusCode, headers, listener);
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, GetMessageListRs response) {
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
    //
    //    /**
    //     * 获取未读收件箱
    //     */
    //    public void getMessageUnreadCnt(final MobileGatewayListener<GetMessageUnreadCntRs> listener) {
    //        String url = BizApplication.BASE_ADDRESS + "/inbox/getMessageUnreadCnt";
    //        GetMessageUnreadCntRq request = new GetMessageUnreadCntRq();
    //        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetMessageUnreadCntRs>(GetMessageUnreadCntRs.class) {
    //
    //            @Override
    //            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
    //                onFailed(statusCode, headers, listener);
    //            }
    //
    //            @Override
    //            public void handleSuccess(int statusCode, Header[] headers, GetMessageUnreadCntRs response) {
    //                if (listener != null) {
    //                    if (response != null && response.getResult().getCode() == Result.OK) {
    //                        listener.onSuccess(response);
    //                    } else {
    //                        listener.onFailed(response.getResult());
    //                    }
    //                }
    //            }
    //
    //            @Override
    //            public void handleNoNetwork() {
    //                onNoNetwork(listener);
    //            }
    //        });
    //    }

    /**
     * 新增 修改--- 获取未读信息
     */
    public void getMessageUnreadCnt(final MobileGatewayListener<GetMessageUnreadCntRs> listener) {
        //        String url = "http://192.168.2.135:8180/corpay/ci/inbox/getMessageUnreadCnt";
        String url = BizApplication.BASE_ADDRESS + "/inbox/getMessageUnreadCnt";
        GetMessageUnreadCntRq request = new GetMessageUnreadCntRq();
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetMessageUnreadCntRs>(GetMessageUnreadCntRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                onFailed(statusCode, headers, listener);
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, GetMessageUnreadCntRs response) {
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
     * 更新消息状态
     */
    public void updateMessageStatus(List<String> messageIdList, final MobileGatewayListener<UpdateMessageStatusRs> listener) {
        //        String url = "http://192.168.2.135:8180/corpay/ci/inbox/updateMessageStatus";
        String url = BizApplication.BASE_ADDRESS + "/inbox/updateMessageStatus";
        UpdateMessageStatusRq request = new UpdateMessageStatusRq();
        request.setMessageIdList(messageIdList);
        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<UpdateMessageStatusRs>(UpdateMessageStatusRs.class) {

            @Override
            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
                onFailed(statusCode, headers, listener);
            }

            @Override
            public void handleSuccess(int statusCode, Header[] headers, UpdateMessageStatusRs response) {
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
}
