package com.example.newke.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.newke.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseMapper<User> {

}
