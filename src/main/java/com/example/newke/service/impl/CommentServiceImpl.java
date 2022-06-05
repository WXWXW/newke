package com.example.newke.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.newke.dao.CommentDao;
import com.example.newke.dao.UserDao;
import com.example.newke.dto.PageDto;
import com.example.newke.entity.Comment;
import com.example.newke.entity.DiscussPost;
import com.example.newke.entity.User;
import com.example.newke.service.CommentService;
import com.example.newke.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {


    @Autowired
    private CommentDao commentDao;

    @Override
    public PageResult<Comment> listComment(int entityType, int entityId,PageDto pageDto) {


        Page<Comment> page = new Page<>(pageDto.getCurrent(), pageDto.getLimit());

        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = Wrappers.lambdaQuery();

        commentLambdaQueryWrapper.eq(Comment::getEntityType,entityType);
        commentLambdaQueryWrapper.eq(Comment::getEntityId,entityId);
        commentLambdaQueryWrapper.orderByDesc(Comment::getCreateTime);

        Page<Comment> commentPage  = commentDao.selectPage(page, commentLambdaQueryWrapper);

        List<Comment> commentList = commentPage.getRecords();


        return new PageResult<>(commentList,(int)page.getTotal());
    }



    @Override
    public int selectCountByEntity(int EntityType, int EntityId) {

        QueryWrapper<Comment> wrapper =new QueryWrapper();
        wrapper.eq("entity_type",EntityType);
        wrapper.eq("entity_id",EntityId);

        int rows = commentDao.selectCount(wrapper);

        return rows;
    }
}
