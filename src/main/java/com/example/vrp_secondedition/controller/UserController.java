package com.example.vrp_secondedition.controller;

import com.example.vrp_secondedition.annotation.TokenCheck;
import com.example.vrp_secondedition.enums.result.ResultCode;
import com.example.vrp_secondedition.enums.token.TokenType;
import com.example.vrp_secondedition.interceptor.TokenInterceptor;
import com.example.vrp_secondedition.pojo.Order;
import com.example.vrp_secondedition.pojo.User;
import com.example.vrp_secondedition.resultType.ResponseResult;
import com.example.vrp_secondedition.service.UserService;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    public TokenInterceptor tokenInterceptor;
    @GetMapping("/")
    public ResponseResult test(@Nullable@RequestParam("test")String test){
        return new ResponseResult<>(ResultCode.OK.getValue(),"连接成功");
    }

    /**
     * 登录
     * @param phone
     * @param password
     * @return
     */
    @PostMapping("/login")
    public ResponseResult login(@Nullable @RequestParam("phone") String phone,@Nullable @RequestParam("password") String password){
        try {
            return new ResponseResult(ResultCode.OK.getValue(), userService.login(phone,password));
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseResult register(User user){
        try {
            userService.register(user);
            return new ResponseResult(ResultCode.OK.getValue(), "注册成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 用户信息
     * @return
     */
    @TokenCheck(TYPE = TokenType.User)
    @GetMapping("/info")
    public ResponseResult info(){
        try {
            return new ResponseResult(ResultCode.OK.getValue(), userService.info(Integer.valueOf(tokenInterceptor.Id.get())));
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @TokenCheck(TYPE = TokenType.User)
    @PostMapping("/info")
    public ResponseResult updateInfo(User user){
        try {
            userService.updateInfo(user);
            return new ResponseResult(ResultCode.OK.getValue(), "更新成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 查询指定订单
     * @param id
     * @return
     */
    @TokenCheck(TYPE = TokenType.User)
    @GetMapping("/order")
    public ResponseResult order(@RequestParam("id")Integer id){
        try {
            return new ResponseResult(ResultCode.OK.getValue(), userService.order(id,Integer.valueOf(tokenInterceptor.Id.get())));
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.getMessage());
        }
    }

    /**
     * 查询订单（分页)
     * @param startPage
     * @param pageSize
     * @return
     */
    @TokenCheck(TYPE = TokenType.User)
    @GetMapping("/orders")
    public ResponseResult orders(@RequestParam(value = "startPage") Integer startPage,
                                 @RequestParam(value = "pageSize") Integer pageSize){
        try {
            return new ResponseResult(ResultCode.OK.getValue(), userService.orders(Integer.valueOf(tokenInterceptor.Id.get()),startPage,pageSize));
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 添加订单
     * @param order
     * @return
     */
    @TokenCheck(TYPE = TokenType.User)
    @PutMapping("/order")
    public ResponseResult addOrder(Order order){
        try {
            order.user_id=Integer.valueOf(tokenInterceptor.Id.get());
            userService.addOrder(order);
            return new ResponseResult(ResultCode.OK.getValue(), "添加成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 修改订单
     * @param order
     * @return
     */
    @TokenCheck(TYPE = TokenType.User)
    @PostMapping("/order")
    public ResponseResult updateOrder(Order order){
        try {
            userService.updateOrder(order);
            return new ResponseResult(ResultCode.OK.getValue(), "修改成功");
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }




}
