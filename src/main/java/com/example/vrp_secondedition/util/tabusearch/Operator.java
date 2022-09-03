package com.example.vrp_secondedition.util.tabusearch;

import java.util.Random;

//算子类
public class Operator {

    //交换两个节点
    public static int[] Swap(int[] r){
        if(r.length<2) return r;
        int i,j,temp;
        int[] result=r.clone();
        do {
            i = new Random().nextInt(r.length);
            j = new Random().nextInt(r.length);
        }while (i==j);
        temp=result[i];
        result[i]=result[j];
        result[j]=temp;
        return result;
    }

    //翻转两节点中间的数组
    public static int[] opt_2(int[] r){
        if(r.length<2) return r;
        int i,j,temp;
        int[] result=r.clone();
        do {
            i = new Random().nextInt(r.length);
            j = new Random().nextInt(r.length);
        }while (i==j);
        if(i>j){
            temp=i;
            i=j;
            j=temp;
        }
        while (i<=j){
            temp=result[i];
            result[i]=result[j];
            result[j]=temp;
            i++;
            j--;
        }
        return result;
    }

    //选择并移除一个节点(node)，然后再选择一个位置将选中的节点插入
    public static int[] Relocation(int[] r){
        if(r.length<2) return r;
        int i,j,temp;
        int[] result=r.clone();
        do {
            i = new Random().nextInt(r.length);
            j = new Random().nextInt(r.length);
        }while (i==j);
        if(i>j){
            temp=i;
            i=j;
            j=temp;
        }
        temp=result[i];
        while (i<j){
            result[i]=result[i+1];
            i++;
        }
        result[j]=temp;
        return result;
    }

}
