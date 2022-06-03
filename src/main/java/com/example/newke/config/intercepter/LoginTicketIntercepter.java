package com.example.newke.config.intercepter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.newke.dao.LoginTricketDao;
import com.example.newke.dao.UserDao;
import com.example.newke.entity.LoginTicket;
import com.example.newke.entity.User;
import com.example.newke.utils.CookieUtils;
import com.example.newke.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketIntercepter implements HandlerInterceptor {

    @Autowired
    UserDao userDao;

    @Autowired
    LoginTricketDao loginTricketDao;

    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtils.getValue(request, "ticket");
        if(ticket != null){
            //查询凭证
            QueryWrapper<LoginTicket> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("ticket",ticket);
            LoginTicket loginTicket = loginTricketDao.selectOne(wrapper1);

            //检查凭证是否有效
            if(loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){
                //根据凭证查询用户
                User user = userDao.selectById(loginTicket.getUserId());
                //在本次请求中持有用户,将用户信息存入当前线程
                hostHolder.setUser(user);
            }
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user != null && modelAndView != null){
            modelAndView.addObject("loginUser",user);

        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
