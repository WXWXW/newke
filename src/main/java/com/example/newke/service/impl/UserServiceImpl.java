package com.example.newke.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.newke.dao.LoginTricketDao;
import com.example.newke.dao.UserDao;
import com.example.newke.entity.LoginTicket;
import com.example.newke.entity.User;
import com.example.newke.service.UserService;
import com.example.newke.utils.CommunityConstant;
import com.example.newke.utils.CommunityUtil;
import com.example.newke.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService, CommunityConstant {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTricketDao loginTricketDao;


    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    /**
     * 项目名
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public User findUserById(int userId) {

        User user = userDao.selectById(userId);
        return user;
    }

    @Override
    public Map<String, Object> register(User user) {

        HashMap<String, Object> map = new HashMap<>();

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        QueryWrapper<User> wrapper2 = new QueryWrapper<>();

        if(user == null){
            throw  new IllegalArgumentException("user不能为空");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空! ");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空! ");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空! ");
            return map;
        }

        //验证账号
        wrapper.eq("username",user.getUsername());
        User u = userDao.selectOne(wrapper);
        if(u != null){
            map.put("usernameMsg","该账号已存在！");
            return map;
        }

        //验证邮箱
        wrapper2.eq("email",user.getEmail());
        u = userDao.selectOne(wrapper2);
        if(u != null){
            map.put("emailMsg","该邮箱已被注册！");
            return map;
        }

        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("C:/Users/30669/Desktop/head.PNG"));
        user.setCreateTime(new Date());


        userDao.insert(user);


        //给用户发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());

        //http:localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailUtils.sendMail(user.getEmail(),"激活账号",content);

        return null;
    }

    public int activation(int userId, String code){


        User user = userDao.selectById(userId);

        if(user.getStatus() == 1){
            return ACTIVATION_SUCCESS;
        }else if(user.getActivationCode().equals(code)){

            user.setStatus(1);
            userDao.updateById(user);


        }else{
            return ACTIVATION_FAILURE;
        }
        return 0;
    }

    @Override
    public Map<String, Object> login(String username, String password, int expired) {

        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空！");
            return map;
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user =userDao.selectOne(wrapper);

        if(user == null){
            map.put("usernameMsg","该账号不存在！");
            return map;
        }
        //验证激活状态
        if(user.getStatus() == 0){
            map.put("usernameMsg","该账号未激活！");
            return map;
        }

        //验证密码
        String s = CommunityUtil.md5(password + user.getSalt());
        if(!s.equals(user.getPassword())){
            map.put("passwordMsg","密码错误！");
            return map;
        }


        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expired*1000));  //到期时间
        loginTricketDao.insert(loginTicket);

        map.put("ticket",loginTicket.getTicket());

        System.out.println();

        return map;

    }

    @Override
    public void logout(String loginTicket){
        LoginTicket login = new LoginTicket();
        login.setTicket(loginTicket);
        loginTricketDao.updateById(login);
    }

    @Override
    public int updateHeader(int userId, String headUrl){
        UpdateWrapper<User> updateWrapper = new UpdateWrapper();
        updateWrapper.set("header_url",headUrl);
        updateWrapper.eq("id",userId);
        int rows = userDao.update(null,updateWrapper);

        return rows;
    }

    @Override
    public void updatePassword(Integer id, String aNew) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper();
        updateWrapper.set("password",aNew);
        updateWrapper.eq("id",id);
        userDao.update(null,updateWrapper);
    }


}
