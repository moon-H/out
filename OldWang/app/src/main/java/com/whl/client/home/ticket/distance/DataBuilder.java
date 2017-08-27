/**
 *
 */
package com.whl.client.home.ticket.distance;

import com.whl.framework.utils.MLog;
import com.whl.client.db.SingleTicketDBManager;
import com.whl.client.gateway.model.singleticket.LineCode;
import com.whl.client.gateway.model.singleticket.StationCode;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * desc:构造数据关系
 *
 * @author chaisson
 * @since 2015-5-31 上午10:23:52
 */
public class DataBuilder {
    public static final String TAG = "DataBuilder";

    //    public static List<CssStation> line1 = new ArrayList<CssStation>();//1号线
    //    public static List<CssStation> line2 = new ArrayList<CssStation>();//2号线
    //    public static List<CssStation> line3 = new ArrayList<CssStation>();//3号线
    //    public static List<CssStation> line10 = new ArrayList<CssStation>();//10号线
    //    public static List<CssStation> lineS1 = new ArrayList<CssStation>();//s1号线
    //    public static List<CssStation> lineS8 = new ArrayList<CssStation>();//s8号线
    public static Set<List<CssStation>> lineSet = new HashSet<List<CssStation>>();//所有线集合
    public static int totalStation = 0;//总的站点数量

    static {
        long t1 = System.currentTimeMillis();
        MLog.d(TAG, "cache data start....");
        List<LineCode> lineList = DataSupport.findAll(LineCode.class);
        if (lineList != null && lineList.size() > 0) {
            for (LineCode lineCode : lineList) {
                List<CssStation> stationList = new ArrayList<>();
                List<StationCode> orgStationList = SingleTicketDBManager.getStationCodeListByLineCode(lineCode.getLineCode());
                for (StationCode stationCode : orgStationList) {
                    stationList.add(new CssStation(stationCode));
                    //                    MLog.d(TAG, "## line = " + lineCode.getLineCode() + "  stationName = " + stationCode.getStationNameZH());
                }
                for (int i = 0; i < stationList.size(); i++) {
                    if (i < stationList.size() - 1) {
                        stationList.get(i).next = stationList.get(i + 1);
                        stationList.get(i + 1).prev = stationList.get(i);
                    }
                }
                totalStation += stationList.size();
                //                MLog.d(TAG, "--------line = " + lineCode.getLineNameZH() + "  station size = " + stationList.size());
                lineSet.add(stationList);
            }
        }
        long t2 = System.currentTimeMillis();
        MLog.d(TAG, "cache data end...." + (t2 - t1) + "ms");
        MLog.d(TAG, "total station= " + totalStation);


        //        //1号线
        //        String line1Str = "迈皋桥站、红山动物园站、南京站、新模范马路站、玄武门站、鼓楼站、珠江路站、新街口站、张府园站、三山街站、中华门站、安德门站、天隆寺站、软件大道站、花神庙站、南京南站、双龙大道站、河定桥站、胜太路站、百家湖站、小龙湾站、竹山路站、天印大道站、龙眠大道站、南医大·江苏经贸学院站、南京交院站、中国药科大学站";
        //        String[] line1Arr = line1Str.split("、");
        //        for (String s : line1Arr) {
        //            line1.add(new CssStation(s));
        //        }
        //        for (int i = 0; i < line1.size(); i++) {
        //            if (i < line1.size() - 1) {
        //                line1.get(i).next = line1.get(i + 1);
        //                line1.get(i + 1).prev = line1.get(i);
        //            }
        //        }

        /*******************************************************************************/
        //2号线
        //        String line2Str = "油坊桥站、雨润大街站、元通站、奥体东站、兴隆大街站、集庆门大街站、云锦路站、莫愁湖站、汉中门站、上海路站、新街口站、大行宫站、西安门站、明故宫站、苜蓿园站、下马坊站、孝陵卫站、钟灵街站、马群站、金马路站、仙鹤门站、学则路站、仙林中心站、羊山公园站、南大仙林校区站、经天路站";
        //        String[] line2Arr = line2Str.split("、");
        //        for (String s : line2Arr) {
        //            line2.add(new CssStation(s));
        //        }
        //        for (int i = 0; i < line2.size(); i++) {
        //            if (i < line2.size() - 1) {
        //                line2.get(i).next = line2.get(i + 1);
        //                line2.get(i + 1).prev = line2.get(i);
        //            }
        //        }

        /*******************************************************************************/
        //3号线
        //        String line3Str = "林场站、星火路站、东大成贤学院站、泰冯路站、天润城站、柳洲东路站、上元门站、五塘广场站、小市站、南京站、南京林业大学·新庄站、鸡鸣寺站、浮桥站、大行宫站、常府街站、夫子庙站、武定门站、雨花门站、卡子门站、大明路站、明发广场站、南京南站、宏运大道站、胜太西路站、天元西路站、九龙湖站、诚信大道站、东大九龙湖校区站、秣周东路站";
        //        String[] line3Arr = line3Str.split("、");
        //        for (String s : line3Arr) {
        //            line3.add(new CssStation(s));
        //        }
        //        for (int i = 0; i < line3.size(); i++) {
        //            if (i < line3.size() - 1) {
        //                line3.get(i).next = line3.get(i + 1);
        //                line3.get(i + 1).prev = line3.get(i);
        //            }
        //        }

        /*******************************************************************************/
        //10号线
        //        String line10Str = "雨山路站、文德路站、龙华路站、南京工业大学站、浦口万汇城站、临江站、江心洲站、绿博园站、梦都大街站、奥体中心站、元通站、中胜站、小行站、安德门站";
        //        String[] line10Arr = line10Str.split("、");
        //        for (String s : line10Arr) {
        //            line10.add(new CssStation(s));
        //        }
        //        for (int i = 0; i < line10.size(); i++) {
        //            if (i < line10.size() - 1) {
        //                line10.get(i).next = line10.get(i + 1);
        //                line10.get(i + 1).prev = line10.get(i);
        //            }
        //        }

        /*******************************************************************************/
        //s1号线
        //        String lineS1Str = "南京南站、翠屏山站、河海大学·佛城西路站、吉印大道站、正方中路站、翔宇路北站、翔宇路南站、禄口机场站";
        //        String[] lineS1Arr = lineS1Str.split("、");
        //        for (String s : lineS1Arr) {
        //            lineS1.add(new CssStation(s));
        //        }
        //        for (int i = 0; i < lineS1.size(); i++) {
        //            if (i < lineS1.size() - 1) {
        //                lineS1.get(i).next = lineS1.get(i + 1);
        //                lineS1.get(i + 1).prev = lineS1.get(i);
        //            }
        //        }

        /*******************************************************************************/
        //s8号线
        //        String lineS8Str = "泰山新村站、泰冯路站、高新开发区站、信息工程大学站、卸甲甸站、大厂站、葛塘站、长芦站、化工园站、六合开发区站、龙池站、雄州站、凤凰山公园站、方州广场站、沈桥站、八百桥站、金牛湖站";
        //        String[] lineS8Arr = lineS8Str.split("、");
        //        for (String s : lineS8Arr) {
        //            lineS8.add(new CssStation(s));
        //        }
        //        for (int i = 0; i < lineS8.size(); i++) {
        //            if (i < lineS8.size() - 1) {
        //                lineS8.get(i).next = lineS8.get(i + 1);
        //                lineS8.get(i + 1).prev = lineS8.get(i);
        //            }
        //        }

        //        lineSet.add(line1);
        //        lineSet.add(line2);
        //        lineSet.add(line3);
        //        lineSet.add(line10);
        //        lineSet.add(lineS1);
        //        lineSet.add(lineS8);
        //        totalStation = line1.size() + line2.size() + line3.size() + line10.size() + lineS1.size() + lineS8.size();
        //        System.out.println("总的站点数量：" + totalStation);
        //        MLog.d(TAG,"");
    }
}
