package com.example.newke.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.newke.entity.Comment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDao extends BaseMapper<Comment> {
}
