package com.instabook.model.dos;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Kuiyan Zhao
 * @version 1.0 2024-03-29
 * 
 */
@Getter
@Setter
@TableName(value = "message", autoResultMap = true)
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    @TableId("message_id")
    private Long messageId;

    /**
     * user id
     */
    @TableField("user_id")
    private Long userId;

    @TableField("user_name")
    private String userName;

    @TableField("user_head_img")
    private String userHeadImg;

    /**
     * another one
     */
    @TableField("another_user_id")
    private Long anotherUserId;

    @TableField("another_user_name")
    private String anotherUserName;

    @TableField("another_user_head_img")
    private String anotherUserHeadImg;

    /**
     * chat id
     */
    @TableField("chat_id")
    private String chatId;

    /**
     * message object
     */
    @TableField(value = "message", typeHandler = JacksonTypeHandler.class)
    private JSONObject message;

    /**
     * 0:text 1:img 2:video 3:file
     */
    @TableField("type")
    private Integer type;

    /**
     * create time
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * user del it
     */
    @TableField("del_flag")
    private Integer delFlag;

    /**
     * another one del it
     */
    @TableField("another_del_flag")
    private Integer anotherDelFlag;


    /**
     * use to mark a request
     * for data synchronization in different endpoints or result reply(timeout, not friend, blocked etc.)
     */
    @TableField("request_id")
    private String requestId;


    public String getContent() {
        return this.message != null ? this.message.getString("content") : null;
    }

    public void setContent(String content) {
        // Initialize 'message' if it's null
        if (this.message == null) {
            this.message = new JSONObject();
        }
        this.message.put("content", content);
    }
}
