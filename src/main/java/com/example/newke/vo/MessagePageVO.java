package com.example.newke.vo;

import com.example.newke.dto.PageDto;
import com.example.newke.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessagePageVO {

    private List<Message> recordList;  //总的记录数

    private List<ConversationVO> conversationVOS;  //当前用户的会话信息

    private long totalOfUnread;//总的未读消息

    private PageDto pageDto;  //分页信息


    @Override
    public String toString() {
        return "MessagePageVO{" +
                "recordList=" + recordList +
                ", conversationVOS=" + conversationVOS +
                ", totalOfUnread=" + totalOfUnread +
                ", pageDto=" + pageDto +
                '}';
    }
}
