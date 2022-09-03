package com.example.vrp_secondedition.util.tabusearch;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Cw {
    //解矩阵
    private boolean[][] BestResult;
    //路径点集
    private PathPoint[] points;
    //最大装载量
    private double capacity;
    //路径长度图
    private double[][] map;

    public void Init(PathPoint[] points,double capacity){
        this.points=points;
        this.capacity=capacity;
    }
    public void solve(){
        BestResult=new boolean[points.length][points.length];
        for(int i=1;i<points.length;i++){
            BestResult[i][0]=true;
            BestResult[0][i]=true;
        }
        //优先队列比较节约值
        Queue<Object> queue=new PriorityQueue<>(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if(((double[])o1)[2]>((double[])o2)[2])
                    return 1;
                else if(((double[])o1)[2]==((double[])o2)[2])
                    return 0;
                else
                    return -1;
            }
        });
        for (int i = 1; i < points.length; i++) {
            for (int j = 1; j < points.length; j++) {
                if (i != j) {
                    queue.add(new double[]{i,j,(getdistance(i,j)-getdistance(i,0)-getdistance(0,j))});
                }
            }
        }
        double[] s;
        while (!queue.isEmpty() ){
            s = (double[])queue.poll();
            System.out.println(s[2]);
            if (BestResult[(int)s[0]][0] && BestResult[0][(int)s[1]] && !both_line((int)s[0],(int)s[1])) {
                //仅左右衔接 //两个点在左右端点且两点不在同一条线
                if (load_get((int)s[0]) + load_get((int)s[1]) <= capacity) {
                    //两路线的载货量小于
                    BestResult[(int)s[0]][0]=false;
                    BestResult[0][(int)s[1]]=false;
                    BestResult[(int)s[0]][(int)s[1]]=true;
                }
            }
        }
        System.out.println(1);
    }

    //返回路线上的总负载
    public double load_get(int i) {
        double loa = 0;
        int temp = 0;
        while (i != 0)
        {
            for (int j = 0; j < points.length; j++)
            {
                if (BestResult[j][i])
                {
                    temp = i;
                    i = j;
                    break;
                }
            }
        }
        loa = (BestResult[0][temp]?1:0) * points[temp].getDemand();
        while (temp != 0)
        {
            for (int j = 0; j < points.length; j++)
            {
                if (BestResult[temp][j])
                {
                    loa += (BestResult[temp][j]?1:0) * points[j].getDemand();
                    temp = j;
                    break;
                }
            }
        }
        return loa;
    }

    //返回两点是否在同一路线上
    public boolean both_line(int i, int j) {
        int temp = 0;
        while (i != 0) {
            for (int k = 0; k < points.length; k++) {
                if (BestResult[k][i]) {
                    temp = i;
                    i = k;
                    break;
                }
            }
        }
        i = temp;
        temp = 0;
        while (j != 0) {
            for (int k = 0; k < points.length; k++) {
                if (BestResult[k][j]) {
                    temp = j;
                    j = k;
                    break;
                }
            }
        }
        if (temp == i)
            return true;
        return false;
    }

    //返回两点距离
    public double getdistance(int p1,int p2){
        return (map[p1][p2]);
    }
    //返回解
    public int[] show(){
        int[] result=new int[points.length-1];
        int k=0;
        for(int i=0;i<points.length;i++){
            if (BestResult[0][i]){
                int temp=i;
                while (temp!=0){
                    for(int j=0;j<points.length;j++){
                        if(BestResult[temp][j]){
                            result[k++]=temp;
                            temp=j;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }
}
