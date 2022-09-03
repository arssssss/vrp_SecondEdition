package com.example.vrp_secondedition.interceptor;

import com.example.vrp_secondedition.annotation.LoginToken;
import com.example.vrp_secondedition.annotation.TokenCheck;
import com.example.vrp_secondedition.enums.result.ResultCode;
import com.example.vrp_secondedition.resultType.ResponseResult;
import com.example.vrp_secondedition.util.SendMsgUtil;
import com.example.vrp_secondedition.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    public TokenUtil tokenUtil;

    public ThreadLocal<String> Id = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Token");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod hm = (HandlerMethod) handler;
        if (hm.getMethod().isAnnotationPresent(LoginToken.class)) {
            if (hm.getMethod().getAnnotation(LoginToken.class).required()) {
                return true;
            }
        }
        if (hm.getMethod().isAnnotationPresent(TokenCheck.class)) {
            if (!hm.getMethod().getAnnotation(TokenCheck.class).required()) {
                return true;
            }
            if (!StringUtils.hasText(token)) {
                //未携带token
                SendMsgUtil.sendJsonMessage(response,new ResponseResult<>(ResultCode.TOKENERROR.getValue(),"未登录"));
                return false;
            }
            if (!tokenUtil.IsEffective(token)) {
                //token过期
                SendMsgUtil.sendJsonMessage(response,new ResponseResult<>(ResultCode.TOKENERROR.getValue(),"登录已过期"));
                return false;
            }
            if (tokenUtil.getId(token, hm.getMethod().getAnnotation(TokenCheck.class).TYPE()) == null ||
                    tokenUtil.getId(token, hm.getMethod().getAnnotation(TokenCheck.class).TYPE()).equals("null")) {
                //token无效
                SendMsgUtil.sendJsonMessage(response,new ResponseResult<>(ResultCode.TOKENERROR.getValue(),"身份验证错误,请重新登录"));
                return false;
            }
            Id.set(tokenUtil.getId(token, hm.getMethod().getAnnotation(TokenCheck.class).TYPE()));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Id.remove();
    }

}

