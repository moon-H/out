//package com.cssweb.shankephone.gateway;
//
//import android.content.Context;
//
//import com.cssweb.framework.http.ResponseMappingHandler;
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.DeviceInfo;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.gateway.model.PageInfo;
//import com.cssweb.shankephone.gateway.model.order.GetAllProductOrderListRq;
//import com.cssweb.shankephone.gateway.model.order.GetAllProductOrderListRs;
//import com.cssweb.shankephone.gateway.model.order.GetProductByCityCodeRq;
//import com.cssweb.shankephone.gateway.model.order.GetProductByCityCodeRs;
//import com.cssweb.shankephone.gateway.model.singleticket.CancelOrderRq;
//import com.cssweb.shankephone.gateway.model.singleticket.CancelOrderRs;
//import com.cssweb.shankephone.gateway.model.singleticket.CheckStationCodeVersionRq;
//import com.cssweb.shankephone.gateway.model.singleticket.CheckStationCodeVersionRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetBuySinlgeTicketMaxNumRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetBuySinlgeTicketMaxNumRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetCityCodeListRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetCityCodeListRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetCityInfoRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetCityInfoRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetLineCodeListRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetLineCodeListRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetMetroMapListRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetMetroMapListRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetQrCodeSjtRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetQrCodeSjtRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetQrCodeSjtStatusRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetQrCodeSjtStatusRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetRefundAmountRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetRefundAmountRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetStationCodeListRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetStationCodeListRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetStationDetailRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetStationDetailRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketOrderListRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketOrderListRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketPriceByStationRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketPriceByStationRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketPriceListRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketPriceListRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetUnFinishTicketOrderRq;
//import com.cssweb.shankephone.gateway.model.singleticket.GetUnFinishTicketOrderRs;
//import com.cssweb.shankephone.gateway.model.singleticket.MetroMapCheck;
//import com.cssweb.shankephone.gateway.model.singleticket.QrStatusUpdateNoticeRq;
//import com.cssweb.shankephone.gateway.model.singleticket.QrStatusUpdateNoticeRs;
//import com.cssweb.shankephone.gateway.model.singleticket.RequestRefundTicketRq;
//import com.cssweb.shankephone.gateway.model.singleticket.RequestRefundTicketRs;
//import com.cssweb.shankephone.gateway.model.spservice.GetSupportServiceYnRq;
//import com.cssweb.shankephone.gateway.model.spservice.GetSupportServiceYnRs;
//
//import org.apache.http.Header;
//
//import java.util.List;
//
///**
// * Created by liwx on 2015/10/22.
// */
//public class STGateway extends MobileGateway {
//
//    public STGateway(Context context) {
//        super(context);
//    }
//
//    /**
//     * 获取城市代码列表
//     */
//    public void getCityCodeList(final MobileGatewayListener<GetCityCodeListRs> listener) {
//        final String url = BizApplication.BASE_ADDRESS + "/ticket/getCityCodeList";
//        GetCityCodeListRq req = new GetCityCodeListRq();
//        req.osName = BizApplication.OS_NAME;
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetCityCodeListRs>(GetCityCodeListRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetCityCodeListRs response) {
//                if (listener != null) {
//                    if (response != null && response.getResult().getCode() == Result.OK) {
//                        listener.onSuccess(response);
//                    } else {
//                        if (response != null) {
//                            listener.onFailed(response.getResult());
//                        }
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
//     * 获取定值票价列表
//     */
//    public void getTicketPriceList(String cityCode, String linecode, final MobileGatewayListener<GetTicketPriceListRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getTicketPriceList";
//        GetTicketPriceListRq req = new GetTicketPriceListRq();
//        req.setWalletId(BizApplication.WALLET_ID);
//        req.setCityCode(cityCode);
//        req.setLineCode(linecode);
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetTicketPriceListRs>(GetTicketPriceListRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetTicketPriceListRs response) {
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
//     * 获取线路代码
//     */
//    public void getLineCodeList(String cityCode, final MobileGatewayListener<GetLineCodeListRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getLineCodeList";
//        GetLineCodeListRq req = new GetLineCodeListRq();
//        req.setWalletId(BizApplication.WALLET_ID);
//        req.setCityCode(cityCode);
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetLineCodeListRs>(GetLineCodeListRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetLineCodeListRs response) {
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
//     * 获取站点代码信息
//     */
//    public void getStationCodeList(String cityCode, String lineCode, final MobileGatewayListener<GetStationCodeListRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getStationCodeList";
//        GetStationCodeListRq req = new GetStationCodeListRq();
//        req.setCityCode(cityCode);
//        req.setLineCode(lineCode);
//        req.setWalletId(BizApplication.WALLET_ID);
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetStationCodeListRs>(GetStationCodeListRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetStationCodeListRs response) {
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
//     * 计算票价
//     */
//    public void getTicketPriceByStation(String cityCode, String startStationCode, String endStationCode, final MobileGatewayListener<GetTicketPriceByStationRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getTicketPriceByStation";
//        GetTicketPriceByStationRq req = new GetTicketPriceByStationRq();
//        req.setWalletId(BizApplication.WALLET_ID);
//        req.setCityCode(cityCode);
//        req.setStartStationCode(startStationCode);
//        req.setEndStationCode(endStationCode);
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetTicketPriceByStationRs>(GetTicketPriceByStationRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetTicketPriceByStationRs response) {
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
//     * 获取单次可购买最大车票张数
//     */
//    public void getBuySinlgeTicketMaxNum(String cityCode, final MobileGatewayListener<GetBuySinlgeTicketMaxNumRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getBuySinlgeTicketMaxNum";
//        GetBuySinlgeTicketMaxNumRq req = new GetBuySinlgeTicketMaxNumRq();
//        req.setWalletId(BizApplication.WALLET_ID);
//        req.setCityCode(cityCode);
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetBuySinlgeTicketMaxNumRs>(GetBuySinlgeTicketMaxNumRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetBuySinlgeTicketMaxNumRs response) {
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
//     * 获取订单列表
//     */
//    public void getTicketOrderList(int orderStatus, int pageNumber, int pageSize, String cityCode, String categoryCode, final MobileGatewayListener<GetTicketOrderListRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getTicketOrderList";
//        GetTicketOrderListRq req = new GetTicketOrderListRq();
//        PageInfo pageInfo = new PageInfo();
//        pageInfo.setPageNumber(pageNumber);
//        pageInfo.setPageSize(pageSize);
//        req.setPageInfo(pageInfo);
//        req.setOrderStatus(orderStatus);
//        req.setCityCode(cityCode);
//        req.setCategoryCode(categoryCode);
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetTicketOrderListRs>(GetTicketOrderListRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetTicketOrderListRs response) {
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
//     * 取消订单
//     */
//    public void cancelOrder(String orderNum, String cityCode, final MobileGatewayListener<CancelOrderRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/cancelOrder";
//        CancelOrderRq req = new CancelOrderRq();
//        req.setOrderNo(orderNum);
//        req.setCityCode(cityCode);
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<CancelOrderRs>(CancelOrderRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, CancelOrderRs response) {
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
//     * 获取退票金额
//     */
//    public void getRefundAmount(String orderNum, String cityCode, final MobileGatewayListener<GetRefundAmountRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getRefundAmount";
//        GetRefundAmountRq req = new GetRefundAmountRq();
//        req.setOrderNo(orderNum);
//        req.setCityCode(cityCode);
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetRefundAmountRs>(GetRefundAmountRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetRefundAmountRs response) {
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
//     * 请求退票
//     */
//    public void requestRefundTicket(String orderNum, int orderAmount, int refundAmount, String cityCode, final MobileGatewayListener<RequestRefundTicketRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/requestRefundTicket";
//        RequestRefundTicketRq req = new RequestRefundTicketRq();
//        req.setOrderNo(orderNum);
//        req.setOrderAmount(orderAmount);
//        req.setRefundAmount(refundAmount);
//        req.setCityCode(cityCode);
//
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<RequestRefundTicketRs>(RequestRefundTicketRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, RequestRefundTicketRs response) {
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
//    /***
//     * 获取地铁路网图信息
//     */
//    public void getMetroMapList(List<MetroMapCheck> list, final MobileGatewayListener<GetMetroMapListRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getMetroMapList";
//        GetMetroMapListRq req = new GetMetroMapListRq();
//        req.setMetroMapCheckList(list);
//        req.setOsName(BizApplication.OS_NAME);
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetMetroMapListRs>(GetMetroMapListRs.class) {
//
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetMetroMapListRs response) {
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
//     * 根据百度定位得到的CityCode获取当前城市的信息
//     */
//    public void getCityInfo(String originalCityCode, final MobileGatewayListener<GetCityInfoRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getCityInfo";
//        GetCityInfoRq req = new GetCityInfoRq();
//        req.setIsGdYn("Y");
//        req.setOriginalCityCode(originalCityCode);
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetCityInfoRs>(GetCityInfoRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetCityInfoRs response) {
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
//     * 检查站点信息版本
//     */
//    public void checkStationCodeVersion(String cityCode, int lineVersion, int stationVersion, final MobileGatewayListener<CheckStationCodeVersionRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/checkStationCodeVersion";
//        CheckStationCodeVersionRq request = new CheckStationCodeVersionRq();
//        request.setCityCode(cityCode);
//        request.setCurrentLineVersion(lineVersion);
//        request.setCurrentStationVersion(stationVersion);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<CheckStationCodeVersionRs>(CheckStationCodeVersionRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, CheckStationCodeVersionRs response) {
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
//     * 获取二维码信息
//     */
//    public void getQrCodeSjt(String cityCode, String orderNumber, String serviceId, final MobileGatewayListener<GetQrCodeSjtRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getQrCodeSjt";
//        GetQrCodeSjtRq request = new GetQrCodeSjtRq();
//        request.setCityCode(cityCode);
//        request.setOrderNo(orderNumber);
//        request.setServiceId(serviceId);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetQrCodeSjtRs>(GetQrCodeSjtRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetQrCodeSjtRs response) {
//                if (listener != null) {
//                    if (response != null && response.getResult().getCode() == Result.OK) {
//                        listener.onSuccess(response);
//                    } else {
//                        if (response != null) {
//                            listener.onFailed(response.getResult());
//                        }
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
//     * 获取二维码单程票状态
//     */
//    public void getQrCodeSjtStatus(String cityCode, String orderNumber, final MobileGatewayListener<GetQrCodeSjtStatusRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getQrCodeSjtStatus";
//        GetQrCodeSjtStatusRq request = new GetQrCodeSjtStatusRq();
//        request.setCityCode(cityCode);
//        request.setOrderNo(orderNumber);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetQrCodeSjtStatusRs>(GetQrCodeSjtStatusRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetQrCodeSjtStatusRs response) {
//                if (listener != null) {
//                    if (response != null && response.getResult().getCode() == Result.OK) {
//                        listener.onSuccess(response);
//                    } else {
//                        if (response != null) {
//                            listener.onFailed(response.getResult());
//                        }
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
//     * 获取当前城市是否支持电子单程票
//     */
//    public void getSupportServiceYn(String serviceId, String cityCode, final MobileGatewayListener<GetSupportServiceYnRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getSupportServiceYn";
//        GetSupportServiceYnRq request = new GetSupportServiceYnRq();
//        request.setServiceId(serviceId);
//        request.setCityCode(cityCode);
//        request.setUserId(DeviceInfo.getIMEI(mContext));
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetSupportServiceYnRs>(GetSupportServiceYnRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetSupportServiceYnRs response) {
//                if (listener != null) {
//                    if (response != null && response.getResult().getCode() == Result.OK) {
//                        listener.onSuccess(response);
//                    } else {
//                        if (response != null) {
//                            listener.onFailed(response.getResult());
//                        }
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
//     * 获取站点详情
//     */
//    public void getStationDetail(String cityCode, String stationCode, final MobileGatewayListener<GetStationDetailRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getStationDetail";
//        GetStationDetailRq request = new GetStationDetailRq();
//        request.setCityCode(cityCode);
//        request.setStationCode(stationCode);
//        mGateway.sendRequset(mApp, url, request, new ResponseMappingHandler<GetStationDetailRs>(GetStationDetailRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetStationDetailRs response) {
//                if (listener != null) {
//                    if (response != null && response.getResult().getCode() == Result.OK) {
//                        listener.onSuccess(response);
//                    } else {
//                        if (response != null) {
//                            listener.onFailed(response.getResult());
//                        }
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
//     * 单程票状态变更通知
//     */
//    public void qrStatusUpdateNotice(String cityCode, String deviceId, int sjtId, String sjtStatus, String handleDateTime, String trxType, int trxAmount, final MobileGatewayListener<QrStatusUpdateNoticeRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/qrStatusUpdateNotice";
//        QrStatusUpdateNoticeRq req = new QrStatusUpdateNoticeRq();
//        req.setCityCode(cityCode);
//        req.setDeviceId(deviceId);
//        req.setSjtId(sjtId);
//        req.setSjtStatus(sjtStatus);
//        req.setHandleDateTime(handleDateTime);
//        req.setTrxType(trxType);
//        req.setTrxAmount(trxAmount);
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<QrStatusUpdateNoticeRs>(QrStatusUpdateNoticeRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, QrStatusUpdateNoticeRs response) {
//                if (listener != null) {
//                    if (response != null && response.getResult().getCode() == Result.OK) {
//                        listener.onSuccess(response);
//                    } else {
//                        if (response != null) {
//                            listener.onFailed(response.getResult());
//                        }
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
//     * 单程票状态变更通知
//     */
//    public void getUnFinishTicketOrder(String cityCode, final MobileGatewayListener<GetUnFinishTicketOrderRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/ticket/getUnFinishTicketOrder";
//        GetUnFinishTicketOrderRq req = new GetUnFinishTicketOrderRq();
//        req.setCityCode(cityCode);
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetUnFinishTicketOrderRs>(GetUnFinishTicketOrderRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetUnFinishTicketOrderRs response) {
//                if (listener != null) {
//                    if (response != null && response.getResult().getCode() == Result.OK) {
//                        listener.onSuccess(response);
//                    } else {
//                        if (response != null) {
//                            listener.onFailed(response.getResult());
//                        }
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
//     * 获取全部商品列表
//     */
//    public void getAllProductOrderList(String cityCode, int orderStatus, String categoryCode, int pageNumber, int pageSize, final MobileGatewayListener<GetAllProductOrderListRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/order/getAllProductOrderList";
//        GetAllProductOrderListRq req = new GetAllProductOrderListRq();
//        PageInfo pageInfo = new PageInfo();
//        pageInfo.setPageNumber(pageNumber);
//        pageInfo.setPageSize(pageSize);
//        req.cityCode = cityCode;
//        req.orderStatus = orderStatus;
//        req.categoryCode = categoryCode;
//        req.osName = BizApplication.OS_NAME;
//        req.pageInfo = pageInfo;
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetAllProductOrderListRs>(GetAllProductOrderListRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetAllProductOrderListRs response) {
//                if (listener != null) {
//                    if (response != null && response.getResult().getCode() == Result.OK) {
//                        listener.onSuccess(response);
//                    } else {
//                        if (response != null) {
//                            listener.onFailed(response.getResult());
//                        }
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
//     * 获取商品种类
//     */
//    public void getProductByCityCode(String cityCode, final MobileGatewayListener<GetProductByCityCodeRs> listener) {
//        String url = BizApplication.BASE_ADDRESS + "/order/getProductByCityCode";
//        GetProductByCityCodeRq req = new GetProductByCityCodeRq();
//        req.cityCode = cityCode;
//        req.osName = BizApplication.OS_NAME;
//        mGateway.sendRequset(mApp, url, req, new ResponseMappingHandler<GetProductByCityCodeRs>(GetProductByCityCodeRs.class) {
//            @Override
//            public void handleFail(int statusCode, Header[] headers, byte[] response, Throwable error) {
//                onFailed(statusCode, headers, listener);
//            }
//
//            @Override
//            public void handleSuccess(int statusCode, Header[] headers, GetProductByCityCodeRs response) {
//                if (listener != null) {
//                    if (response != null && response.getResult().getCode() == Result.OK) {
//                        listener.onSuccess(response);
//                    } else {
//                        if (response != null) {
//                            listener.onFailed(response.getResult());
//                        }
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
//}
