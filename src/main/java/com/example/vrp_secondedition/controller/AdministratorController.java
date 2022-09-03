package com.example.vrp_secondedition.controller;

import com.example.vrp_secondedition.RequestType.PathPlan;
import com.example.vrp_secondedition.annotation.LoginToken;
import com.example.vrp_secondedition.annotation.TokenCheck;
import com.example.vrp_secondedition.enums.result.ResultCode;
import com.example.vrp_secondedition.enums.token.TokenType;
import com.example.vrp_secondedition.interceptor.TokenInterceptor;
import com.example.vrp_secondedition.resultType.ResponseResult;
import com.example.vrp_secondedition.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {

    @Autowired
    public AdministratorService administratorService;

    @Autowired
    public TokenInterceptor tokenInterceptor;

    /**
     * 登录
     * @param phone
     * @param password
     * @return
     */
    @LoginToken
    @PostMapping("/login")
    public ResponseResult login(@RequestParam("phone") String phone,@RequestParam("password") String password){
        try {
            return new ResponseResult(ResultCode.OK.getValue(), administratorService.login(phone,password));
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.getMessage());
        }
    }

    /**
     * 查看管理员信息
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @GetMapping("/info")
    public ResponseResult info(){
        try {
            return new ResponseResult(ResultCode.OK.getValue(), administratorService.info(Integer.valueOf(tokenInterceptor.Id.get())));
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

    /**
     * 查看所有订单(分页）
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @GetMapping("/orders")
    public ResponseResult orders(@RequestParam(value = "startPage") Integer startPage,
                                 @RequestParam(value = "pageSize") Integer pageSize){
        try {
            return new ResponseResult(ResultCode.OK.getValue(), administratorService.orders(startPage,pageSize));
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), "获取失败");
        }
    }

    /**
     *  获取未分配订单
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @GetMapping("/orderNoAllot")
    public ResponseResult orderNoAllot(){
        try {
            return new ResponseResult(ResultCode.OK.getValue(), administratorService.ordersNoAllot());
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), "获取失败");
        }
    }

    /**
     * 获取车型信息
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @GetMapping("/vehicle")
    public ResponseResult vehicle(){
        try {
            return new ResponseResult(ResultCode.OK.getValue(), administratorService.vehicle());
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), "获取失败");
        }
    }

    /**
     * @return
     */
    @TokenCheck(TYPE = TokenType.Manager)
    @PostMapping("/plan")
    public ResponseResult plan(@RequestBody PathPlan pathPlan){
        try {
            return new ResponseResult(ResultCode.OK.getValue(), administratorService.plan(pathPlan));
        }catch (Exception e){
            return new ResponseResult(ResultCode.ERROR.getValue(), e.toString());
        }
    }

}
