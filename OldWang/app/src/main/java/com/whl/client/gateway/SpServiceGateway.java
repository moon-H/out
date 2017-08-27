//package com.cssweb.shankephone.gateway;
//
//import android.content.Context;
//
//import com.cssweb.framework.http.ResponseMappingHandler;
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.DeviceInfo;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.gateway.model.PageInfo;
//import com.cssweb.shankephone.gateway.model.hceservice.CheckHceDeviceChangedRq;
//import com.cssweb.shankephone.gateway.model.hceservice.CheckHceDeviceChangedRs;
//import com.cssweb.shankephone.gateway.model.hceservice.CheckPaymentAccountBindingRq;
//import com.cssweb.shankephone.gateway.model.hceservice.CheckPaymentAccountBindingRs;
//import com.cssweb.shankephone.gateway.model.hceservice.GetMyHceSjtServiceRq;
//import com.cssweb.shankephone.gateway.model.hceservice.GetMyHceSjtServiceRs;
//import com.cssweb.shankephone.gateway.model.hceservice.RequestHceSjtRq;
//import com.cssweb.shankephone.gateway.model.hceservice.RequestHceSjtRs;
//import com.cssweb.shankephone.gateway.model.hceservice.UpdateMyLastHecSjtServiceRq;
//import com.cssweb.shankephone.gateway.model.hceservice.UpdateMyLastHecSjtServiceRs;
//import com.cssweb.shankephone.gateway.model.myorder.GetProductCategoryRq;
//import com.cssweb.shankephone.gateway.model.myorder.GetProductCategoryRs;
//import com.cssweb.shankephone.gateway.model.order.GetDefaultProductCategoryCodeRq;
//import com.cssweb.shankephone.gateway.model.order.GetDefaultProductCategoryCodeRs;
//import com.cssweb.shankephone.gateway.model.order.GetNfcOrderDetailRq;
//import com.cssweb.shankephone.gateway.model.order.GetNfcOrderDetailRs;
//import com.cssweb.shankephone.gateway.model.spservice.AddMyServiceRq;
//import com.cssweb.shankephone.gateway.model.spservice.AddMyServiceRs;
//import com.cssweb.shankephone.gateway.model.spservice.CheckEligibilityRq;
//import com.cssweb.shankephone.gateway.model.spservice.CheckEligibilityRs;
//import com.cssweb.shankephone.gateway.model.spservice.Command;
//import com.cssweb.shankephone.gateway.model.spservice.DeleteCustomerOrderRq;
//import com.cssweb.shankephone.gateway.model.spservice.DeleteCustomerOrderRs;
//import com.cssweb.shankephone.gateway.model.spservice.DeleteServiceAuthRq;
//import com.cssweb.shankephone.gateway.model.spservice.DeleteServiceAuthRs;
//import com.cssweb.shankephone.gateway.model.spservice.GetServiceListForNoNFCRq;
//import com.cssweb.shankephone.gateway.model.spservice.GetServiceListForNoNFCRs;
//import com.cssweb.shankephone.gateway.model.spservice.GetTabServiceListRq;
//import com.cssweb.shankephone.gateway.model.spservice.GetTabServiceListRs;
//import com.cssweb.shankephone.gateway.model.spservice.GetTopupRecordListRq;
//import com.cssweb.shankephone.gateway.model.spservice.GetTopupRecordListRs;
//import com.cssweb.shankephone.gateway.model.spservice.NotiRapduRq;
//import com.cssweb.shankephone.gateway.model.spservice.NotiRapduRs;
//import com.cssweb.shankephone.gateway.model.spservice.NotiTopupReslutRq;
//import com.cssweb.shankephone.gateway.model.spservice.NotiTopupReslutRs;
//import com.cssweb.shankephone.gateway.model.spservice.RequestAppMagrRq;
//import com.cssweb.shankephone.gateway.model.spservice.RequestAppMagrRs;
//import com.cssweb.shankephone.gateway.model.spservice.RequestRefundTopupRq;
//import com.cssweb.shankephone.gateway.model.spservice.RequestRefundTopupRs;
//import com.cssweb.shankephone.gateway.model.spservice.RequestSynTopupReslutRq;
//import com.cssweb.shankephone.gateway.model.spservice.RequestSynTopupReslutRs;
//import com.cssweb.shankephone.gateway.model.spservice.RequestTopupRq;
//import com.cssweb.shankephone.gateway.model.spservice.RequestTopupRs;
//import com.cssweb.shankephone.gateway.model.spservice.SearchServiceListRq;
//import com.cssweb.shankephone.gateway.model.spservice.SearchServiceListRs;
//import com.cssweb.shankephone.gateway.model.spservice.SendRAPDURq;
//import com.cssweb.shankephone.gateway.model.spservice.SendRAPDURs;
//import com.cssweb.shankephone.gateway.model.spservice.TriggerDeployServiceRq;
//import com.cssweb.shankephone.gateway.model.spservice.TriggerDeployServiceRs;
//import com.cssweb.shankephone.gateway.model.spservice.TriggerRemoveServiceRq;
//import com.cssweb.shankephone.gateway.model.spservice.TriggerRemoveServiceRs;
//import com.cssweb.shankephone.gateway.model.spservice.UpdateMyServiceRq;
//import com.cssweb.shankephone.gateway.model.spservice.UpdateMyServiceRs;
//import com.cssweb.shankephone.gateway.model.spservice.UpdateServiceStatusRq;
//import com.cssweb.shankephone.gateway.model.spservice.UpdateServiceStatusRs;
//import com.cssweb.shankephone.gateway.model.spservice.ViewServiceInfoRq;
//import com.cssweb.shankephone.gateway.model.spservice.ViewServiceInfoRs;
//import com.cssweb.shankephone.gateway.model.wallet.RequestChangeCardRq;
//import com.cssweb.shankephone.gateway.model.wallet.RequestChangeCardRs;
//import com.cssweb.shankephone.gateway.model.wallet.SetMyFirstOpenServiceRq;
//import com.cssweb.shankephone.gateway.model.wallet.SetMyFirstOpenServiceRs;
//import com.cssweb.shankephone.login.LoginManager;
//import com.cssweb.shankephone.preference.MPreference;
//
//import org.apache.http.Header;
//
//import java.util.List;
//
///**
// * Created by liwx on 2015/9/8.
// */
//public class SpServiceGateway extends MobileGateway {
//    public SpServiceGateway(Context context) {
//        super(context);
//    }
//
//    private final String TAG = "SpServiceGateway";
//
//    /**
//     * 查看服务详情
//     */
//    public void viewServiceInfo(String serviceId, final MobileGatewayListener<ViewServiceInfoRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/spservice/viewServiceInfo";
//        ViewServiceInfoRq request = new ViewServiceInfoRq();
//        request.setServiceId(serviceId);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<ViewServiceInfoRs>(ViewServiceInfoRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, ViewServiceInfoRs response) {
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
//
//    /**
//     * 显示服务列表
//     */
//    public void searchServiceList(String serviceName, int pageNumber, int pageSize, final MobileGatewayListener<SearchServiceListRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/spservice/searchServiceList";
//        PageInfo pageInfo = new PageInfo();
//        pageInfo.setPageNumber(pageNumber);
//        pageInfo.setPageSize(pageSize);
//        SearchServiceListRq request = new SearchServiceListRq();
//        request.setOsName(BizApplication.OS_NAME);
//        request.setWalletId(BizApplication.WALLET_ID);
//        request.setServiceName(serviceName);
//        request.setPageInfo(pageInfo);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<SearchServiceListRs>(SearchServiceListRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, SearchServiceListRs response) {
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
//
//    /**
//     * 添加我的服务列表
//     */
//    public void addMyService(String serviceId, String serviceState, final MobileGatewayListener<AddMyServiceRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/spservice/addMyService";
//        AddMyServiceRq request = new AddMyServiceRq();
//        request.setServiceId(serviceId);
//        request.setServiceState(serviceState);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<AddMyServiceRs>(AddMyServiceRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, AddMyServiceRs response) {
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
//
//    public void deleteServiceAuth(String serviceId, String cardNumber, int balance, final MobileGatewayListener<DeleteServiceAuthRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/spservice/deleteServiceAuth";
//        DeleteServiceAuthRq request = new DeleteServiceAuthRq();
//        request.setServiceId(serviceId);
//        request.setBalance(balance);
//        request.setCardNumber(cardNumber);
//        request.setIccid(DeviceInfo.getICCID(mApp));
//        request.setMsisdn(MPreference.getLoginId(mApp));
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<DeleteServiceAuthRs>(DeleteServiceAuthRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, DeleteServiceAuthRs response) {
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
//
//
//    /**
//     * 设置默认打开服务
//     */
//    public void setMyFirstOpenService(String serviceId, String firstOpenYn, final MobileGatewayListener<SetMyFirstOpenServiceRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/spservice/setMyFirstOpenService";
//        SetMyFirstOpenServiceRq request = new SetMyFirstOpenServiceRq();
//        request.setServiceId(serviceId);
//        request.setFirstOpenYn(firstOpenYn);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<SetMyFirstOpenServiceRs>(SetMyFirstOpenServiceRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, SetMyFirstOpenServiceRs response) {
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
//
//    /**
//     * 请求写卡
//     */
//
//    /**
//     * 获取充值记录
//     */
//    public void getTopupRecordList(String categoryCode, int orderStatus, int pageSize, int pageNumber, final MobileGatewayListener<GetTopupRecordListRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/topup/getTopupRecordList";
//        GetTopupRecordListRq request = new GetTopupRecordListRq();
//        PageInfo pageInfo = new PageInfo();
//        pageInfo.setPageNumber(pageNumber);
//        pageInfo.setPageSize(pageSize);
//        request.setPageInfo(pageInfo);
//        request.setCategoryCode(categoryCode);
//        request.setOrderStatus(orderStatus);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetTopupRecordListRs>(GetTopupRecordListRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetTopupRecordListRs response) {
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
//
//    /**
//     * 删除客户订单
//     */
//    public void deleteCustomerOrder(String orderId, final MobileGatewayListener<DeleteCustomerOrderRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/topup/deleteCustomerOrder";
//        DeleteCustomerOrderRq request = new DeleteCustomerOrderRq();
//        request.setOrderId(orderId);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<DeleteCustomerOrderRs>(DeleteCustomerOrderRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, DeleteCustomerOrderRs response) {
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
//
//    /**
//     * 请求个人化
//     */
//    public void triggerDeployService(String serviceId, final MobileGatewayListener<TriggerDeployServiceRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/spservice/triggerDeployService";
//        TriggerDeployServiceRq request = new TriggerDeployServiceRq();
//        request.setSeId(DeviceInfo.getICCID(mApp));
//        request.setServiceId(serviceId);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<TriggerDeployServiceRs>(TriggerDeployServiceRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, TriggerDeployServiceRs response) {
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
//
//    /**
//     * 获取非NFC服务列表
//     */
//    public void getServiceListForNoNFC(String serviceName, final MobileGatewayListener<GetServiceListForNoNFCRs> listener) {
//        final String url = BizApplication.BASE_ADDRESS + "/spservice/getServiceListForNoNFC";
//        GetServiceListForNoNFCRq request = new GetServiceListForNoNFCRq();
//        request.setOsName(BizApplication.OS_NAME);
//        request.setWalletId(BizApplication.WALLET_ID);
//        request.setServiceName(serviceName);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetServiceListForNoNFCRs>(GetServiceListForNoNFCRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetServiceListForNoNFCRs response) {
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
//
//    }
//
//    /**
//     * 换卡
//     */
//    public void requestChangeCard(String msisdn, String pwd, final MobileGatewayListener<RequestChangeCardRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/activate/requestChangeCard";
//        RequestChangeCardRq request = new RequestChangeCardRq();
//        request.setMsisdn(msisdn);
//        request.setSeId(DeviceInfo.getICCID(mApp));
//        request.setLoginPassword(pwd);
//        request.setImsi(DeviceInfo.getIMSI(mContext));
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<RequestChangeCardRs>(RequestChangeCardRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                if (listener != null) {
//                    listener.onHttpFailed(statusCode, headers);
//                }
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, RequestChangeCardRs response) {
//                if (listener != null) {
//                    if (response != null && response.getResult().getCode() == Result.OK) {
//                        listener.onSuccess(response);
//                    } else {
//                        listener.onFailed(response != null ? response.getResult() : null);
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
//
//    /**
//     * 获取商品列表
//     */
//    public void getProductList(final MobileGatewayListener<GetProductCategoryRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/order/getProductCategory";
//        GetProductCategoryRq rq = new GetProductCategoryRq();
//        rq.setOsName(BizApplication.OS_NAME);
//        mGateway.sendRequset(mApp, url, rq, new ResponseMappingHandler<GetProductCategoryRs>(GetProductCategoryRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                if (listener != null) {
//                    listener.onHttpFailed(statusCode, headers);
//                }
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetProductCategoryRs response) {
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
//
//    public void refundCsSubway(String orderNo, final MobileGatewayListener listener) {
//        String url = BizApplication.BASE_ADDRESS + "/topup/requestRefundTopup";
//        RequestRefundTopupRq rq = new RequestRefundTopupRq();
//        rq.setOrderNo(orderNo);
//        mGateway.sendRequset(mApp, url, rq, new ResponseMappingHandler<RequestRefundTopupRs>(RequestRefundTopupRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                if (listener != null) {
//                    listener.onHttpFailed(statusCode, headers);
//                }
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, RequestRefundTopupRs response) {
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
//
//    //删除已损坏卡片
//    public void triggerRemoveService(String serviceId, final MobileGatewayListener listener) {
//        String url = BizApplication.BASE_ADDRESS + "/spservice/triggerRemoveService";
//        TriggerRemoveServiceRq rq = new TriggerRemoveServiceRq();
//        rq.setServiceId(serviceId);
//        rq.setSeId(DeviceInfo.getICCID(mContext));
//        mGateway.sendRequset(mApp, url, rq, new ResponseMappingHandler<TriggerRemoveServiceRs>(TriggerRemoveServiceRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                if (listener != null) {
//                    listener.onHttpFailed(statusCode, headers);
//                }
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, TriggerRemoveServiceRs response) {
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
//
//    //更新服务状态
//    public void updateServiceStatus(String serviceId, String status, final MobileGatewayListener listener) {
//        String url = BizApplication.BASE_ADDRESS + "/spservice/updateServiceStatus";
//        UpdateServiceStatusRq rq = new UpdateServiceStatusRq();
//        rq.setServiceId(serviceId);
//        rq.setServiceStatus(status);
//        mGateway.sendRequset(mApp, url, rq, new ResponseMappingHandler<UpdateServiceStatusRs>(UpdateServiceStatusRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                if (listener != null) {
//                    listener.onHttpFailed(statusCode, headers);
//                }
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, UpdateServiceStatusRs response) {
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
//
//    //请求充值
//    public void requestTopup(int seType, String serviceId, String msisdn, String logicNum, int beforeBalance, String physicalNum, String orderId, final MobileGatewayListener listener) {
//        String url = BizApplication.BASE_ADDRESS + "/topup/requestTopup";
//        RequestTopupRq rq = new RequestTopupRq();
//        rq.setServiceId(serviceId);
//        rq.setMsisdn(msisdn);
//        rq.setLogicCardNum(logicNum);
//        rq.setBeforeBalance(beforeBalance);
//        rq.setPhysicalCardNum(physicalNum);
//        rq.setSeId(DeviceInfo.getICCID(mContext));
//        rq.setOrderId(orderId);
//        rq.setSeType(seType);
//        mGateway.sendRequset(mApp, url, rq, new ResponseMappingHandler<RequestTopupRs>(RequestTopupRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, RequestTopupRs response) {
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
//
//    //写卡指令交互
//    public void sendRAPDU(String transactionId, int cmdSequence, String rAPDU, String orderId, final MobileGatewayListener listener) {
//        String url = BizApplication.BASE_ADDRESS + "/topup/sendRAPDU";
//        SendRAPDURq rq = new SendRAPDURq();
//        rq.setTransactionId(transactionId);
//        rq.setCmdSequence(cmdSequence);
//        rq.setrAPDU(rAPDU);
//        rq.setOrderId(orderId);
//        mGateway.sendRequset(mApp, url, rq, new ResponseMappingHandler<SendRAPDURs>(SendRAPDURs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, SendRAPDURs response) {
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
//
//    //请求个人化接口
//    public void requestAppMagr(String serviceId, String optType, String cityCode, int seIdType, final MobileGatewayListener<RequestAppMagrRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/spservice/requestAppMagr";
//        RequestAppMagrRq rq = new RequestAppMagrRq();
//        rq.setOptType(optType);
//        rq.setCityCode(cityCode);
//        rq.setSeIdType(seIdType);
//        rq.setSeId(DeviceInfo.getICCID(mContext));
//        rq.setServiceId(serviceId);
//        mGateway.sendRequset(mApp, url, rq, new ResponseMappingHandler<RequestAppMagrRs>(RequestAppMagrRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, RequestAppMagrRs response) {
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
//
//    /**
//     * 卡片结果通知
//     */
//    public void notiRAPDU(String transactionId, int cmdSeq, List<Command> cmdList, final MobileGatewayListener<NotiRapduRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/spservice/notiRapdu";
//        NotiRapduRq rq = new NotiRapduRq();
//        rq.setTransactionId(transactionId);
//        rq.setCmdSeq(cmdSeq);
//        rq.setCommandList(cmdList);
//        mGateway.sendRequset(mApp, url, rq, new ResponseMappingHandler<NotiRapduRs>(NotiRapduRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, NotiRapduRs response) {
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
//
//    /**
//     * 同步写卡状态
//     */
//    public void requestSynTopupReslut(String orderId, final MobileGatewayListener<RequestSynTopupReslutRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/topup/requestSynTopupReslut";
//        RequestSynTopupReslutRq rq = new RequestSynTopupReslutRq();
//        rq.setOrderId(orderId);
//        mGateway.sendRequset(mApp, url, rq, new ResponseMappingHandler<RequestSynTopupReslutRs>(RequestSynTopupReslutRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, RequestSynTopupReslutRs response) {
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
//
//    /**
//     * 通知写卡状态
//     */
//    public void notiTopupResult(String orderId, int topupStatus, String transeDate, final MobileGatewayListener<NotiTopupReslutRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/topup/notiTopupReslut";
//        NotiTopupReslutRq rq = new NotiTopupReslutRq();
//        rq.setOrderId(orderId);
//        rq.setTopupStatus(topupStatus);
//        rq.setTransDate(transeDate);
//        mGateway.sendRequset(mApp, url, rq, new ResponseMappingHandler<NotiTopupReslutRs>(NotiTopupReslutRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, NotiTopupReslutRs response) {
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
//
//    /**
//     * 更新地铁逻辑卡号
//     */
//    public void updateMyService(String serviceId, String cardNumber, String status, final MobileGatewayListener<UpdateMyServiceRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/spservice/updateMyService";
//        UpdateMyServiceRq rq = new UpdateMyServiceRq();
//        rq.setServiceId(serviceId);
//        rq.setCardNumber(cardNumber);
//        rq.setServiceState(status);
//        mGateway.sendRequset(mApp, url, rq, new ResponseMappingHandler<UpdateMyServiceRs>(UpdateMyServiceRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, UpdateMyServiceRs response) {
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
//
//    //===================HCE单程票==================
//
//    /**
//     * 检查是否可以空发应用
//     **/
//    public void checkEligibilityHce(String serviceId, final MobileGatewayListener<CheckEligibilityRs> listener) {
//        final String url = BizApplication.BASE_ADDRESS + "/spservice/checkEligibility";
//        CheckEligibilityRq request = new CheckEligibilityRq();
//        request.setServiceId(serviceId);
//        request.setSeId(DeviceInfo.getICCID(mContext));
//        MLog.d(TAG, "检查是否可以空发应用request= " + request.toString() + "  url = " + url);
//
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<CheckEligibilityRs>(CheckEligibilityRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, CheckEligibilityRs response) {
//                if (response != null && response.getResult().getCode() == Result.OK) {
//                    if (listener != null)
//                        listener.onSuccess(response);
//                } else {
//                    if (listener != null)
//                        listener.onFailed(response.getResult());
//                }
//            }
//
//            @Override
//            public void handleNoNetwork() {
//                onNoNetwork(listener);
//            }
//        });
//    }
//
//    /**
//     * 请求下发单程票
//     */
//    public void requestHceSjt(final MobileGatewayListener<RequestHceSjtRs> listener) {
//        final String url = BizApplication.BASE_ADDRESS + "/hceService/requestHceSjt";
//        RequestHceSjtRq request = new RequestHceSjtRq();
//        request.setMsisdn(LoginManager.getPhoneNumber(mContext));//LoginManager.getPhoneNumber(mContext)
//        request.setImei(DeviceInfo.getIMEI(mContext));//DeviceInfo.getIMEI(mContext)
//        request.setModelName(DeviceInfo.getDeviceModelName());
//        MLog.d(TAG, "请求下发单程票request= " + request.toString() + "  url = " + url);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<RequestHceSjtRs>(RequestHceSjtRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, RequestHceSjtRs response) {
//                if (response != null && response.getResult().getCode() == Result.OK) {
//                    if (listener != null)
//                        listener.onSuccess(response);
//                } else {
//                    if (listener != null)
//                        listener.onFailed(response.getResult());
//                }
//            }
//
//            @Override
//            public void handleNoNetwork() {
//                onNoNetwork(listener);
//            }
//        });
//    }
//
//    /**
//     * 我的单程票详情
//     */
//    public void getMyHceSjtService(final MobileGatewayListener<GetMyHceSjtServiceRs> listener) {
//        final String url = BizApplication.BASE_ADDRESS + "/hceService/getMyHceSjtService";
//        GetMyHceSjtServiceRq request = new GetMyHceSjtServiceRq();
//        request.setMsisdn(LoginManager.getPhoneNumber(mContext));
//        MLog.d(TAG, "我的单程票详情request= " + request.toString() + "  url = " + url);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetMyHceSjtServiceRs>(GetMyHceSjtServiceRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetMyHceSjtServiceRs response) {
//                if (response != null && response.getResult().getCode() == Result.OK) {
//                    if (listener != null)
//                        listener.onSuccess(response);
//                } else {
//                    if (listener != null)
//                        listener.onFailed(response.getResult());
//                }
//            }
//
//            @Override
//            public void handleNoNetwork() {
//                onNoNetwork(listener);
//            }
//        });
//    }
//
//    /**
//     * 检测是否绑定支付账户
//     */
//    public void checkPaymentAccountBinding(final MobileGatewayListener<CheckPaymentAccountBindingRs> listener) {
//        final String url = BizApplication.BASE_ADDRESS + "/hceService/checkPaymentAccountBinding";
//        CheckPaymentAccountBindingRq request = new CheckPaymentAccountBindingRq();
//        request.setMsisdn(LoginManager.getPhoneNumber(mContext));
//        MLog.d(TAG, "检测是否绑定支付账户request= " + request.toString() + "  url = " + url);
//
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<CheckPaymentAccountBindingRs>(CheckPaymentAccountBindingRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, CheckPaymentAccountBindingRs response) {
//                if (response != null && response.getResult().getCode() == Result.OK) {
//                    if (listener != null)
//                        listener.onSuccess(response);
//                } else {
//                    if (listener != null)
//                        listener.onFailed(response != null ? response.getResult() : null);
//                }
//            }
//
//            @Override
//            public void handleNoNetwork() {
//                onNoNetwork(listener);
//            }
//        });
//    }
//
//    /**
//     * 上传最新单程票数据
//     */
//    public void updateMyLastHecSjtService(String sjtContent, final MobileGatewayListener<UpdateMyLastHecSjtServiceRs> listener) {
//        final String url = BizApplication.BASE_ADDRESS + "/hceService/updateMyLastHecSjtService";
//        UpdateMyLastHecSjtServiceRq request = new UpdateMyLastHecSjtServiceRq();
//        request.setSjtContent(sjtContent);
//        request.setMsisdn(LoginManager.getPhoneNumber(mContext));
//        MLog.d(TAG, "上传最新单程票数据request= " + request.toString() + "  url = " + url);
//
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<UpdateMyLastHecSjtServiceRs>(UpdateMyLastHecSjtServiceRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, UpdateMyLastHecSjtServiceRs response) {
//                if (response != null && response.getResult().getCode() == Result.OK) {
//                    if (listener != null)
//                        listener.onSuccess(response);
//                } else {
//                    if (listener != null)
//                        listener.onFailed(response != null ? response.getResult() : null);
//                }
//            }
//
//
//            @Override
//            public void handleNoNetwork() {
//                onNoNetwork(listener);
//            }
//        });
//    }
//
//    /**
//     * 检测是否更换手机
//     */
//    public void checkHceDeviceChanged(final MobileGatewayListener<CheckHceDeviceChangedRs> listener) {
//        final String url = BizApplication.BASE_ADDRESS + "/hceService/checkHceDeviceChanged";
//        CheckHceDeviceChangedRq request = new CheckHceDeviceChangedRq();
//        request.setMsisdn(LoginManager.getPhoneNumber(mContext));
//        request.setImei(DeviceInfo.getIMEI(mContext));
//        MLog.d(TAG, "检测是否更换手机request= " + request.toString() + "  url = " + url);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<CheckHceDeviceChangedRs>(CheckHceDeviceChangedRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, CheckHceDeviceChangedRs response) {
//                if (response != null && response.getResult().getCode() == Result.OK) {
//                    if (listener != null)
//                        listener.onSuccess(response);
//                } else {
//                    if (listener != null)
//                        listener.onFailed(response != null ? response.getResult() : null);
//                }
//            }
//
//            @Override
//            public void handleNoNetwork() {
//                onNoNetwork(listener);
//            }
//        });
//    }
//
//    /**
//     * 获取主页服务列表
//     */
//    public void getTabServiceList(final MobileGatewayListener<GetTabServiceListRs> listener) {
//        final String url = BizApplication.BASE_ADDRESS + "/spservice/getTabServiceList";
//        GetTabServiceListRq request = new GetTabServiceListRq();
//        request.setWalletId(BizApplication.WALLET_ID);
//        request.setOsName(BizApplication.OS_NAME);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetTabServiceListRs>(GetTabServiceListRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetTabServiceListRs response) {
//                if (response != null && response.getResult().getCode() == Result.OK) {
//                    if (listener != null)
//                        listener.onSuccess(response);
//                } else {
//                    if (listener != null)
//                        listener.onFailed(response != null ? response.getResult() : null);
//                }
//            }
//
//            @Override
//            public void handleNoNetwork() {
//                onNoNetwork(listener);
//            }
//        });
//    }
//
//    /**
//     * 获取城市默认商品编码
//     */
//    public void getDefaultProductCategoryCode(String cityCode, final MobileGatewayListener<GetDefaultProductCategoryCodeRs> listener) {
//        final String url = BizApplication.BASE_ADDRESS + "/order/getDefaultProductCategoryCode";
//        GetDefaultProductCategoryCodeRq request = new GetDefaultProductCategoryCodeRq();
//        request.setCityCode(cityCode);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetDefaultProductCategoryCodeRs>(GetDefaultProductCategoryCodeRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetDefaultProductCategoryCodeRs response) {
//                if (response != null && response.getResult().getCode() == Result.OK) {
//                    if (listener != null)
//                        listener.onSuccess(response);
//                } else {
//                    if (listener != null)
//                        listener.onFailed(response != null ? response.getResult() : null);
//                }
//            }
//
//            @Override
//            public void handleNoNetwork() {
//                onNoNetwork(listener);
//            }
//        });
//    }
//
//    /**
//     * 获取NFC订单详情
//     */
//    public void getNfcOrderDetail(String cityCode, String orderNum, final MobileGatewayListener<GetNfcOrderDetailRs> listener) {
//        final String url = BizApplication.BASE_ADDRESS + "/order/getNfcOrderDetail";
//        GetNfcOrderDetailRq request = new GetNfcOrderDetailRq();
//        request.cityCode = cityCode;
//        request.orderNo = orderNum;
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetNfcOrderDetailRs>(GetNfcOrderDetailRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetNfcOrderDetailRs response) {
//                if (response != null && response.getResult().getCode() == Result.OK) {
//                    if (listener != null)
//                        listener.onSuccess(response);
//                } else {
//                    if (listener != null)
//                        listener.onFailed(response != null ? response.getResult() : null);
//                }
//            }
//
//            @Override
//            public void handleNoNetwork() {
//                onNoNetwork(listener);
//            }
//        });
//    }
//
//}
