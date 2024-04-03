package com.instabook.service;

import com.instabook.common.model.Page;
import com.instabook.model.dos.News;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Kuiyan Zhao
 * @version 1.0 2024-03-29
 * @since 2024-03-29
 */
public interface NewsService extends IService<News> {

    Page<News> getFriendNewsAndComments();

    News publish(News news);

    News like(Long newsId, int like);
}
