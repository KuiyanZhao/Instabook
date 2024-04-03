package com.instabook.model.dos;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * @since 2024-03-29
 */
@Getter
@Setter
@TableName(value = "news", autoResultMap = true)
public class News implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    @TableId("news_id")
    private Long newsId;

    /**
     * user id
     */
    @TableField("user_id")
    private Long userId;

    @TableField("user_name")
    private String userName;

    @TableField("head_img")
    private String headImg;

    /**
     * photo urls
     */
    @TableField(value = "photos", typeHandler = JacksonTypeHandler.class)
    private JSONArray photos;

    /**
     * messages
     */
    @TableField("message")
    private String message;

    /**
     * like num
     */
    @TableField("like_num")
    private Integer likeNum;

    /**
     * unlike num
     */
    @TableField("unlike_num")
    private Integer unlikeNum;

    /**
     * like users
     */
    @TableField(value = "like_users_meta_info", typeHandler = JacksonTypeHandler.class)
    private List<User> likeUsersMetaInfo = new ArrayList<>();

    /**
     * unlike users
     */
    @TableField(value = "unlike_users_meta_info", typeHandler = JacksonTypeHandler.class)
    private List<User> unlikeUsersMetaInfo = new ArrayList<>();

    /**
     * create_time
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("version")
    private Long version;

    @TableField(exist = false)
    private List<NewsComment> comments;


}
