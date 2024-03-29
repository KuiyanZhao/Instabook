package com.example.wechatdemo.model.dos;

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
 * @author 作者
 * 
 */
@Getter
@Setter
@TableName("user_application")
public class UserApplication implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    @TableId("application_id")
    private Long applicationId;

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
     * 0:apply 1:pass -1:disapprove
     */
    @TableField("status")
    private Integer status;

    /**
     * create time
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
