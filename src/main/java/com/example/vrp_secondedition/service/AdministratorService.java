package com.example.vrp_secondedition.service;

import com.example.vrp_secondedition.RequestType.PathPlan;
import com.example.vrp_secondedition.enums.token.TokenType;
import com.example.vrp_secondedition.mapper.AdministratorMapper;
import com.example.vrp_secondedition.pojo.Administrator;
import com.example.vrp_secondedition.pojo.Order;
import com.example.vrp_secondedition.pojo.Vehicle;
import com.example.vrp_secondedition.resultType.PlanResult;
import com.example.vrp_secondedition.util.HttpApi;
import com.example.vrp_secondedition.util.TokenUtil;
import com.example.vrp_secondedition.util.TabuSearch;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorService {

    @Autowired
    public AdministratorMapper administratorMapper;

    @Autowired
    public TokenUtil tokenUtil;

    public String login(String phone,String password) throws Exception {
        Integer id = administratorMapper.selectIdByPhAndPw(phone,password);
        if(id==null)
            throw  new Exception("登录失败");
        return tokenUtil.CreateToken(24*3600*1000,String.valueOf(id), TokenType.Manager);
    }

    public Administrator info(Integer id){
        return administratorMapper.selectAdmin(id);
    }

    public PageInfo orders(Integer startPage, Integer pageSize){
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo(administratorMapper.selectOrders(),startPage);
    }

    public List<Order> ordersNoAllot(){
        return administratorMapper.selectOrdersNoAllot();
    }

    public List<Vehicle> vehicle(){
        return administratorMapper.selectVehicle();
    }

    public PlanResult plan(PathPlan pathPlan) throws Exception {
        if(pathPlan.administrator==null||pathPlan.pointList==null|| pathPlan.maxLoad < 0.00001||pathPlan.drivingStrategy==null)
            throw new Exception("数据不合法");
        if(!HttpApi.translateAddress(pathPlan))
            throw new Exception("无效的地址");
        double[][] distance=HttpApi.getDirection(pathPlan);
        if(distance==null)
            throw new Exception("无效的地址路线");
        return TabuSearch.solveAnd(pathPlan,distance);
    }
}
