package com.example.vrp_secondedition.RequestType;

import com.example.vrp_secondedition.pojo.Administrator;
import com.example.vrp_secondedition.pojo.Order;
import lombok.Data;

import java.util.List;
@Data
public class PathPlan {

    public Administrator administrator;

    public List<Order> pointList;

    public Double maxLoad;

    public Integer drivingStrategy;
}
