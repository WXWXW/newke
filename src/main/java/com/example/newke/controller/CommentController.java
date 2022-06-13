package com.example.newke.controller;

import com.example.newke.entity.Comment;
import com.example.newke.service.CommentService;
import com.example.newke.service.DiscussPostService;
import com.example.newke.utils.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/comment")
public class CommentController {



    @Autowired
    private CommentService commentService;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "/add/{discussPostId}",method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId")int discussPostId, Comment comment){

        log.info("前端评论对象：{}",comment);

        System.out.println(hostHolder.getUser().toString());
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());



        commentService.insertComment(comment);

        int rows = commentService.selectCountByEntity(comment.getEntityType(),comment.getEntityId());
        discussPostService.updateCommentCount(comment.getId(),rows);

        return "redirect:/discuss/detail/"+discussPostId;
    }
}
