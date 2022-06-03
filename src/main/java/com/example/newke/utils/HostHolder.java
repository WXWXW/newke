package com.example.newke.utils;

import com.example.newke.entity.User;
import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<User>();

    public void setUser(User user){
        users.set(user);
    }
    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
