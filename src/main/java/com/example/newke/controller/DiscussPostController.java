package com.example.newke.controller;

import com.example.newke.dto.PageDto;
import com.example.newke.entity.Comment;
import com.example.newke.entity.DiscussPost;
import com.example.newke.entity.User;
import com.example.newke.service.CommentService;
import com.example.newke.service.DiscussPostService;
import com.example.newke.service.UserService;
import com.example.newke.utils.CommunityConstant;
import com.example.newke.utils.CommunityUtil;
import com.example.newke.utils.HostHolder;
import com.example.newke.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {


    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJsonString(403, "您还没有登录!");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.insertDiscussPost(discussPost);

        return CommunityUtil.getJsonString(0, "发布成功!");
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, PageDto pageDto) {

        //帖子，每次查询效率就低了，后期存入redis
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);

        //作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);


        pageDto.setLimit(5);
        pageDto.setPath("/discuss/detail/" + discussPostId);
        pageDto.setRows(post.getCommentCount());

        PageResult<Comment> commentPageResult = commentService.listComment(ENTITY_TYPE_POST, post.getId(), pageDto);

        List<Map<String, Object>> commentVoList = new ArrayList<>();

        if (commentPageResult.getRecordList() != null) {
            //将每条评论的评论人、评论信息插入map
            for (Comment comment : commentPageResult.getRecordList()) {
                Map<String, Object> map = new HashMap<>();
                map.put("comment", comment);
                map.put("user", userService.findUserById(comment.getUserId()));

                PageResult<Comment> replyListResult =
                        commentService.listComment(ENTITY_TYPE_COMMENT, comment.getId(), pageDto);
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyListResult != null) {
                    for (Comment reply : replyListResult.getRecordList()) {
                        Map<String, Object> rmap = new HashMap<>();
                        rmap.put("reply", reply);
                        rmap.put("user", userService.findUserById(reply.getUserId()));
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        rmap.put("target", target);

                        replyVoList.add(rmap);
                    }
                }
                map.put("replys", replyVoList);
                int commentCount = commentService.selectCountByEntity(ENTITY_TYPE_COMMENT, comment.getId());
                map.put("replyCount", commentCount);
                commentVoList.add(map);
            }
        }

        return "/site/discuss-detail";
    }
}
