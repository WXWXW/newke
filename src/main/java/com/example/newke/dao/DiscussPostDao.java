package com.example.newke.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.newke.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussPostDao extends BaseMapper<DiscussPost> {


}
