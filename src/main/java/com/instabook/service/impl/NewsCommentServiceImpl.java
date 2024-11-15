package com.instabook.service.impl;

import cn.hutool.core.util.IdUtil;
import com.instabook.common.exception.ClientErrorEnum;
import com.instabook.common.exception.ClientException;
import com.instabook.interceptor.UserTokenInterceptor;
import com.instabook.model.dos.News;
import com.instabook.model.dos.NewsComment;
import com.instabook.mapper.NewsCommentMapper;
import com.instabook.model.dos.User;
import com.instabook.service.NewsCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.instabook.service.NewsService;
import com.instabook.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kuiyan Zhao
 * @version 1.0 2024-03-29
 * @since 2024-03-29
 */
@Service
public class NewsCommentServiceImpl extends ServiceImpl<NewsCommentMapper, NewsComment> implements NewsCommentService {

    @Lazy
    @Resource
    private UserService userService;

    @Lazy
    @Resource
    private NewsService newsService;

    @Override
    public NewsComment replyComment(NewsComment newsComment) {
        newsComment.setNewsCommentId(IdUtil.getSnowflakeNextId());
        newsComment.setUserId(UserTokenInterceptor.getUser().getUserId());
        User user = userService.getById(newsComment.getUserId());
        newsComment.setUserName(user.getUserName());

        NewsComment replyComment = getById(newsComment.getReplyCommentId());
        if (replyComment == null) {
            throw new ClientException(ClientErrorEnum.REPLY_COMMENT_NOT_EXIST);
        }
        newsComment.setNewsId(replyComment.getNewsId());
        newsComment.setReplyComment(replyComment.getComment() == null ? "" :
                replyComment.getComment().length() <= 30 ? replyComment.getComment() : replyComment.getComment().substring(0, 30));
        newsComment.setReplyUserId(replyComment.getUserId());
        newsComment.setReplyUserName(replyComment.getUserName());

        save(newsComment);
        return newsComment;
    }

    @Override
    public NewsComment replyNews(NewsComment newsComment) {
        newsComment.setNewsCommentId(IdUtil.getSnowflakeNextId());
        newsComment.setUserId(UserTokenInterceptor.getUser().getUserId());
        User user = userService.getById(newsComment.getUserId());
        newsComment.setUserName(user.getUserName());

        News news = newsService.getById(newsComment.getNewsId());
        if (news == null) {
            throw new ClientException(ClientErrorEnum.REPLY_COMMENT_NOT_EXIST);
        }

        newsComment.setReplyComment(news.getMessage() == null ? "" :
                news.getMessage().length() <= 30 ? news.getMessage() : news.getMessage().substring(0, 30));
        newsComment.setReplyUserId(news.getUserId());
        newsComment.setReplyUserName(news.getUserName());

        save(newsComment);
        return newsComment;
    }
}
