package com.example.newke.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.newke.dao.DiscussPostDao;
import com.example.newke.dto.HomePageDto;
import com.example.newke.entity.DiscussPost;
import com.example.newke.service.DiscussPostService;
import com.example.newke.utils.PageUtils;
import com.example.newke.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostDao, DiscussPost> implements DiscussPostService {

    @Autowired
    private DiscussPostDao discussPostDao;


    @Override
    public PageResult<DiscussPost> listDiscussPost(HomePageDto homePageDto) {


        System.out.println(homePageDto.toString());

        Page<DiscussPost> page = new Page<>(homePageDto.getCurrent(), homePageDto.getLimit());


        LambdaQueryWrapper<DiscussPost> discussPostLambdaQueryWrapper = Wrappers.lambdaQuery();
        discussPostLambdaQueryWrapper.orderByDesc(DiscussPost::getCreateTime);



        Page<DiscussPost> discussPostPage  = discussPostDao.selectPage(page, discussPostLambdaQueryWrapper);








        List<DiscussPost> DiscussPostList = discussPostPage.getRecords();



        return new PageResult<>(DiscussPostList,(int)page.getTotal());
    }

    @Override
    public Integer getTotal() {

        QueryWrapper<DiscussPost> queryWrapper=new QueryWrapper();

        Integer Rows = discussPostDao.selectCount(queryWrapper);


        return Rows;
    }
}
