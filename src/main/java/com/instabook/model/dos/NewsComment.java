package com.instabook.model.dos;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
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
@TableName("news_comment")
public class NewsComment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    @TableId("news_comment_id")
    private Long newsCommentId;

    /**
     * news_id
     */
    @TableField("news_id")
    private Long newsId;

    /**
     * comment user id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * user name
     */
    @TableField("user_name")
    private String userName;

    /**
     * reply for one's user id
     */
    @TableField("reply_user_id")
    private Long replyUserId;

    /**
     * one's name
     */
    @TableField("reply_user_name")
    private String replyUserName;

    /**
     * reply comment id
     */
    @TableField("reply_comment_id")
    private Long replyCommentId;

    /**
     * reply comment
     */
    @TableField("reply_comment")
    private String replyComment;

    @TableField("comment")
    private String comment;

    /**
     * create time
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


}
