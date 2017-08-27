/**
 *
 */
package com.whl.client.home.ticket.distance;

import com.whl.framework.utils.MLog;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


/**
 * desc：利用Dijkstra算法
 *
 * @author chaisson
 * @since 2015-5-31 上午9:43:38
 */
public class Subway {
    private static final String TAG = "Subway";
    private List<CssStation> outList = new ArrayList<CssStation>();//记录已经分析过的站点
    OnGetShortPassListener mListener;

    public Subway(OnGetShortPassListener listener) {
        mListener = listener;
    }

    //计算从s1站到s2站的最短经过路径
    public void calculate(CssStation s1, CssStation s2) {
        if (outList.size() == DataBuilder.totalStation) {
            MLog.d(TAG, "1 找到目标站点：" + s2.getName() + "，共经过" + (s1.getAllPassedStations(s2).size() - 1) + "站");
            List<CssStation> passedStationList = new ArrayList<>();
            for (CssStation station : s1.getAllPassedStations(s2)) {
                //                MLog.d(TAG, station.getName() + "->");
                passedStationList.add(station);
            }
            if (mListener != null) {
                mListener.onGetShortPass(passedStationList);
            }
            return;
        }
        if (!outList.contains(s1)) {
            outList.add(s1);
        }
        //如果起点站的OrderSetMap为空，则第一次用起点站的前后站点初始化之
        if (s1.getOrderSetMap().isEmpty()) {
            List<CssStation> LinkedStations = getAllLinkedStations(s1);
            for (CssStation s : LinkedStations) {
                s1.getAllPassedStations(s).add(s);
            }
        }
        CssStation parent = getShortestPath(s1);//获取距离起点站s1最近的一个站（有多个的话，随意取一个）
        if (parent == s2) {
            MLog.d(TAG, "2 找到目标站点：" + s2 + "，共经过" + (s1.getAllPassedStations(s2).size() - 1) + "站");
            List<CssStation> passedStationList = new ArrayList<>();
            for (CssStation station : s1.getAllPassedStations(s2)) {
                //                System.out.print(station.getName() + "->");
                //                MLog.d(TAG, station.getName() + "->");
                passedStationList.add(station);
            }
            if (mListener != null) {
                mListener.onGetShortPass(passedStationList);
            }
            return;
        }
        for (CssStation child : getAllLinkedStations(parent)) {
            if (outList.contains(child)) {
                continue;
            }
            int shortestPath = (s1.getAllPassedStations(parent).size() - 1) + 1;//前面这个1表示计算路径需要去除自身站点，后面这个1表示增加了1站距离
            if (s1.getAllPassedStations(child).contains(child)) {
                //如果s1已经计算过到此child的经过距离，那么比较出最小的距离
                if ((s1.getAllPassedStations(child).size() - 1) > shortestPath) {
                    //重置S1到周围各站的最小路径
                    s1.getAllPassedStations(child).clear();
                    s1.getAllPassedStations(child).addAll(s1.getAllPassedStations(parent));
                    s1.getAllPassedStations(child).add(child);
                }
            } else {
                //如果s1还没有计算过到此child的经过距离
                s1.getAllPassedStations(child).addAll(s1.getAllPassedStations(parent));
                s1.getAllPassedStations(child).add(child);
            }
        }
        outList.add(parent);
        calculate(s1, s2);//重复计算，往外面站点扩展
        return;
    }

    //取参数station到各个站的最短距离，相隔1站，距离为1，依次类推
    private CssStation getShortestPath(CssStation station) {
        int minPatn = Integer.MAX_VALUE;
        CssStation rets = null;
        for (CssStation s : station.getOrderSetMap().keySet()) {
            if (outList.contains(s)) {
                continue;
            }
            LinkedHashSet<CssStation> set = station.getAllPassedStations(s);//参数station到s所经过的所有站点的集合
            if (set.size() < minPatn) {
                minPatn = set.size();
                rets = s;
            }
        }
        return rets;
    }

    //获取参数station直接相连的所有站，包括交叉线上面的站
    private List<CssStation> getAllLinkedStations(CssStation station) {
        List<CssStation> linkedStations = new ArrayList<CssStation>();
        for (List<CssStation> line : DataBuilder.lineSet) {
            if (line.contains(station)) {//如果某一条线包含了此站，注意由于重写了hashcode方法，只有name相同，即认为是同一个对象
                CssStation s = line.get(line.indexOf(station));
                if (s.prev != null) {
                    linkedStations.add(s.prev);
                }
                if (s.next != null) {
                    linkedStations.add(s.next);
                }
            }
        }

        return linkedStations;
    }

    public interface OnGetShortPassListener {
        void onGetShortPass(List<CssStation> stationList);
    }


    /**
     * desc: How to use the method
     * author chaisson
     * since 2015-5-31
     * version 1.0
     */
    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        Subway sw = new Subway(new OnGetShortPassListener() {
            @Override
            public void onGetShortPass(List<CssStation> stationList) {

            }
        });
        //        sw.calculate(new CssStation("迈皋桥站"), new CssStation("奥体东站"));
        long t2 = System.currentTimeMillis();
        System.out.println();
        System.out.println("耗时：" + (t2 - t1) + "ms");
    }
}

