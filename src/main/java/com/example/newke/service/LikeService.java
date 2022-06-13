package com.example.newke.service;

import org.springframework.stereotype.Service;

@Service
public interface LikeService {

    void like(int entityType,int entityId,int userId,int entityUserId);

    boolean likeStatus(int entityType, int entityId);

    Long getLikeCount(int entityType,int entityId);

    Long getAllLikeCountOfUser(int userId);
}
