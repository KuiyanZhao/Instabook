package com.example.wechatdemo.controller;


import com.example.wechatdemo.common.model.R;
import com.example.wechatdemo.model.dos.NewsComment;
import com.example.wechatdemo.service.NewsCommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/news-comment")
public class NewsCommentController {

    @Resource
    private NewsCommentService newsCommentService;

    @PostMapping
    public R<NewsComment> replyComment(@RequestBody NewsComment newsComment) {
        return R.success(newsCommentService.replyComment(newsComment));
    }

    @PostMapping("/news")
    public R<NewsComment> replyNews(@RequestBody NewsComment newsComment) {
        return R.success(newsCommentService.replyNews(newsComment));
    }

}
