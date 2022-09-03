package com.example.vrp_secondedition.service;

import com.example.vrp_secondedition.enums.token.TokenType;
import com.example.vrp_secondedition.mapper.UserMapper;
import com.example.vrp_secondedition.pojo.Order;
import com.example.vrp_secondedition.pojo.User;
import com.example.vrp_secondedition.util.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.text.normalizer.UBiDiProps;

@Service
public class UserService {

    @Autowired
    public UserMapper userMapper;

    @Autowired
    public TokenUtil tokenUtil;

    public void register(User user) {
        userMapper.addUser(user);
    }

    public String login(String phone, String password) throws Exception {
        Integer id = userMapper.selectIdByPhAndPw(phone, password);
        if (id == null)
            throw new Exception("用户密码错误");
        return tokenUtil.CreateToken(24 * 3600 * 1000, String.valueOf(id), TokenType.User);
    }

    public User info(Integer userId) {
        return userMapper.selectUserById(userId);
    }

    public void updateInfo(User user) {
        userMapper.updateUser(user);
    }

    public PageInfo orders(Integer userId, Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo(userMapper.selectOrdersByUserId(userId), startPage);
    }

    public Order order(Integer id,Integer userId) throws Exception {
        Order order = userMapper.selectOrderById(id);
        if(order==null)
            throw new Exception("无效的订单号");
        if(!order.user_id.equals(userId))
            throw new Exception("订单不属于该用户");
        return order;
    }

    public void addOrder(Order order) {
        userMapper.addOrder(order);
    }

    public void updateOrder(Order order) {
        userMapper.updateOrder(order);
    }
}
