package com.example.wechatdemo.service;

import com.aliyun.oss.OSS;
import com.example.wechatdemo.model.dos.File;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Kuiyan Zhao
 * @since 2024-03-29
 */
public interface FileService extends IService<File> {

    File upload(File file);

    File delete(Long fileId);

    File getByOuterId(Long userId);
}
