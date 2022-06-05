package com.example.newke.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.newke.dto.PageDto;
import com.example.newke.entity.Comment;
import com.example.newke.vo.PageResult;
import org.springframework.stereotype.Service;

@Service
public interface CommentService extends IService<Comment> {

    PageResult<Comment> listComment(int entityType, int entityId,PageDto pageDto);

    int selectCountByEntity(int EntityType,int EntityId);

}
