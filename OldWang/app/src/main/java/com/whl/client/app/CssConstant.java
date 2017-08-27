package com.whl.client.app;

import java.util.UUID;

/**
 * Created by liwx on 2016/9/22.
 * 定义所有常量
 */
public class CssConstant {

    public static final int ERROR_CODE_DEFAULT = 101;//默认错误码

    public static final String KEY_QR_HELP_URL_VALUE = "http://120.52.9.5/event/qrcodehelp/activity.html";
    public static final String KEY_QR_HELP_TITLE = "qr_help_title";
    public static final String KEY_QR_HELP_URL_NAME = "qr_help_url";
    public static final String KEY_ORDERID = "orderId";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DATE = "date";
    public static final String KEY_CATEGORY_CODE = "categorycode";
    public static final String KEY_PICK_PHOTO_TYPE = "pick_photo_type";
    public static final String KEY_USER_CHANGE_DEVICE_INFO = "user_change_device_info";
    public static final String KEY_SERVICE_ID = "serviceId";
    public static final String KEY_CITY_CODE = "cityCode";
    public static final String KEY_CITY_NAME = "cityName";
    public static final String KEY_STATION_CODE = "stationCode";
    public static final String KEY_STATION_NAME = "stationName";
    public static final String KEY_LINE_NAME = "line_name";
    public static final String KEY_QR_CODE_TOKEN = "qr_code_token";
    public static final String KEY_SUB_ORDER_ID = "sub_orderId";
    public static final String KEY_GATE_STATUS = "gate_status";
    public static final String KEY_IS_REPAY_TICKET = "is_repay_ticket";
    public static final String KEY_SUB_ORDERID = "single_ticket_sub_orderId";//电子单程票子订单(补票订单)
    public static final String KEY_IS_SUPPORT_QR_CODE_TICKET_YN = "is_support_qr_code_ticket";//是否支持电子单程票
    public static final String KEY_IS_BUY_AGAIN = "is_buy_again";//是否是再次购买
    public static final String KEY_SJTID = "sjt_id";//电子单程票ID
    public static final String KEY_PUSH_MSG = "push_msg";//推送过来要显示的消息

    public static final String KEY_CLICK_HOT_SALE_TYPE = "KEY_CLICK_HOT_SALE_TYPE";//点击热推商品
    public static final String KEY_HOT_SALE_GOODS = "KEY_HOT_SALE_GOODS";//热推商品

    public static final String KEY_EVENT_SNAPSHOT_URL = "snapshot_url";//进站成功 活动图片下载地址
    public static final String KEY_EVENT_URL = "event_url";//活动链接
    public static final String KEY_EVENT = "event";//活动

    public static final String KEY_MESSAGE_TYPE = "msg_type";//消息类型


    public static final int FLAG_GET_PIC_ADVERTISE = 0;//启动页广告
    public static final int FLAG_GET_PIC_BBS = 1;
    public static final int FLAG_GET_PIC_INVITE_FRIEND = 7;

    public static final int FLAG_REFRESH = 1;
    public static final int FLAG_LOAD_MORE = 2;

    public static final int PICK_PHOTO_TYPE_USER_HEAD = 1;//选择头像
    public static final int PICK_PHOTO_TYPE_BBS_IMG = 2;//论坛发帖用

    public static final int REQUEST_CODE_TAKE_PHOTO = 100;//拍照
    public static final int REQUEST_CODE_CROP_PHOTO = 101;//裁剪图片

    public static final int IMAGE_TYPE_PUBLISH_TOPIC = 0;//发帖
    public static final int IMAGE_TYPE_REPLY_TOPIC = 2;//回帖
    public static final int IMAGE_TYPE_USER_HEAD = 1;//头像


    public static class Action {
        public static final String ACTION_SWITCH_MAP = "com.cssweb.shankephone.ACTION_SWITCH_MAP";//切换路网图
        public static final String ACTION_LOGIN_SUCCESS = "com.cssweb.shankephone.ACTION_LOGIN_SUCCESS";//登录成功
        public static final String ACTION_SWITCH_FRAGMENT_NFC_TOPUP = "com.cssweb.shankephone.ACTION_SWITCH_FRAGMENT_NFC_TOPUP";//切换到余额页面
        public static final String ACTION_TURN_TO_ORDER_BRACELET = "com.cssweb.shankephone.ACTION_TURN_TO_ORDER_BRACELET";//跳转到订单-手环
        public static final String ACTION_PUSH_MSG_INBOX = "com.cssweb.shankephone.ACTION_PUSH_MSG_INBOX";//收件箱类型推送
        public static final String ACTION_PUSH_MSG_URL = "com.cssweb.shankephone.ACTION_PUSH_MSG_URL";//URL类型推送
        public static final String ACTION_SYNC_REAL_CARD_ORDER = "com.cssweb.shankephone.ACTION_SYNC_REAL_CARD_ORDER";//同步实体卡订单

        public static final String ACTION_SWITCH_FRAGMENT_REALCARD_ORDER = "com.cssweb.shankephone.ACTION_SWITCH_FRAGMENT_REALCARD_ORDER";//切换到实体卡订单
        public static final String ACTION_SWITCH_FRAGMENT_CS_SUBWAY_ORDER = "com.cssweb.shankephone.ACTION_SWITCH_FRAGMENT_CS_SUBWAY_ORDER";//切换到长沙地铁订单
        public static final String ACTION_SWITCH_FRAGMENT_BRACELET_ORDER = "com.cssweb.shankephone.ACTION_SWITCH_FRAGMENT_BRACELET_ORDER";//切换到手环订单
        public static final String ACTION_SWITCH_FRAGMENT_QR_CODE_ORDER = "com.cssweb.shankephone.ACTION_SWITCH_FRAGMENT_QR_CODE_ORDER";//切换到二维码单程票订单
        public static final String ACTION_SWITCH_FRAGMENT_COMMON_TICKET_ORDER = "ACTION_SWITCH_FRAGMENT_COMMON_TICKET_ORDER";//切换到普通单程票订单

        public static final String ACTION_SINGLETICKET_LOAD_DEFAULT_METRO_INFO = "com.cssweb.shankephone.ACTION_SINGLETICKET_LOAD_DEFAULT_METRO_INFO";//加载默认城市的路网图信息
        public static final String ACTION_LAUNCH_EVENT_BY_PUSH = "com.cssweb.shankephone.ACTION_LAUNCH_EVENT_BY_PUSH";//通过推送启动活动页
        public static final String ACTION_LAUNCH_EVENT_BY_ADVERTISEMENT = "com.cssweb.shankephone.ACTION_LAUNCH_EVENT_BY_ADVERTISEMENT";//通过广告启动活动页
        public static final String ACTION_ADD_MAP_MARKER = "com.cssweb.shankephone.ACTION_ADD_MAP_MARKER";//添加地图兴趣点

        public static final String ACTION_UPDATE_SINGLE_TICKET_ORDER = "com.cssweb.shankephone.ACTION_UPDATE_SINGLE_TICKET_ORDER";//更新单程票订单

        public static final String ACTION_BBS_POST_SUCCESS = "com.cssweb.shankephone.ACTION_BBS_POST_SUCCESS";//论坛发帖成功

        public static final String ACTION_UPDATE_USER_HEAD = "com.cssweb.shankephone.ACTION_UPDATE_USER_HEAD";//更新用户头像

        public static final String ACTION_LOGOUT_CLIENT = "com.cssweb.shankephone.ACTION_LOGOUT_CLIENT";//退出登录
        /***
         * 用户换设备登录
         */
        public static final String ACTION_USER_CHANGE_DEVICE = "com.cssweb.shankephone.ACTION_USER_CHANGE_DEVICE";

        public static final String ACTION_QRCODE_ENTRY_STATION_SUCCESS = "com.cssweb.shankephone.ACTION_QRCODE_ENTRY_STATION_SUCCESS";//进站成功
        public static final String ACTION_QRCODE_EXIT_STATION_SUCCESS = "com.cssweb.shankephone.ACTION_QRCODE_EXIT_STATION_SUCCESS";//出站成功
        public static final String ACTION_QRCODE_OVER_TIME = "com.cssweb.shankephone.ACTION_QRCODE_OVER_TIME";//二维码单程票超时
        public static final String ACTION_QRCODE_OVER_RIDE = "com.cssweb.shankephone.ACTION_QRCODE_OVER_RIDE";//二维码单程票超乘
        public static final String ACTION_QRCODE_OVER_TIME_RIDE = "com.cssweb.shankephone.ACTION_QRCODE_OVER_TIME_RIDE";//二维码单程票超时和超乘
        public static final String ACTION_QRCODE_MULTI_ENTRY = "com.cssweb.shankephone.ACTION_QRCODE_MULTI_ENTRY";//二维码单程票重复进站

        public static final String ACTION_GET_TICKET_SUCCESS = "com.cssweb.shankephone.ACTION_GET_TICKET_SUCCESS";//普通单程票取票成功

        public static final String ACTION_START_LOCATION = "com.cssweb.shankephone.ACTION_START_LOCATION";//发起定位

        public static final String ACTION_BUY_HOT_SALE_GOODS = "com.cssweb.shankephone.ACTION_BUY_HOT_SALE_GOODS";//购买热销咖啡

    }

    /**
     * 主页
     */
    public static class HOME {
        public static final String FRAGMENT_TAG_MAP = "STMapFragment";
        public static final String FRAGMENT_TAG_NFC_TOPUP = "FRAGMENT_TAG_NFC_TOPUP";
        public static final String FRAGMENT_TAG_BRACELET = "FRAGMENT_TAG_BRACELET";
        public static final String FRAGMENT_TAG_HCE_TICKET = "FRAGMENT_TAG_HCE_TICKET";
        public static final String FRAGMENT_TAG_TICKET_PRICE = "FRAGMENT_TAG_TICKET_PRICE";//单程票票价
        public static final String FRAGMENT_TAG_ADD_NFC_SERVICE = "FRAGMENT_TAG_ADD_NFC_SERVICE";
        public static final String FRAGMENT_TAG_ADD_SERVICE_DETAIL = "FRAGMENT_TAG_ADD_SERVICE_DETAIL";
        public static final String FRAGMENT_TAG_ERROR = "FRAGMENT_TAG_ERROR";
        public static final String FRAGMENT_TAG_REAL_CARD_DETAIL = "FRAGMENT_TAG_REAL_CARD_DETAIL";
        public static final String FRAGMENT_TAG_SWIPE_REALCARD = "FRAGMENT_TAG_SWIPE_REALCARD";

        public static final int INDEX_PERSONALLISE_SUCCESS = 1001;//个人化成功
        public static final int INDEX_SWITCH_TO_MAP_FRAGMENT = 1002;//切换到路网图页面
        public static final int INDEX_SWITCH_TO_PRICE_FRAGMENT = 1003;//切换到固定票价页面

    }

    /**
     * HCE单程票
     */
    public static class HCE_TICKET {
        public static final String SERVICE_ID_HCE_SERVICE = "100017";//HCE单程票
    }

    /**
     * NFC充值
     */
    public static class NFC {

    }

    /**
     * 服务
     */
    public static class SP_SERVICE {
        public static final String SERVICE_ID_REAL_CARD = "100016";//长沙实体卡
        public static final String SERVICE_ID_COFFEE = "100018";//蜂窝咖啡
        public static final String SERVICE_ID_QR_CODE_TICKET = "100030";//二维码单程票
        public static final String SERVICE_ID_TICKET = "100008";//普通单程票

        public static final String INDEX_TRANSACTION_LIST = "trans_list";

    }

    /**
     * 单程票
     */
    public static class SINGLE_TICKET {
        public static final String KEY_CURRENT_SINGLE_TICKET_STATE = "single_ticket_status";

        public static final String CITY_CODE = "cityCode";
        public static final String MAP_VERSION = "map_version";

        public static final String CITY_CODE_GUANGZHOU = "4401";
        public static final String CITY_CODE_CHANGSHA = "4100";
        public static final String MAP_TILES_VERSION_GUANGZHOU = "1.0.3";

        public static final String TICKET_TYPE_COMMON_COMPLETE_STATION = "0";//普通单程票 有起点站和终点站
        public static final String TICKET_TYPE_COMMON_ONLY_START_STATION = "1";//普通单程票 只有起点站
        public static final String TICKET_TYPE_QR_CODE_COMPLETE_STATION = "2";//电子单程票 有起点站和终点站
        public static final String TICKET_TYPE_QR_CODE_ONLY_START_STATION = "3";//电子单程票 只有起点站
        public static final String TICKET_TYPE_QR_CODE_REPAY_TICKET = "4";//电子单程票 补票

        public static final int GATE_STATUS_ENTRY = 1;//进站
        public static final int GATE_STATUS_EXIT = 2;//出站

        public static final String QR_CODE_STATUS_ENTRYED = "04";//已进站
        public static final String QR_CODE_STATUS_EXITED = "05";//已出站

        public static final UUID UUID_SERVICE_ENTRY_GATE = UUID.fromString("000001FA-0000-1000-8000-00805f9b34fb");
        public static final UUID UUID_SERVICE_EXIT_GATE = UUID.fromString("000001FB-0000-1000-8000-00805f9b34fb");
        public static final UUID UUID_CHARACTERISTIC_DATA_SHARE = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb");//用于广播设备编号

        public static final String URL_NTP_SERVER = "120.52.9.10";

    }

    /**
     * 订单列表
     */
    public static class ORDER {
        public static final int ORDER_SINGLETICKET = 0;
        public static final int ORDER_CHANGSHA_TICKET = 1;
        public static final int ORDER_BRACELET = 2;
        public static final int ORDER_REAL_CARD = 3;

        public static final int COM_STATUS_ALL = 0;//全部
        public static final int COM_STATUS_UN_PAY = 1;//待支付
        public static final int COM_STATUS_UN_FINISH = 2;//未完成
        public static final int COM_STATUS_FINISH = 3;//已完成

        public static final String CATEGORYCODE_REALCARD = "CSSTK";//长沙实体卡
        public static final String CATEGORYCODE_BRACELET = "CSSH";//长沙手环
        public static final String CATEGORYCODE_CS_METRO_CARD = "CSDT";//长沙地铁卡
        public static final String CATEGORYCODE_SINGLETICKET = "DCP";//单程票
        public static final String CATEGORYCODE_QRCODE_TICKET = "DCP_QR";//二维码单程票
        public static final String CATEGORYCODE_COFFEE = "COFFEE";//风格咖啡
        public static final String CATEGORYCODE_ALL = "ALL";//全部

        //        public static final int STATUS_CS_METRO_CARD_ALL = 0;
        public static final int STATUS_CS_METRO_CARD_PRE_PAY = 1;//待支付
        public static final int STATUS_CS_METRO_CARD_RE_WRITE_CARD = 2;//补充值/已支付
        public static final int STATUS_CS_METRO_CARD_RECHARGE_SUCCESS = 5;//充值成功
        public static final int STATUS_CS_METRO_CARD_RECHARGING = 4;//充值中
        public static final int STATUS_CS_METRO_CARD_REFUNDED = 3;//退款
        public static final int STATUS_CS_METRO_CARD_INVALID = 99;

        public static final int STATUS_ALL = 0;//全部订单
        public static final int STATUS_SINGLE_TICKET_WITHOUT_PAY = 1;//未支付订单
        public static final int STATUS_SINGLE_TICKET_PREGET_TICKET = 2;//待取票订单
        public static final int STATUS_SINGLE_TICKET_REFUND = 3;//已退票订单
        public static final int STATUS_SINGLE_TICKET_COMPLETE = 5;//已取票订单
        public static final int STATUS_SINGLE_TICKET_CANCELED = 6;//已取消订单
        public static final int STATUS_SINGLE_TICKET_PAY_SUCCESS = 999;//已支付成功
        public static final int STATUS_SINGLE_TICKET_REFUNDING = 7;//退款中
        public static final int STATUS_SINGLE_TICKET_CGCOMPLETE = 9;//已完成
        public static final int STATUS_SINGLE_TICKET_ERROR = 12;//异常订单

        public static final int STATUS_QRCODE_TICKET_WITHOUT_PAY = 1;//未支付
        public static final int STATUS_QRCODE_TICKET_PRE_ENTRY = 2;//待进站
        public static final int STATUS_QRCODE_TICKET_REFUND = 3;//已退票
        public static final int STATUS_QRCODE_TICKET_CANCELED = 6;//已取消
        public static final int STATUS_QRCODE_TICKET_PRE_EXIT = 10;//待出站
        public static final int STATUS_QRCODE_TICKET_ALREADY_EXIT = 11;//已出站
        public static final int STATUS_QRCODE_TICKET_ERROR = 12;//异常订单(订单列表显示其他)
        //        public static final int STATUS_QRCODE_TICKET_ENTRY_PAY = 13;//进站补票
        public static final int STATUS_QRCODE_TICKET_EXIT_PAY = 14;//出站补票
        public static final int STATUS_QRCODE_TICKET_INVALID = 15;//已失效
        public static final int STATUS_QRCODE_TICKET_DUPLICATE_ENTRY = 16;//重复进站

        public static final int BTN_SINGLE_TICKET_TAKE_TICKET = 1;//取票
        public static final int BTN_SINGLE_TICKET_REFUND = 2;//退款
        public static final int BTN_SINGLE_TICKET_PAY_ORDER = 3;//支付订单
        public static final int BTN_SINGLE_TICKET_CANCEL_ORDER = 4;//取消订单
        public static final int BTN_SINGLE_TICKET_BUY_AGAIN = 5;//再次购买
        public static final int BTN_EXIT_GATE = 7;//出站

        public static final int STATUS_COFFEE_UN_PAY = 1;//未支付
        public static final int STATUS_COFFEE_PAY_SUCCESS = 2;//支付成功
        public static final int STATUS_COFFEE_PAY_FAILED = 3;//支付失败
        public static final int STATUS_COFFEE_COOKED = 4;//已制作
        public static final int STATUS_COFFEE_COMPLETED = 5;//已取餐
        public static final int STATUS_COFFEE_REFUNDED = 6;//已退款
        public static final int STATUS_COFFEE_CLOSED = 7;//已关闭
        public static final int STATUS_COFFEE_ERROR = 8;//异常订单

    }

    /**
     * 推送
     */
    public static class PUSH {
        public static final String PUSH_MESSAGE = "push_message";

        public static final String TYPE_INBOX = "01";
        public static final String TYPE_URL = "02";
    }

    /**
     * 收件箱
     */
    public static class INBOX {
        public static final String MESSAGE_LIST = "list";
        public static final String POSITON = "position";
        public static final String KEY_MESSAGE = "message";
    }

    /**
     * 论坛
     */
    public static class BBS {
        public static final int IMG_MAX_NUM = 3;
        public static final String BBS_TITLE = "title";
        public static final String BBS_FORUM_ID = "forumId";
        public static final String ISFORUMADMINYN = "isForumAdminYN";
        public static final String TOPICINFO = "TopicInfo";
        public static final String CATEGORY = "category";

        public static final int TOPIC_CATEGORY_NEW_REPLY = 1;//最新回复
        public static final int TOPIC_CATEGORY_NEW_PUBLISH = 3;//最新发布
        public static final int TOPIC_CATEGORY_ESSENCE = 2;//精华

        public static final int TOPIC_CATEGORY_MY_POST = 3;//我发表的
        public static final int TOPIC_CATEGORY_MY_REPLY = 1;//我回复的

        public static final String KEY_PHOTO_LIST = "photo_list";
        public static final String KEY_IMAGE_ITEM = "image_item";
        public static final String KEY_IMAGE_POSITION = "image_position";
        public static final String KEY_SELECTED_PHOTO_LIST = "selected_photo_list";
        public static final String KEY_BEFORE_SELECTED_PHOTO_LIST = "before_selected_photo_list";

        public static final String KEY_LAUNCH_PREVIEW_FLAG = "launch_preview_flag";
        public static final String KEY_MAX_SELECTABLE_COUNT = "max_selectable_count";

        public static final int KEY_LAUNCH_ALBUM_FLAG_POST_DETAIL = 1;//回帖详情页面查看图片
        public static final int KEY_LAUNCH_ALBUM_FLAG_PUBLIST_TOPIC = 2;//发帖页面查看图片

        public static final String VIEW_TYPE_ALL = "1";//全部
        public static final String VIEW_TYPE_POSTER = "2";//查看楼主

    }

    /*消息*/
    public static class MESSAGE {
        public static final String MESSAGE_EVENTURL = "message_eventUrl";
        public static final String MESSAGE_READYN = "message_readYn";
        public static final String MESSAGE_MESSAGEID = "message_messageid";
        public static final String MESSAGE_CONTENT = "message_content";
        public static final String MESSAGE_INBOX_TWO = "2";//纯文字 app原生展示
        public static final String MESSAGE_WORDS_ONE = "1";//纯文字 网页展示
        public static final String MESSAGE_PICTURE_ZERO = "0";//有图
    }

}
