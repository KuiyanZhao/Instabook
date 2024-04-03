package com.instabook.model.dos;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
@TableName("file")
public class File implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    @TableId("file_id")
    private Long fileId;

    /**
     * 1:head_img 2:message
     */
    @TableField("channel")
    private Integer channel;

    /**
     * url
     */
    @TableField("url")
    private String url;

    /**
     * path
     */
    @TableField("path")
    private String path;

    /**
     * outer id
     */
    @TableField("outer_id")
    private String outerId;

    /**
     * create time
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private MultipartFile multipartFile;


}
