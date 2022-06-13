package com.example.newke.vo;

import com.example.newke.entity.Message;
import com.example.newke.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationVO {
    private Message message; //会话消息
    private User user;//发送者 可以是自己或者他人
    private Integer countOfUnread;//每个会话的未读消息
    private Integer countOfMessage;//每个会话的所有消息数量
}
