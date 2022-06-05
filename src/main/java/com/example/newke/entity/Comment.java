package com.example.newke.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("comment")
public class Comment {

    @TableId("id")
    private Integer id;

    @TableField("user_id")
    private Integer userId;
    @TableField("entity_type")
    private Integer entityType;
    @TableField("entity_id")
    private Integer entityId;
    @TableField("target_id")
    private Integer targetId;
    @TableField("content")
    private String content;
    @TableField("status")
    private Integer status;
    @TableField("create_time")
    private Date createTime;


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", entityType=" + entityType +
                ", entityId=" + entityId +
                ", targetId=" + targetId +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
