package com.example.wechatdemo.service.impl;

import cn.hutool.core.util.IdUtil;
import com.example.wechatdemo.common.exception.ClientErrorEnum;
import com.example.wechatdemo.common.exception.ClientException;
import com.example.wechatdemo.interceptor.UserTokenInterceptor;
import com.example.wechatdemo.model.dos.News;
import com.example.wechatdemo.model.dos.NewsComment;
import com.example.wechatdemo.mapper.NewsCommentMapper;
import com.example.wechatdemo.model.dos.User;
import com.example.wechatdemo.service.NewsCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wechatdemo.service.NewsService;
import com.example.wechatdemo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kuiyan Zhao
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
