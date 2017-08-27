package com.whl.client.home.ticket;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.whl.framework.downloadlibrary.DownloadConstants;
import com.whl.framework.utils.DeviceInfo;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.app.CssConstant;
import com.whl.client.home.ticket.detail.STGetTicketActivity;
import com.whl.client.view.RoundLineNameView;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liwx on 2015/11/5.
 */
public class SingleTicketManager {
    private static final String TAG = "SingleTicketManager";

    public static final String ST_ORDER_ID = "orderId";

    public static final String TICKET_TYPE_BY_PRICE_COMMON = "1";//普通单程票固定票价
    public static final String TICKET_TYPE_BY_STATION_COMMON = "0";//普通单程票有终点站
    public static final String TICKET_TYPE_BY_PRICE_QRCODE = "3";//二维码单程票固定票价
    public static final String TICKET_TYPE_BY_STATION_QRCODE = "2";//二维码单程票有终点站

    public static final int SELECT_START_STATION_REQUEST = 101;
    public static final int SELECT_END_STATION_REQUEST = 102;
    public static final int SELECT_STATION_RESULT_OK = 201;
    public static final String LINE_CODE_APM = "68";
    public static final String ORDERSTATUS_HANDS = "OrderStatus_shouhuan";

    public static final String STATION = "station";
    public static final String START_STATION = "start_station";
    public static final String END_STATION = "end_station";

    public static final String INDEX_SELECT_STATION_TYPE = "index_select_station_type";

    public static final int MAP_WIDTH_GUANGZHOU = 4200;
    public static final int MAP_HEIGHT_GUANGZHOU = 3800;

    private Context mContext;
    private Resources res;

    private MapManager mMapManager;
    private boolean isCacheTilesFinish = false;
    //action
    public static final String ACTION_LAUNCH_BUYAGAIN_BY_GETTICKETCOMPLETE = "com.cssweb.shankephone.ACTION_LAUNCH_BUYAGAIN_BY_GETTICKETCOMPLETE";

    private ExecutorService mExecutor = Executors.newCachedThreadPool();
    private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SingleTicketManager(Context context) {
        mContext = context;
        res = context.getResources();
    }


    /**
     * 跳转至支付成功页面
     * 补票订单支付成功时 传 suborderId
     */
    public static void moveToPaySuccessPage(Context context, String serviceId, String orderId, String cityCode, int gateStatus, boolean isRepayOrder, String subOrderId) {
    }

    /**
     * 跳转至取票页面
     */
    public static void moveGetTicketPage(Context context, String qrCodeData, String orderId, String cityCode, String categoryCode) {
        Intent intent = new Intent(context, STGetTicketActivity.class);
        intent.putExtra(CssConstant.KEY_QR_CODE_TOKEN, qrCodeData);
        intent.putExtra(SingleTicketManager.ST_ORDER_ID, orderId);
        intent.putExtra(CssConstant.KEY_CATEGORY_CODE, categoryCode);
        intent.putExtra(CssConstant.KEY_CITY_CODE, cityCode);
        context.startActivity(intent);
    }

    /**
     * 处理单程票订单状态
     */
    public void handleSingleTicketOrderStatus(TextView area, FrameLayout left_bg, int status, String categoryCode) {
        MLog.d(TAG, "handleSingleTicketOrderStatus = " + status);
        GradientDrawable myGrad = (GradientDrawable) left_bg.getBackground();
        area.setTextColor(res.getColor(R.color.FFFFFF));
        switch (status) {
            case CssConstant.ORDER.STATUS_SINGLE_TICKET_PREGET_TICKET:
                //普通单程票：待取票,电子单程票：待进站
                if (categoryCode.equals(CssConstant.ORDER.CATEGORYCODE_SINGLETICKET)) {
                    area.setText(mContext.getString(R.string.st_pre_get_ticket));
                    //                    left_bg.setBackgroundResource(R.drawable.line_green);

                } else {
                    area.setText(mContext.getString(R.string.st_pre_entry));
                    //                    left_bg.setBackgroundResource(R.drawable.line_green);
                }
                myGrad.setColor(mContext.getResources().getColor(R.color._85BE46));
                break;
            case CssConstant.ORDER.STATUS_SINGLE_TICKET_WITHOUT_PAY:
                //未支付订单
                //                area.setTextColor(res.getColor(R.color.st_order_item_status2));
                area.setText(mContext.getString(R.string.st_without_pay));
                myGrad.setColor(mContext.getResources().getColor(R.color._F94F4F));
                break;
            case CssConstant.ORDER.STATUS_SINGLE_TICKET_CANCELED:
                //已取消订单
                //                area.setTextColor(res.getColor(R.color.st_order_item_status3));
                area.setText(mContext.getString(R.string.st_canceled));
                myGrad.setColor(mContext.getResources().getColor(R.color._61BEF2));
                //                left_bg.setBackgroundResource(R.drawable.line_blue);
                break;
            case CssConstant.ORDER.STATUS_SINGLE_TICKET_COMPLETE:
                //已取票订单
                //                area.setTextColor(res.getColor(R.color.st_order_item_status3));
                area.setText(mContext.getString(R.string.st_complete_order));
                myGrad.setColor(mContext.getResources().getColor(R.color._6C80E6));
                break;
            case CssConstant.ORDER.STATUS_SINGLE_TICKET_REFUND:
                //已退票订单
            case CssConstant.ORDER.STATUS_SINGLE_TICKET_REFUNDING:
                //退款中
                area.setText(mContext.getString(R.string.st_refund));
                myGrad.setColor(mContext.getResources().getColor(R.color._6C80E6));
                break;
            case CssConstant.ORDER.STATUS_SINGLE_TICKET_CGCOMPLETE:
                ////已完成
                myGrad.setColor(mContext.getResources().getColor(R.color._FFAE41));
                area.setText(mContext.getString(R.string.cg_complete_order));
                break;
            case CssConstant.ORDER.STATUS_QRCODE_TICKET_PRE_EXIT:
                //电子单程票：待出站
                myGrad.setColor(mContext.getResources().getColor(R.color._01C8C6));
                area.setText(mContext.getString(R.string.st_pre_exit));
                break;
            case CssConstant.ORDER.STATUS_QRCODE_TICKET_ALREADY_EXIT:
                //电子单程票,已出站
                myGrad.setColor(mContext.getResources().getColor(R.color._FFAE41));
                area.setText(mContext.getString(R.string.st_already_exit));
                break;
            case CssConstant.ORDER.STATUS_QRCODE_TICKET_EXIT_PAY:
                //电子单程票,出站补票
                myGrad.setColor(mContext.getResources().getColor(R.color._01C8C6));
                area.setText(mContext.getString(R.string.st_exit_pay));
                break;
            case CssConstant.ORDER.STATUS_SINGLE_TICKET_ERROR:
                //其他状态
                myGrad.setColor(mContext.getResources().getColor(R.color._6C80E6));
                area.setText(mContext.getString(R.string.other_status));
                break;
            case CssConstant.ORDER.STATUS_QRCODE_TICKET_INVALID:
                //已失效
                myGrad.setColor(mContext.getResources().getColor(R.color._6C80E6));
                area.setText(mContext.getString(R.string.order_invalid));
                break;
            case CssConstant.ORDER.STATUS_QRCODE_TICKET_DUPLICATE_ENTRY:
                //重复进站
                myGrad.setColor(mContext.getResources().getColor(R.color._6C80E6));
                area.setText(mContext.getString(R.string.duplicate_entry));
                break;
        }
    }

    public void displayOrderHands(TextView area, ImageView left_bg, int status) {
        MLog.d(TAG, "handleSingleTicketOrderStatus = " + status);
        switch (status) {
            case CssConstant.ORDER.STATUS_CS_METRO_CARD_RECHARGE_SUCCESS://
                area.setTextColor(res.getColor(R.color.st_order_item_status4));
                left_bg.setBackgroundResource(R.drawable.line_green);
                area.setText(mContext.getResources().getString(R.string.wristband_topup_success));
                break;
            case CssConstant.ORDER.STATUS_CS_METRO_CARD_RECHARGING://
                area.setTextColor(res.getColor(R.color.st_order_item_status3));
                area.setText(mContext.getResources().getString(R.string.wristband_topup));
                left_bg.setBackgroundResource(R.drawable.line_blue);
                break;
            case CssConstant.ORDER.STATUS_CS_METRO_CARD_RE_WRITE_CARD://
                area.setTextColor(res.getColor(R.color.st_order_item_status1));
                area.setText(mContext.getResources().getString(R.string.wristband_paid));
                left_bg.setBackgroundResource(R.drawable.line_orange);
                break;
            case CssConstant.ORDER.STATUS_CS_METRO_CARD_REFUNDED://
                area.setTextColor(res.getColor(R.color.st_order_item_status5));
                area.setText(mContext.getResources().getString(R.string.wristband_refund));
                left_bg.setBackgroundResource(R.drawable.line_purple);
                break;

        }
    }

    public void displayOrderCard(TextView area, ImageView left_bg, int status) {
        MLog.d(TAG, "handleSingleTicketOrderStatus = " + status);
        switch (status) {
            case CssConstant.ORDER.STATUS_CS_METRO_CARD_RECHARGE_SUCCESS://支付成功
                area.setTextColor(res.getColor(R.color.st_order_item_status4));
                left_bg.setBackgroundResource(R.drawable.line_green);
                area.setText(mContext.getResources().getString(R.string.wristband_topup_success));
                break;
            case CssConstant.ORDER.STATUS_CS_METRO_CARD_PRE_PAY://待支付
                area.setTextColor(res.getColor(R.color.st_order_item_status2));
                left_bg.setBackgroundResource(R.drawable.line_red);
                area.setText(mContext.getResources().getString(R.string.pre_pay));
                break;
            case CssConstant.ORDER.STATUS_CS_METRO_CARD_RECHARGING://充值中
                area.setTextColor(res.getColor(R.color.st_order_item_status3));
                area.setText(mContext.getResources().getString(R.string.wristband_topup));
                left_bg.setBackgroundResource(R.drawable.line_blue);
                break;
            case CssConstant.ORDER.STATUS_CS_METRO_CARD_RE_WRITE_CARD://补充值
                area.setTextColor(res.getColor(R.color.st_order_item_status1));
                area.setText(mContext.getResources().getString(R.string.re_write_card));
                left_bg.setBackgroundResource(R.drawable.line_orange);
                break;
            case CssConstant.ORDER.STATUS_CS_METRO_CARD_REFUNDED://已退款
                area.setTextColor(res.getColor(R.color.st_order_item_status5));
                area.setText(mContext.getResources().getString(R.string.wristband_refund));
                left_bg.setBackgroundResource(R.drawable.line_purple);
                break;
        }
    }


    public static SpannableString getSpananbleString(int color, String spannable, String pattern) {
        if (spannable == null || pattern == null) {
            return new SpannableString("");
        }
        SpannableString spannableString = new SpannableString(spannable);
        int start = spannable.toLowerCase().indexOf(pattern.toLowerCase());
        if (start != -1) {
            spannableString.setSpan(new ForegroundColorSpan(color), start, start + pattern.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    public void cacheSingleTicketData(final BaseActivity context, final int width, final int height, final String cityCode, OnCacheSingletTicketDataListener listener) {
        mMapManager = BizApplication.getInstance().getMapManager();
        getLineCode(cityCode, context, listener);
        //        getMetroMapList(cityCode, context, listener);
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mMapManager.clearMemoryCache();
                mMapManager.calculateTilesCountAndCache(width, height, cityCode);
                isCacheTilesFinish = true;
            }

        });
    }

    private void getLineCode(final String cityCode, final BaseActivity context, final OnCacheSingletTicketDataListener listener) {
    }

    private void cacheComplete(BaseActivity context, final OnCacheSingletTicketDataListener listener) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isCacheTilesFinish) {
                        isCacheTilesFinish = false;
                        MLog.d(TAG, "cache complete ");
                        if (listener != null)
                            listener.onCacheComplete();
                        break;
                    }
                }
            }
        });
    }

    public interface OnCacheSingletTicketDataListener {
        void onCacheComplete();

        void onCacheFailed();
    }

    /**
     * 保存与本地路网图图片对应的版本信息
     */
    public void saveLocalVersionInfo() {
        String[] cityList = getLocalCityFromAsset();
        List<String> versionList = new ArrayList<>();
        for (int i = 0; i < cityList.length; i++) {
            versionList.add(Utils.getAssetFileContent(mContext, "metro" + File.separator + cityList[i] + File.separator + "version"));
        }
        for (int i = 0; i < cityList.length; i++) {
            String path = DownloadConstants.getMetroCacheDir(mContext).getPath() + File.separator + cityList[i];
            String version = versionList.get(i);
            MLog.d(TAG, "#### path = " + path + " version = " + version);
            //        String tilesPath = DownloadConstants.getMetroCacheDir(getApplicationContext()).getPath()+File.separator+"4401";
            File file = new File(path);
            //        File tilesFile = new File(tilesPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String versionPath = file.getPath() + File.separator + "version";
            File versionFile = new File(versionPath);
            if (versionFile.exists()) {
                MLog.d(TAG, "versionFile exist " + versionFile.getPath());
                continue;
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(versionPath);
                fos.write(version.getBytes());
            } catch (Exception e) {
                MLog.e(TAG, "saveLocalVersionFile occur error.", e);
            } finally {
                try {
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //            }
        //        });
    }

    public String[] getLocalCityFromAsset() {
        AssetManager manager = mContext.getAssets();
        try {
            return manager.list("metro");
        } catch (Exception e) {
            MLog.d(TAG, "get metro list from asset occur error ", e);
        }
        return null;
    }

    /**
     * 返回是否是电子单程票
     *
     * @param serviceId
     * @return true 电子单程票.false 普通单程票
     */
    public static boolean isQrCodeTicket(String serviceId) {
        if (!TextUtils.isEmpty(serviceId) && serviceId.equals(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET)) {
            return true;
        }
        return false;
    }

    /**
     * 返回类型是否是车票商品
     *
     * @param categoryCode
     * @return true 电子单程票.false 普通单程票
     */
    public static boolean isSJTCategory(String categoryCode) {
        if (!TextUtils.isEmpty(categoryCode) && categoryCode.equals(CssConstant.ORDER.CATEGORYCODE_SINGLETICKET) || categoryCode.equals(CssConstant.ORDER.CATEGORYCODE_QRCODE_TICKET)) {
            return true;
        }
        return false;
    }


    /**
     * 返回是否是电子单程票
     *
     * @param productCategory
     * @return true 电子单程票.false 普通单程票
     */
    public static boolean isQrCodeTicketByCategory(String productCategory) {
        if (!TextUtils.isEmpty(productCategory) && productCategory.equals(CssConstant.ORDER.CATEGORYCODE_QRCODE_TICKET)) {
            return true;
        }
        return false;
    }

    /**
     * 显示圆形线路名称
     *
     * @param textView  圆形textview控件
     * @param shortName 线路简称
     * @param color     线路颜色
     */
    public void setLineName(RoundLineNameView textView, String shortName, String color) {
        MLog.d(TAG, "setLineName = " + shortName);
        MLog.d(TAG, "color:" + color);
        if (!TextUtils.isEmpty(shortName) && shortName.length() > 1) {
            textView.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.st_order_line_name_size) / DeviceInfo.getDensity(mContext));
        } else {
            textView.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.st_order_line_name_size2) / DeviceInfo.getDensity(mContext));
        }
        textView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(color))
            textView.setBackgroundColor(Utils.getColorFromString(color));
        textView.setTextColor(mContext.getResources().getColor(R.color.FFFFFF));
        textView.setText(shortName);
    }

    /**
     * 时钟同步
     * 不能在UI线程调用
     */
    public long getNtpTime() {
        try {
            NTPUDPClient timeClient = new NTPUDPClient();
            InetAddress timeServerAddress = null;
            timeServerAddress = InetAddress.getByName(CssConstant.SINGLE_TICKET.URL_NTP_SERVER);
            TimeInfo timeInfo = timeClient.getTime(timeServerAddress);
            TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();
            TimeStamp timeStamp1 = timeInfo.getMessage().getOriginateTimeStamp();

            long delayTime = timeStamp1.getDate().getTime() - timeStamp.getDate().getTime();

            //            MLog.d(TAG, "时间1= " + mDateFormat.format(timeStamp1.getDate().getTime()));
            //            MLog.d(TAG, "时间2= " + mDateFormat.format(timeStamp.getDate().getTime()));

            Date date = timeStamp.getDate();
            //            System.out.println(dateFormat.format(date));
            MLog.d(TAG, "时间差= " + delayTime + "  服务器时间= " + mDateFormat.format(date) + "当前时间= " + mDateFormat.format(System.currentTimeMillis()));
            return delayTime;

        } catch (Exception e) {
            MLog.d(TAG, "get NTP Time error::", e);
        }
        return 0;
    }

}
