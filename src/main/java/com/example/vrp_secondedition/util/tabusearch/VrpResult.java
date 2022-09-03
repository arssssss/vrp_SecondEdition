package com.example.vrp_secondedition.util.tabusearch;

//解集类
public class VrpResult {
    public int[] getList() {
        return list;
    }

    private int[] list;

    public VrpResult(int[] l){
        list=l.clone();
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj instanceof VrpResult){
            int i=this.list.length;
            if (i==((VrpResult) obj).list.length){
                while (--i>=0){
                    if (this.list[i]!=((VrpResult) obj).list[i])
                        return false;
                }
                return true;
            }
        }
        return false;
    }

}
