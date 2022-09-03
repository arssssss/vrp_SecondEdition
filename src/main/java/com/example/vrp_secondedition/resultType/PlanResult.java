package com.example.vrp_secondedition.resultType;


import com.example.vrp_secondedition.pojo.Order;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PlanResult {

    public PlanResult(){
        center=new Center();
        orders=new ArrayList<>();
    }

    public Center center;

    public List<List<Order>> orders;

    public Double totalDistance;

    @Data
    public static class Center{
        public String address;
    }

}

