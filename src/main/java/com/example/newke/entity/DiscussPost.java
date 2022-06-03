package com.example.newke.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("discuss_post")
public class DiscussPost {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;
    @TableField("title")
    private String title;
    @TableField("content")
    private String content;
    @TableField("type")
    private Integer type;
    @TableField("status")
    private Integer status;
    @TableField("create_time")
    private Date createTime;
    @TableField("comment_count")
    private Integer commentCount;
    @TableField("score")
    private double score;

    @Override
    public String toString() {
        return "DiscussPost{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createTime=" + createTime +
                ", commentCount=" + commentCount +
                ", score=" + score +
                '}';
    }
}
