package com.instabook.controller;


import cn.hutool.core.util.IdUtil;
import com.instabook.common.model.R;
import com.instabook.model.dos.File;
import com.instabook.service.FileService;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Kuiyan Zhao
 * @version 1.0 2024-03-29
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
