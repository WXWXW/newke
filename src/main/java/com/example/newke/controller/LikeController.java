package com.example.newke.controller;

import com.example.newke.annotation.LoginRequired;
import com.example.newke.entity.User;
import com.example.newke.service.LikeService;
import com.example.newke.utils.CommunityUtil;
import com.example.newke.utils.HostHolder;
import com.example.newke.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LikeController {


    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/like")
    @LoginRequired
    public String like(int entityType, int entityId, int entityUserId,int postId){

        User user = hostHolder.getUser();
        likeService.like(entityType,entityId,entityUserId,postId);
        long likeCount = likeService.getLikeCount(entityType, entityId);
        boolean likeStatus = likeService.likeStatus(entityType, entityId);
        Map<String,Object> map = new HashMap<>();
        map.put("likeStatus",likeStatus);
        map.put("likeCount",likeCount);

        //将评论帖子放入Redis中，方便热帖排行
        String postRefreshKey = RedisUtils.getPostRefreshKey();
        redisTemplate.opsForSet().add(postRefreshKey,postId+"");

        return CommunityUtil.getJsonString(0,null,map);

    }
}
