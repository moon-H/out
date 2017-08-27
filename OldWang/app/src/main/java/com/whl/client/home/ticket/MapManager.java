package com.whl.client.home.ticket;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.LruCache;

import com.whl.framework.downloadlibrary.DownloadConstants;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.app.BizApplication;
import com.whl.client.bdlocationservice.ThirdMapService;
import com.whl.client.db.SingleTicketDBManager;
import com.whl.client.gateway.model.singleticket.StationCode;
import com.whl.client.gateway.model.singleticket.StationRect;
import com.whl.client.preference.MPreference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import libcore.io.DiskLruCache;

/**
 * Created by liwx on 2015/10/17.
 */
public class MapManager {
    private static final String TAG = "MapManager";
    public static final int TILE_WIDTH = 256;
    public static final int TILE_HEIGHT = 256;
    private static final String CACHE_DIR = "tiles";

    private Context context;

    private static final int DOT_WIDTH = 60;
    private static final int DOT_HEIGHT = 60;
    public static final float INIT_SCALE = 0.8f;

    private static final int MAP_WIDTH = 4200;
    private static final int MAP_HEIGHT = 3800;
    private LruCache<String, Bitmap> mMapTileCache;
    private DiskLruCache mDiskLruCache;
    private SingleTicketDBManager mSingleTicketDBManager;

    private ArrayList<StationRect> stationsArrayList = new ArrayList<>();
    private List<StationCode> tempStationCodeList = new ArrayList<>();


    private int horizontalCount;
    private int verticalCount;
    private int mapWidth;
    private int mapHeight;
    private String cityCode;
    private String mParentCachDir;
    private String mTilesCachDir;


    public MapManager(Context context) {
        this.context = context;
        mSingleTicketDBManager = new SingleTicketDBManager(context);
        mParentCachDir = DownloadConstants.getMetroCacheDir(context).getPath();
        MLog.d(TAG, "mParentCacheDir = " + mParentCachDir);
    }

    public void calculateTilesCountAndCache(int mapWidth, int mapHeight, String cityCode) {
        MLog.d(TAG, "mapWidth = " + mapWidth + " mapHeight = " + mapHeight + " cityCode = " + cityCode);
        mTilesCachDir = mParentCachDir + File.separator + cityCode + File.separator + CACHE_DIR;
        MLog.d(TAG, "mTilesCacheDir = " + mTilesCachDir);

        setMapWidth(mapWidth);
        setMapHeight(mapHeight);
        setCityCode(cityCode);
        if (mapWidth % MapManager.TILE_WIDTH == 0)
            this.horizontalCount = mapWidth / MapManager.TILE_WIDTH;
        else
            this.horizontalCount = mapWidth / MapManager.TILE_WIDTH + 1;

        MLog.d(TAG, " horizontalCount = " + horizontalCount);

        if (mapHeight % MapManager.TILE_HEIGHT == 0)
            this.verticalCount = mapHeight / MapManager.TILE_HEIGHT;
        else
            this.verticalCount = mapHeight / MapManager.TILE_HEIGHT + 1;

        MLog.d(TAG, " verticalCount = " + verticalCount);
        //        cacheMapTiles(cityCode, verticalCount, horizontalCount);
    }

    public Bitmap getMapTiles(String name) {
        //        MLog.d(TAG, "name = " + name);
        Bitmap bitmap = null;
        //        if (mMapTileCache != null) {
        //            bitmap = mMapTileCache.get(name);
        //        }
        //        if (bitmap == null) {
        if (MPreference.getBoolean(context, MPreference.MAP_GET_FROM_DISK, false)) {
            //                MLog.d(TAG, "get tiles from disk :: " + mTilesCachDir);
            bitmap = Utils.getBitmapFromDiskCach(mTilesCachDir, name);
        } else {
            //                MLog.d(TAG, "get tiles from assets:: " + mTilesCachDir);
            mTilesCachDir = "metro" + File.separator + cityCode + File.separator + "tiles" + File.separator;
            bitmap = Utils.getBitmapFromAsset(context, mTilesCachDir + name);
            //            }

            //            cacheMapTile(name, bitmap);
            //        } else {
            //get bitmap from cache
            //            MLog.d(TAG, "get bitmap from cache");
        }
        return bitmap;
    }

    private void cacheMapTiles(String cityCode, int verticalCount, int horizontalCount) {
        //        clearMemoryCache();
        int v = verticalCount + 1;
        int h = horizontalCount + 1;
        for (int i = 1; i < v; i++) {
            for (int j = 1; j < h; j++) {
                String fileName = i + "_" + j + ".png";
                cacheMapTile(fileName, Utils.getBitmapFromDiskCach(mTilesCachDir, fileName));
            }
        }
    }

    //    public void cacheMapTiles() {
    //        for (int i = 1; i < 16; i++) {
    //            for (int j = 1; j < 18; j++) {
    //                String fileName = i + "_" + j + ".png";
    //                cacheMapTile(fileName, Utils.getBitmapFromAsset(context, fileName));
    //            }
    //        }
    //    }

    private void cacheMapTile(String key, Bitmap bmp) {
        if (mMapTileCache == null)
            mMapTileCache = new LruCache<>(BizApplication.getInstance().getMaxMemory() / 4);
        if (bmp != null) {
            mMapTileCache.remove(key);
            mMapTileCache.put(key, bmp);
        }
    }

    public void clearMemoryCache() {
        if (mMapTileCache != null)
            mMapTileCache.evictAll();
        System.gc();
        System.runFinalization();
    }

    public void clearCachedStationInfo() {
        if (stationsArrayList != null) {
            stationsArrayList.clear();
        }
    }

    public void cacheStationsRect() {
        MLog.d(TAG, "cacheStationsRect--------start-------");
        stationsArrayList.clear();
        List<StationCode> list = SingleTicketDBManager.getAllStationCode();
        if (list != null && list.size() > 0) {
            Rect btnRect;
            Rect dotRect;
            for (StationCode st : list) {
                btnRect = new Rect((int) st.getBtnOriginX(), (int) st.getBtnOriginY(), (int) st.getBtnOriginX() + (int) st.getBtnWidth(), +(int) st.getBtnOriginY() + (int) st.getBtnHeight());
                dotRect = new Rect((int) st.getDotX() - DOT_WIDTH, (int) st.getDotY() - DOT_HEIGHT, (int) st.getDotX() + DOT_WIDTH, (int) st.getDotY() + DOT_HEIGHT);
                StationRect stationRect = new StationRect();
                stationRect.setBtnRect(btnRect);
                stationRect.setDotRect(dotRect);
                stationRect.setStationCode(st);
                stationsArrayList.add(stationRect);
            }
        }
    }

    public void getNearestStation(final double longitude, final double latitude, final OnGetNearestStationCallback callback) {
        BizApplication.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<StationCode> list = SingleTicketDBManager.getAllStationCode();
                int position = 0;
                float distance = 0;
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        //                        MLog.d(TAG, "name = " + list.get(i).getStationNameZH() + " " + list.get(i).getStationLat() + " " + list.get(i).getStationLong());
                        float tempDistance = ThirdMapService.getInstance(context).getDistance(latitude, longitude, list.get(i).getStationLat(), list.get(i).getStationLong());
                        //                        MLog.d(TAG, "## DISTANCE  = " + tempDistance);
                        if (i == 0) {
                            distance = tempDistance;
                        }
                        if (distance > tempDistance) {
                            distance = tempDistance;
                            position = i;
                        }
                    }
                    if (callback != null) {
                        callback.onCalculateComplete(list.get(position));
                    }
                } else {
                    if (callback != null) {
                        callback.onCalculateComplete(null);
                    }
                }
            }
        });
    }

    interface OnGetNearestStationCallback {
        /**
         * 回调在子线程
         */
        void onCalculateComplete(StationCode stationCode);
    }

    //    public StationCode getClickedStation(int x, int y) {
    //        MLog.d(TAG, " getClickedStation 1 ");
    //        for (StationRect stationRect : stationsArrayList) {
    //            if (stationRect.getBtnRect().contains(x, y) || stationRect.getDotRect().contains(x, y)) {
    //                MLog.d(TAG, " getClickedStation 2 ");
    //                return stationRect.getStationCode();
    //            }
    //        }
    //        return null;
    //    }

    public List<StationCode> getClickedStation(int x, int y) {
        //        MLog.d(TAG,"stationsArrayList size = "+stationsArrayList.size());
        //        for (StationRect rect:stationsArrayList){
        //            MLog.d(TAG," rect = "+rect.toString());
        //        }
        tempStationCodeList.clear();
        for (StationRect stationRect : stationsArrayList) {
            if (stationRect.getBtnRect().contains(x, y) || stationRect.getDotRect().contains(x, y)) {
                StationCode stationCode = stationRect.getStationCode();
                if (stationCode.getTransferYn().equalsIgnoreCase("Y")) {
                    return mSingleTicketDBManager.getStationCodeByName(stationCode.getStationNameZH().trim());
                } else {
                    tempStationCodeList.add(stationCode);
                    return tempStationCodeList;
                }
            }
        }
        return tempStationCodeList;
    }

    public boolean isContainToAirportLine(List<StationCode> list) {
        for (StationCode stationCode : list) {
            if (stationCode.getToAirportYn().equalsIgnoreCase("Y"))
                return true;
        }
        return false;
    }

    public int getVerticalCount() {
        return verticalCount;
    }

    public void setVerticalCount(int verticalCount) {
        this.verticalCount = verticalCount;
    }

    public int getHorizontalCount() {
        return horizontalCount;
    }

    public void setHorizontalCount(int horizontalCount) {
        this.horizontalCount = horizontalCount;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    String[] stationName = {"广州东站", "体育中心      ", "体育西路      ", "杨箕          ", "东山口        ", "烈士陵园      ", "农讲所        ", "公园前        ", "西门口        ", "陈家祠        ", "长寿路        ", "黄沙          ", "芳村          ", "花地湾        ", "坑口          ", "西朗          ", "嘉禾望岗      ", "黄边          ", "江夏          ", "萧岗          ", "白云文化广场  ", "白云公园      ", "飞翔公园      ", "三元里        ", "广州火车站    ", "越秀公园      ", "纪念堂        ", "海珠广场      ", "市二宫        ", "江南西        ", "昌岗          ", "江泰路        ", "东晓南        ", "南洲          ", "洛溪          ", "南浦          ", "会江          ", "石壁          ", "广州南站      ", "番禺广场      ", "市桥          ", "汉溪长隆      ", "大石          ", "厦滘          ", "沥滘          ", "大塘          ", "客村          ", "广州塔        ", "珠江新城      ", "石牌桥        ", "岗顶          ", "华师          ", "五山          ", "天河客运站    ", "林和西        ", "燕塘          ", "梅花园        ", "京溪南方医院  ", "同和          ", "永泰          ", "白云大道北    ", "龙归          ", "人和          ", "机场南        ", "黄村          ", "车陂          ", "车陂南        ", "万胜围        ", "官洲          ", "大学城北      ", "大学城南      ", "新造          ", "石碁          ", "海傍          ", "低涌          ", "东涌          ", "黄阁汽车城    ", "黄阁          ", "蕉门          ", "金洲          ", "滘口          ", "坦尾          ", "中山八        ", "西场          ", "西村          ", "小北          ", "淘金          ", "区庄          ", "动物园        ", "五羊邨        ", "猎德          ", "潭村          ", "员村          ", "科韵路        ", "东圃          ", "三溪          ", "鱼珠          ", "大沙地        ", "大沙东        ", "文冲          ", "凤凰新村      ", "沙园          ", "宝岗大道      ", "晓港          ", "中大          ", "鹭江          ", "赤岗          ", "磨碟沙        ", "新港东        ", "琶洲          ", "体育中心南    ", "天河南        ", "黄埔大道      ", "妇儿中心      ", "花城大道      ", "大剧院        ", "海心沙        ", "菊树          ", "龙溪          ", "金融高新区    ", "千灯湖        ", "虫雷岗        ", "南桂路        ", "桂城          ", "朝安          ", "普君北路      ", "祖庙          ", "同济路        ", "季华园        ", "魁奇路        ", "浔峰岗        ", "横沙          ", "沙贝          ", "河沙          ", "如意坊        ", "文化公园      ", "北京路        ", "团一大广场    ", "东湖          ", "黄花岗        ", "沙河顶        ", "天平架        ", "长湴          ", "一德路        "};
    int[] stationId = {274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 396, 397, 398, 399, 400, 401, 402, 403, 1607, 1608, 1609, 1610, 1611, 1612, 1613, 1614, 1615, 1616, 1617, 1618, 1619, 2386};
    double[] dotX = {2428.0, 2678.0, 2428.0, 2199.0, 2065.0, 1919.0, 1771.0, 1634.0, 1496.0, 1326.0, 1208.0, 1208.0, 1208.0, 1208.0, 1208.0, 1208.0, 2028.0, 1959.0, 1889.0, 1820.0, 1748.0, 1672.0, 1633.0, 1633.0, 1633.0, 1634.0, 1634.0, 1633.0, 1633.0, 1633.0, 1633.0, 1633.0, 1634.0, 1634.0, 1634.0, 1634.0, 1634.0, 1634.0, 1634.0, 2689.0, 2518.0, 2429.0, 2429.0, 2429.0, 2428.0, 2428.0, 2429.0, 2444.0, 2429.0, 2724.0, 2838.0, 2910.0, 2949.0, 2949.0, 2443.0, 2428.0, 2388.0, 2311.0, 2237.0, 2163.0, 2089.0, 2028.0, 2028.0, 2028.0, 3344.0, 3231.0, 3120.0, 3116.0, 3116.0, 3116.0, 3116.0, 3117.0, 3116.0, 3116.0, 3117.0, 3117.0, 3329.0, 3478.0, 3611.0, 3744.0, 710.0, 905.0, 1046.0, 1148.0, 1251.0, 1774.0, 1907.0, 2064.0, 2199.0, 2258.0, 2682.0, 2795.0, 2900.0, 3008.0, 3291.0, 3420.0, 3549.0, 3677.0, 3801.0, 3927.0, 1358.0, 1423.0, 1490.0, 1840.0, 2021.0, 2195.0, 2558.0, 2695.0, 2831.0, 2963.0, 2458.0, 2457.0, 2458.0, 2458.0, 2458.0, 2459.0, 2458.0, 1091.0, 941.0, 829.0, 829.0, 829.0, 762.0, 633.0, 509.0, 384.0, 291.0, 290.0, 291.0, 291.0, 743.0, 743.0, 743.0, 743.0, 998.0, 1388.0, 1790.0, 1901.0, 2006.0, 2065.0, 2064.0, 2283.0, 3152.0, 1508.0};
    double[] dotY = {1547.0, 1630.0, 1900.0, 1901.0, 1901.0, 1901.0, 1901.0, 1901.0, 1901.0, 1901.0, 1981.0, 2099.0, 2220.0, 2342.0, 2462.0, 2579.0, 898.0, 965.0, 1034.0, 1104.0, 1177.0, 1252.0, 1366.0, 1472.0, 1559.0, 1666.0, 1777.0, 2099.0, 2206.0, 2303.0, 2423.0, 2514.0, 2633.0, 2746.0, 2855.0, 2964.0, 3077.0, 3191.0, 3302.0, 3186.0, 3185.0, 3093.0, 2984.0, 2817.0, 2669.0, 2561.0, 2424.0, 2341.0, 2088.0, 1901.0, 1861.0, 1787.0, 1550.0, 1345.0, 1674.0, 1345.0, 1256.0, 1179.0, 1104.0, 1031.0, 957.0, 807.0, 708.0, 513.0, 1857.0, 1972.0, 2087.0, 2423.0, 2489.0, 2587.0, 2678.0, 2776.0, 2959.0, 3058.0, 3149.0, 3241.0, 3329.0, 3329.0, 3329.0, 3329.0, 2167.0, 1969.0, 1829.0, 1725.0, 1622.0, 1559.0, 1559.0, 1559.0, 1717.0, 2088.0, 2088.0, 2087.0, 2088.0, 2087.0, 2087.0, 2088.0, 2087.0, 2087.0, 2088.0, 2087.0, 2270.0, 2336.0, 2404.0, 2423.0, 2423.0, 2423.0, 2423.0, 2423.0, 2423.0, 2423.0, 1749.0, 1830.0, 1954.0, 2033.0, 2133.0, 2207.0, 2278.0, 2579.0, 2579.0, 2644.0, 2739.0, 2835.0, 2919.0, 2918.0, 2918.0, 2918.0, 3005.0, 3088.0, 3172.0, 3262.0, 1280.0, 1421.0, 1550.0, 1678.0, 2061.0, 2099.0, 2099.0, 2099.0, 2100.0, 1486.0, 1415.0, 1344.0, 1344.0, 2100.0};
    double[] btnX = {2276.0, 2694.0, 2289.0, 2117.0, 1941.0, 1865.0, 1720.0, 1505.0, 1449.0, 1270.0, 1092.0, 1111.0, 1124.0, 1101.0, 1130.0, 1225.0, 1866.0, 1977.0, 1904.0, 1835.0, 1760.0, 1688.0, 1650.0, 1651.0, 1467.0, 1649.0, 1647.0, 1487.0, 1645.0, 1647.0, 1650.0, 1642.0, 1643.0, 1646.0, 1644.0, 1646.0, 1645.0, 1649.0, 1649.0, 2632.0, 2503.0, 2442.0, 2444.0, 2444.0, 2439.0, 2441.0, 2344.0, 2298.0, 2280.0, 2682.0, 2834.0, 2923.0, 2958.0, 2847.0, 2315.0, 2447.0, 2393.0, 2329.0, 2245.0, 2180.0, 2102.0, 2039.0, 2037.0, 2043.0, 3345.0, 3228.0, 3134.0, 2996.0, 3126.0, 3135.0, 3135.0, 3126.0, 3133.0, 3125.0, 3123.0, 3135.0, 3251.0, 3444.0, 3578.0, 3708.0, 719.0, 796.0, 1057.0, 1162.0, 1265.0, 1739.0, 1872.0, 1971.0, 2217.0, 2210.0, 2654.0, 2767.0, 2868.0, 2964.0, 3256.0, 3389.0, 3521.0, 3634.0, 3755.0, 3888.0, 1235.0, 1350.0, 1375.0, 1808.0, 1983.0, 2160.0, 2523.0, 2652.0, 2774.0, 2931.0, 2476.0, 2475.0, 2476.0, 2475.0, 2474.0, 2476.0, 2475.0, 1063.0, 911.0, 671.0, 726.0, 749.0, 718.0, 590.0, 477.0, 330.0, 220.0, 190.0, 184.0, 194.0, 760.0, 756.0, 755.0, 756.0, 906.0, 1333.0, 1750.0, 1833.0, 1972.0, 2081.0, 2079.0, 2237.0, 3110.0, 1460.0};
    double[] bntY = {1509.0, 1600.0, 1826.0, 1829.0, 1828.0, 1923.0, 1922.0, 1823.0, 1920.0, 1918.0, 1951.0, 2115.0, 2195.0, 2313.0, 2431.0, 2589.0, 844.0, 969.0, 1042.0, 1111.0, 1186.0, 1259.0, 1341.0, 1446.0, 1573.0, 1643.0, 1738.0, 2024.0, 2173.0, 2278.0, 2354.0, 2484.0, 2604.0, 2718.0, 2827.0, 2941.0, 3049.0, 3164.0, 3266.0, 3201.0, 3200.0, 3063.0, 2952.0, 2785.0, 2638.0, 2533.0, 2439.0, 2312.0, 2012.0, 1922.0, 1862.0, 1795.0, 1525.0, 1263.0, 1643.0, 1361.0, 1207.0, 1132.0, 1057.0, 985.0, 908.0, 779.0, 682.0, 486.0, 1864.0, 1979.0, 2105.0, 2348.0, 2459.0, 2556.0, 2646.0, 2748.0, 2934.0, 3029.0, 3116.0, 3217.0, 3346.0, 3349.0, 3349.0, 3350.0, 2174.0, 1938.0, 1843.0, 1742.0, 1637.0, 1566.0, 1569.0, 1575.0, 1689.0, 2104.0, 2108.0, 2108.0, 2109.0, 2109.0, 2096.0, 2095.0, 2108.0, 2106.0, 2107.0, 2104.0, 2282.0, 2352.0, 2417.0, 2434.0, 2441.0, 2434.0, 2440.0, 2441.0, 2443.0, 2443.0, 1724.0, 1807.0, 1927.0, 2000.0, 2105.0, 2180.0, 2254.0, 2589.0, 2597.0, 2616.0, 2715.0, 2803.0, 2940.0, 2935.0, 2940.0, 2939.0, 2975.0, 3062.0, 3142.0, 3241.0, 1254.0, 1393.0, 1523.0, 1646.0, 2064.0, 2029.0, 2029.0, 2123.0, 2030.0, 1460.0, 1389.0, 1365.0, 1354.0, 2118.0};
    double[] bntWidth = {130.0, 130.0, 124.0, 69.0, 105.0, 109.0, 99.0, 105.0, 92.0, 105.0, 99.0, 87.0, 75.0, 94.0, 62.0, 71.0, 138.0, 75.0, 71.0, 83.0, 177.0, 109.0, 116.0, 85.0, 155.0, 109.0, 85.0, 130.0, 87.0, 83.0, 101.0, 93.0, 107.0, 71.0, 59.0, 59.0, 68.0, 60.0, 117.0, 117.0, 71.0, 140.0, 62.0, 67.0, 63.0, 66.0, 71.0, 109.0, 133.0, 88.0, 92.0, 77.0, 76.0, 168.0, 90.0, 76.0, 97.0, 173.0, 71.0, 67.0, 142.0, 72.0, 59.0, 84.0, 80.0, 72.0, 93.0, 103.0, 83.0, 108.0, 109.0, 62.0, 61.0, 74.0, 83.0, 71.0, 150.0, 72.0, 65.0, 70.0, 81.0, 68.0, 88.0, 70.0, 57.0, 64.0, 68.0, 74.0, 80.0, 99.0, 56.0, 60.0, 63.0, 83.0, 74.0, 66.0, 55.0, 82.0, 92.0, 79.0, 127.0, 67.0, 110.0, 68.0, 76.0, 66.0, 68.0, 84.0, 112.0, 65.0, 138.0, 84.0, 111.0, 108.0, 120.0, 100.0, 85.0, 56.0, 61.0, 141.0, 88.0, 69.0, 85.0, 84.0, 64.0, 107.0, 58.0, 83.0, 93.0, 80.0, 93.0, 63.0, 65.0, 57.0, 83.0, 109.0, 81.0, 135.0, 67.0, 95.0, 83.0, 94.0, 84.0, 94.0};
    double[] bntHeight = {71.0, 57.0, 54.0, 57.0, 53.0, 50.0, 67.0, 59.0, 53.0, 56.0, 58.0, 55.0, 52.0, 56.0, 60.0, 60.0, 56.0, 52.0, 49.0, 52.0, 47.0, 49.0, 53.0, 52.0, 55.0, 45.0, 75.0, 54.0, 66.0, 52.0, 55.0, 56.0, 59.0, 51.0, 52.0, 51.0, 52.0, 48.0, 63.0, 58.0, 56.0, 58.0, 60.0, 60.0, 57.0, 54.0, 52.0, 57.0, 52.0, 51.0, 59.0, 55.0, 56.0, 56.0, 52.0, 56.0, 55.0, 48.0, 48.0, 48.0, 52.0, 57.0, 57.0, 53.0, 49.0, 48.0, 52.0, 56.0, 59.0, 62.0, 65.0, 57.0, 51.0, 54.0, 56.0, 51.0, 54.0, 50.0, 48.0, 48.0, 60.0, 57.0, 49.0, 51.0, 52.0, 53.0, 49.0, 52.0, 51.0, 52.0, 51.0, 48.0, 46.0, 46.0, 56.0, 57.0, 48.0, 49.0, 53.0, 52.0, 51.0, 46.0, 45.0, 52.0, 69.0, 52.0, 53.0, 51.0, 47.0, 47.0, 48.0, 42.0, 44.0, 57.0, 44.0, 44.0, 46.0, 51.0, 52.0, 51.0, 51.0, 56.0, 48.0, 53.0, 49.0, 50.0, 48.0, 51.0, 55.0, 48.0, 57.0, 62.0, 61.0, 68.0, 49.0, 50.0, 51.0, 50.0, 49.0, 55.0, 47.0, 51.0, 56.0, 66.0};
    String[] pinyin = {"guangzhoudongzhan                  ", "tiyuzhongxin                       ", "tiyuxilu                           ", "yangji                             ", "dongshankou                        ", "lieshilingyuan                     ", "nongjiangsuo                       ", "gongyuanqian                       ", "ximenkou                           ", "chenjiaci                          ", "changshoulu                        ", "huangsha                           ", "fangcun                            ", "huadiwan                           ", "kengkou                            ", "xilang                             ", "jiahewanggang                      ", "huangbian                          ", "jiangxia                           ", "xiaogang                           ", "baiyunwenhuaguangchang             ", "baiyungongyuan                     ", "feixianggongyuan                   ", "sanyuanli                          ", "guangzhouhuochezhan                ", "yuexiugongyuan                     ", "jiniantang                         ", "haizhuguangchang                   ", "shiergong                          ", "jiangnanxi                         ", "changgang                          ", "jiangtailu                         ", "dongxiaonan                        ", "nanzhou                            ", "luoxi                              ", "nanpu                              ", "huijiang                           ", "shibi                              ", "guangzhounanzhan                   ", "fanyuguangchang                    ", "shiqiao                            ", "hanxichanglong                     ", "dashi                              ", "xiajiao                            ", "lijiao                             ", "datang                             ", "kecun                              ", "guangzhouta                        ", "zhujiangxincheng                   ", "shipaiqiao                         ", "gangding                           ", "huashi                             ", "wushan                             ", "tianhekeyunzhan                    ", "linhexi                            ", "yantang                            ", "meihuayuan                         ", "jingxinanfangyiyuan                ", "tonghe                             ", "yongtai                            ", "baiyundadaobei                     ", "longgui                            ", "renhe                              ", "jichangnan                         ", "huangcun                           ", "chepo                              ", "cheponan                           ", "wanshengwei                        ", "guanzhou                           ", "daxuechengbei                      ", "daxuechengnan                      ", "xinzao                             ", "shiqi                              ", "haibang                            ", "diyong                             ", "dongyong                           ", "huanggeqichecheng                  ", "huangge                            ", "jiaomen                            ", "jinzhou                            ", "jiaokou                            ", "tanwei                             ", "zhongshanba                        ", "xichang                            ", "xicun                              ", "xiaobei                            ", "taojin                             ", "quzhuang                           ", "dongwuyuan                         ", "wuyangcun                          ", "liede                              ", "tancun                             ", "yuancun                            ", "keyunlu                            ", "dongpu                             ", "sanxi                              ", "yuzhu                              ", "dashadi                            ", "dashadong                          ", "wenchong                           ", "fenghuangxincun                    ", "shayuan                            ", "baogangdadao                       ", "xiaogang                           ", "zhongda                            ", "lujiang                            ", "chigang                            ", "modiesha                           ", "xingangdong                        ", "pazhou                             ", "tiyuzhongxinnan                    ", "tianhenan                          ", "huangpudadao                       ", "fuerzhongxin                       ", "huachengdadao                      ", "dajuyuan                           ", "haixinsha                          ", "jushu                              ", "longxi                             ", "jinronggaoxinqu                    ", "qiandenghu                         ", "leigang                            ", "nanguilu                           ", "guicheng                           ", "chaoan                             ", "pujunbeilu                         ", "zumiao                             ", "tongjilu                           ", "jihuayuan                          ", "kuiqilu                            ", "xunfenggang                        ", "hengsha                            ", "shabei                             ", "hesha                              ", "ruyifang                           ", "wenhuagongyuan                     ", "beijinglu                          ", "tuanyidaguangchang                 ", "donghu                             ", "huanghuagang                       ", "shaheding                          ", "tianpingjia                        ", "changban                           ", "yidelu                             "};

}
