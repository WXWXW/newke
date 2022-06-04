package com.example.newke.service;

import com.example.newke.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {

    User findUserById(int userId);

    Map<String, Object> register(User user);

    int activation(int userId, String code);

    Map<String,Object> login(String username, String password, int expired);

    void logout(String ticket);

    int updateHeader(int userId, String headUrl);

    void updatePassword(Integer id, String aNew);
}
