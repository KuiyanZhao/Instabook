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
 * @author 作者
 * 
 */
@Getter
@Setter
@TableName("user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * primary_key
     */
    @TableId("user_id")
    private Long userId;

    /**
     * name
     */
    @TableField("user_name")
    private String userName;

    @TableField("head_img")
    private String headImg;

    /**
     * password_md5_salt
     */
    @TableField("salt")
    private String salt;

    /**
     * encrypted password
     */
    @TableField("password")
    private String password;

    /**
     * create time
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * login token in header Authorization
     */
    @TableField(exist = false)
    private String token;

}
