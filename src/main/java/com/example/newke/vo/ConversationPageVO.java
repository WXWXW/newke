package com.example.newke.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.newke.dto.PageDto;
import com.example.newke.entity.Message;
import com.example.newke.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationPageVO {

    private Page<Message> page;
    private List<ConversationVO> messages;
    private User fromUser;
    private Integer status; //0表示当前页未读 1已读
}