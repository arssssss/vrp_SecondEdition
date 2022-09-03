package com.example.vrp_secondedition.util;

import com.example.vrp_secondedition.RequestType.PathPlan;
import com.example.vrp_secondedition.pojo.Order;
import com.example.vrp_secondedition.resultType.PlanResult;
import com.example.vrp_secondedition.util.tabusearch.Operator;
import com.example.vrp_secondedition.util.tabusearch.PathPoint;
import com.example.vrp_secondedition.util.tabusearch.TabuTable;
import com.example.vrp_secondedition.util.tabusearch.VrpResult;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class TabuSearch {
    //禁忌表
    private TabuTable Table;
    //最优解
    private VrpResult BestResult;
    //路径点集
    private PathPoint[] points;
    //路径长度表
    private double[][] map;
    //最大装载量
    private double capacity;
    //最大迭代次数
    private int iteration;

    //初始化,获得道路信息
    private TabuSearch(PathPoint[] points, double[][] map, double capacity) {
        this.points = points;
        this.map = map;
        this.capacity = capacity;
    }

    //运算
    private void solve() {
        int len = points.length, flag = 0;
        Table = new TabuTable();
        //优先队列比较解的质量
        Queue<int[]> queue = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return (int) (alldistance(o1) - alldistance(o2));
            }
        });
        //禁忌表长
        Table.setsize((int) (5 * Math.floor(Math.sqrt(len))));
        //迭代次数
        int k = 0;
        //1.给出初始解
        int[] r = new int[len - 1];
        for (int i = 0; i < len - 1; i++) {
            r[i] = i + 1;
        }
        BestResult = new VrpResult(r);
        int[] temp = BestResult.getList();
        //迭代
        while (k++ < len * 5) {
            queue.clear();
            //2.构造邻域
            for (int i = 0; i < k * 3; i++) {
                int[] a = Operator.opt_2(r);
                queue.add(a);
            }
            //3.选择最好解并更新禁忌表
            do {
                r = queue.poll();
            } while (Table.contains(new VrpResult(r)) && new Random().nextInt(2) < 1 && !queue.isEmpty());
            if (flag > len && !Table.isEmpty()) {
                r = temp;
                flag = 0;
            }
            flag++;
            if (alldistance(r) < alldistance(BestResult.getList())) {
                BestResult = new VrpResult(r);
                flag = 0;
            }
            Table.add(new VrpResult(r));
        }
    }

    //返回结果
//    public VrpResult getBestResult() {
//        return BestResult;
//    }

    //返回两点距离
    private double getdistance(int p1, int p2) {
        return map[p1][p2];
    }

    //返回解行驶的长度
    private double alldistance(int[] r) {
        double result = 0, cap = 0;
        for (int i = 0; i < r.length; i++) {
            if (i == 0) {
                result += getdistance(0, r[i]);
                cap += points[r[0]].getDemand();
            } else {
                if (cap + points[r[i]].getDemand() > capacity) {
                    result += getdistance(r[i - 1], 0);
                    result += getdistance(0, r[i]);
                    cap = points[r[i]].getDemand();
                } else {
                    result += getdistance(r[i - 1], r[i]);
                    cap += points[r[i]].getDemand();
                }
                if (i == r.length - 1) {
                    result += getdistance(r[i], 0);
                    cap = 0;
                }
            }
        }
        return result;
    }

    public PlanResult show() {
        int[] list = BestResult.getList();
        PlanResult result=new PlanResult();
        result.totalDistance=alldistance(list);
        result.center.address=points[0].getAddress();
        for (int i = 0, sum = 0; i < list.length; i++) {
            if (sum == 0)
                result.orders.add(new ArrayList<>());
            if (sum + points[list[i]].getDemand() > capacity) {
                result.orders.add(new ArrayList<>());
                sum = 0;
            }
            Order order=new Order();
            order.id=points[list[i]].getId();
            order.address=points[list[i]].getAddress();
            result.orders.get(result.orders.size()-1).add(order);
            sum += points[list[i]].getDemand();
        }
        return result;
    }

    //
    public List<Integer[]> show2() {
        List<Integer[]> result = new ArrayList<>();
        double cap = 0;
        for (int i = 0; i < BestResult.getList().length; i++) {
            if (i == 0) {
                result.add(new Integer[]{0, BestResult.getList()[i]});
                cap += points[BestResult.getList()[0]].getDemand();
            } else {
                if (cap + points[BestResult.getList()[i]].getDemand() > capacity) {
                    result.add(new Integer[]{BestResult.getList()[i - 1], 0});
                    result.add(new Integer[]{0, BestResult.getList()[i]});
                    cap = points[BestResult.getList()[i]].getDemand();
                } else {
                    result.add(new Integer[]{BestResult.getList()[i - 1], BestResult.getList()[i]});
                    cap += points[BestResult.getList()[i]].getDemand();
                }
                if (i == BestResult.getList().length - 1) {
                    result.add(new Integer[]{BestResult.getList()[i], 0});
                    cap = 0;
                }
            }
        }
        return result;
    }

    /*
     * 返回jsonArray {   center:{
     *                              address:xxx
     *                          }
     *                  order:[
     *                      [
     *                          {id:2,address:xxx.xxx,xxx,xxx},
     *                          {id:4,address:xxx.xxx,xxx,xxx},
     *                          {id:3,address:xxx.xxx,xxx,xxx}
     *                      ],
     *                      [
     *                          {id:9,address:xxx.xxx,xxx,xxx},
     *                        {id:11,address:xxx.xxx,xxx,xxx},
     *                          {id:6,address:xxx.xxx,xxx,xxx}
     *                      ],
     *                      [
     *                          {id:13,address:xxx.xxx,xxx,xxx},
     *                        {id:6,address:xxx.xxx,xxx,xxx}
     *                      ]
     *                  ]
     *              }
     */
    public static PlanResult solveAnd(PathPlan pathPlan, double[][] distance) {
        PathPoint[] p = new PathPoint[pathPlan.pointList.size()+1];
        for (int i = 0; i < p.length; i++) {
            p[i] = new PathPoint();
            p[i].setId(i==0?0:pathPlan.pointList.get(i-1).id);
            p[i].setDemand(i==0?0:pathPlan.pointList.get(i-1).demand);
            p[i].setAddress(i==0?pathPlan.administrator.address:pathPlan.pointList.get(i-1).address);
        }
        TabuSearch tabu = new TabuSearch(p, distance, pathPlan.maxLoad);
        tabu.solve();
        return tabu.show();
    }

}
