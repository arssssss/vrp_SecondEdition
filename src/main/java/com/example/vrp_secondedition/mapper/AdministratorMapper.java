package com.example.vrp_secondedition.mapper;

import com.example.vrp_secondedition.pojo.Administrator;
import com.example.vrp_secondedition.pojo.Order;
import com.example.vrp_secondedition.pojo.Vehicle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdministratorMapper {

    public Integer selectIdByPhAndPw(String phone,String password);

    public List<Order> selectOrders();

    public List<Order> selectOrdersNoAllot();

    public List<Vehicle> selectVehicle();

    public Administrator selectAdmin(Integer id);

}
