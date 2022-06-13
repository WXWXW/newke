package com.example.newke.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.newke.dto.PageDto;
import com.example.newke.entity.DiscussPost;
import com.example.newke.vo.PageResult;
import org.springframework.stereotype.Service;

@Service
public interface DiscussPostService extends IService<DiscussPost> {

    PageResult<DiscussPost> listDiscussPost(PageDto PageDto);

    Integer getTotal();

    Integer insertDiscussPost(DiscussPost discussPost);

    DiscussPost findDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);
}
