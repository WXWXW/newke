package com.example.newke.service.impl;

import com.example.newke.service.LikeService;
import com.example.newke.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void like(int entityType, int entityId, int userId, int entityUserId) {

        redisTemplate.execute(new SessionCallback<Object>(){

            @Override
            public  Object execute(RedisOperations ops) throws DataAccessException {
                String likeKey = RedisUtils.getLikeKey(entityType, entityId);
                String likeUserKey = RedisUtils.getLikeUserKey(userId);
                //先判断是否点赞，没有点赞就点赞，点赞了就取消点赞 ,参数为字符串否则报错
                Boolean member = ops.opsForSet().isMember(likeKey,userId+"");
                //开启事务
                ops.multi();
                //执行事务之内的操作
                if(member){//点赞了就取消
                    ops.opsForSet().remove(likeKey,userId+"");
                    ops.opsForValue().decrement(likeUserKey);
                }else{
                    //没有点赞，点赞
                    ops.opsForSet().add(likeKey,userId+"");
                    ops.opsForValue().increment(likeUserKey);
                }
                return ops.exec();
            }
        });
    }

    @Override
    public boolean likeStatus(int entityType, int entityId) {
        String likeKey = RedisUtils.getLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(likeKey) != 0;
    }

    @Override
    public Long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisUtils.getLikeKey(entityType, entityId);
        return  redisTemplate.opsForSet().size(likeKey);
    }

    @Override
    public Long getAllLikeCountOfUser(int userId) {
        String likeUserKey = RedisUtils.getLikeUserKey(userId);
        String count = redisTemplate.opsForValue().get(likeUserKey);
        Long likeCount = count == null ?0:Long.parseLong(count);
        return likeCount;
    }
}
