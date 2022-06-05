package com.example.newke.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.newke.dao.DiscussPostDao;
import com.example.newke.dto.PageDto;
import com.example.newke.entity.DiscussPost;
import com.example.newke.service.DiscussPostService;
import com.example.newke.utils.SensitiveUtil;
import com.example.newke.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostDao, DiscussPost> implements DiscussPostService {

    @Autowired
    private DiscussPostDao discussPostDao;

    @Autowired
    private SensitiveUtil sensitiveUtil;


    @Override
    public PageResult<DiscussPost> listDiscussPost(PageDto pageDto) {


        System.out.println(pageDto.toString());

        Page<DiscussPost> page = new Page<>(pageDto.getCurrent(), pageDto.getLimit());

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

    @Override
    public Integer insertDiscussPost(DiscussPost discussPost) {
        if(discussPost == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        //标题内容去标签
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        //过滤敏感词
        discussPost.setTitle(sensitiveUtil.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveUtil.filter(discussPost.getContent()));

        return discussPostDao.insert(discussPost);
    }

    public DiscussPost findDiscussPostById(int id){
        return discussPostDao.selectById(id);
    }
}
