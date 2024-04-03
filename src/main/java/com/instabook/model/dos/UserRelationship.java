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
 * 
 */
@Getter
@Setter
@TableName("user_relationship")
public class UserRelationship implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * primary key
     */
    @TableId("user_relationship_id")
    private Long userRelationshipId;

    /**
     * user id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * another one
     */
    @TableField("another_user_id")
    private Long anotherUserId;

    /**
     * 0:normal;-1:block
     */
    @TableField("relation_status")
    private Integer relationStatus;

    /**
     * 0:false 1:true
     */
    @TableField("friend_status")
    private Integer friendStatus;

    /**
     * create_time
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


}
