package com.example.vrp_secondedition.mapper;

import com.example.vrp_secondedition.pojo.Order;
import com.example.vrp_secondedition.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    public Integer addUser(User user);

    public Integer selectIdByPhAndPw(String phone,String password);

    public User selectUserById(Integer userId);

    public Integer updateUser(User user);

    public List<Order> selectOrdersByUserId(Integer userId);

    public Integer addOrder(Order order);

    public Order selectOrderById(Integer id);

    public Integer updateOrder(Order order);

}
