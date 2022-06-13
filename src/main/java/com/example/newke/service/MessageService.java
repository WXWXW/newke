package com.example.newke.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.newke.dto.PageDto;
import com.example.newke.entity.Message;
import com.example.newke.vo.ConversationPageVO;
import com.example.newke.vo.MessagePageVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService extends IService<Message> {

    MessagePageVO getAllMessage(int userId, PageDto pageDto);

    int selectConversationCount(int userId);

    List<Message> selectLetters(String conversationId,PageDto pageDto);

    int selectLettersCount(String conversationId);

    int selectLetterUnreadCount(int userId, String conversationId);

    int getNoticeCount(int userId);

    int updateStatus(List<Integer> ids,int status);

    int sendMessage(String toName, String content);

    ConversationPageVO getAllConversationMsgByPage(String conversationId, PageDto pageDto);


}
