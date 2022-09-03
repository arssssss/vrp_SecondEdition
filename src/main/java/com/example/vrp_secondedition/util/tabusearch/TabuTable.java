package com.example.vrp_secondedition.util.tabusearch;

import java.util.LinkedList;
import java.util.Queue;

//禁忌表类
public class TabuTable {
    private Queue<VrpResult> queue;
    int maxsize;

    public TabuTable(){
        queue=new LinkedList<>();
        maxsize=0;
    }

    //设置禁忌表长
    public void setsize(int maxsize){
        this.maxsize=maxsize;
    }

    //清空禁忌表
    public void clear(){
        queue=new LinkedList<>();
    }

    //插入禁忌表
    public void add(VrpResult result){
        if (queue.size()>=maxsize)
            queue.poll();
        queue.add(result);
    }
    public boolean isEmpty(){
        return queue.isEmpty();
    }

    //结果集是否在禁忌表内
    public boolean contains(VrpResult result){
        return queue.contains(result);
    }

    //返回禁忌表最解
    public VrpResult peek(){
        return queue.peek();
    }


}
