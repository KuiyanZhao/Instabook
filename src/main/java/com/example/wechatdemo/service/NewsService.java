package com.example.wechatdemo.service;

import com.example.wechatdemo.common.model.Page;
import com.example.wechatdemo.model.dos.News;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Kuiyan Zhao
 * @since 2024-03-29
 */
public interface NewsService extends IService<News> {

    Page<News> getFriendNewsAndComments();

    News publish(News news);

    News like(Long newsId, int like);
}
