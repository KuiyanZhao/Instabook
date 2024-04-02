package com.instabook.service;

import com.instabook.model.dos.NewsComment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Kuiyan Zhao
 * @since 2024-03-29
 */
public interface NewsCommentService extends IService<NewsComment> {

    NewsComment replyComment(NewsComment newsComment);

    NewsComment replyNews(NewsComment newsComment);
}
