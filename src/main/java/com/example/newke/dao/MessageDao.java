package com.example.newke.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.newke.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MessageDao extends BaseMapper<Message> {

    //分页查询当前用户的所有私信，只显示最后一条没有读的消息
    List<Message> getAllMessagesThisUser(int userId);

    //查询每一个会话的未读消息数量
    int getCountOfUnread(@Param("conversationId") String conversationId);
    //每个会话的总消息条数
    int getCountOfAllMessage(@Param("conversationId") String conversationId);
    //当前用户的所有未读消息数量
    int getTotalUnreadMessage(@Param("userId") int userId);
    //获取系统所有未读消息数量
    int getAllUnreadNoticeCount(@Param("userId") int userId);

    //分页查询当前会话的所有消息
    List<Message> getAllMessageThisConversation(@Param("conversationId") String conversationId);

    //设置消息的状态
    int updateMessageStatus(@Param("ids") List<Integer> ids,@Param("status") int status);


}
