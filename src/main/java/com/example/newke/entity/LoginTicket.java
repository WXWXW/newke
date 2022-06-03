package com.example.newke.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("login_ticket")
public class LoginTicket {

    @TableId("id")
    private int id;

    @TableField("user_id")
    private int userId;

    @TableField("ticket")
    private String ticket;

    @TableField("status")
    private int status;

    @TableField("expired")
    private Date expired;
}
