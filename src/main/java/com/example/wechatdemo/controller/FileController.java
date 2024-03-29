package com.example.wechatdemo.controller;


import cn.hutool.core.util.IdUtil;
import com.example.wechatdemo.common.model.R;
import com.example.wechatdemo.model.dos.File;
import com.example.wechatdemo.service.FileService;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Kuiyan Zhao
 * @since 2024-03-29
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/message/{request_id}")
    public R<File> uploadMessageFile(@PathVariable("request_id") String requestId,
                                     @RequestPart("file") MultipartFile file) {
        File messageFile = new File();
        messageFile.setChannel(2);
        messageFile.setFileId(IdUtil.getSnowflakeNextId());
        messageFile.setMultipartFile(file);
        messageFile.setOuterId(requestId);
        return R.success(fileService.upload(messageFile));
    }

}
