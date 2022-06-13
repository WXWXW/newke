package com.example.newke.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("message")
public class Message {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("from_id")
    private Integer fromId;    //1代表系统消息
    @TableField("to_id")
    private Integer toId;
    @TableField("conversation_id")
    private String conversationId;//会话id
    @TableField("content")
    private String content;
    @TableField("status")
    private int status;//0 未读 1 已读 2 删除
    @TableField("create_time")
    private Date createTime;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", conversationId='" + conversationId + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
