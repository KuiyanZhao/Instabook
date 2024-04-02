package com.instabook.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.instabook.common.exception.ClientErrorEnum;
import com.instabook.common.exception.ClientException;
import com.instabook.common.model.Page;
import com.instabook.interceptor.UserTokenInterceptor;
import com.instabook.model.dos.News;
import com.instabook.mapper.NewsMapper;
import com.instabook.model.dos.NewsComment;
import com.instabook.model.dos.User;
import com.instabook.model.dos.UserRelationship;
import com.instabook.service.NewsCommentService;
import com.instabook.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.instabook.service.UserRelationshipService;
import com.instabook.service.UserService;
import com.instabook.utils.PageInfoUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Kuiyan Zhao
 * @since 2024-03-29
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Resource
    private NewsCommentService newsCommentService;

    @Resource
    private UserService userService;

    @Resource
    private UserRelationshipService userRelationshipService;

    @Override
    public Page<News> getFriendNewsAndComments() {
        Page<News> newsPage = this.page(PageInfoUtil.startPage(), new QueryWrapper<News>()
                .inSql("user_id", "select distinct user_id from user_relationship where (another_user_id = "
                        + UserTokenInterceptor.getUser().getUserId() + " and friend_status = 1 and relation_status = 0) " +
                        "or user_id = " + UserTokenInterceptor.getUser().getUserId()));//can see yourself
        List<News> records = newsPage.getRecords();
        if (records.size() > 0) {
            List<NewsComment> newsId = newsCommentService.list(new QueryWrapper<NewsComment>()
                    .in("news_id", records.stream().map(News::getNewsId).toList()));
            records.forEach(news -> news.setComments(newsId.stream()
                    .filter(newsComment -> newsComment.getNewsId().equals(news.getNewsId())).toList()));
            newsPage.setRecords(records);
        }
        return newsPage;
    }

    @Override
    public News publish(News news) {
        User user = userService.getById(UserTokenInterceptor.getUser().getUserId());
        if (user == null) {
            throw new ClientException(ClientErrorEnum.UserNotExist);
        }

        news.setUserId(user.getUserId());
        news.setUserName(user.getUserName());
        news.setHeadImg(user.getHeadImg());
        news.setNewsId(IdUtil.getSnowflakeNextId());
        this.save(news);
        return news;
    }

    @Override
    public News like(Long newsId, int like) {
        News news = this.getById(newsId);
        if (news == null) {
            throw new ClientException(ClientErrorEnum.DataNotExist);
        }

        UserRelationship userRelationship = userRelationshipService.getOne(new QueryWrapper<UserRelationship>()
                .eq("user_id", news.getUserId())
                .eq("another_user_id", UserTokenInterceptor.getUser().getUserId()));
        if (userRelationship == null) {
            throw new ClientException(ClientErrorEnum.RelationshipNotExist);
        }
        if (userRelationship.getRelationStatus() == 1) {
            throw new ClientException(ClientErrorEnum.Blocked);
        }
        if (userRelationship.getFriendStatus() == 0) {
            throw new ClientException(ClientErrorEnum.RelationshipNotExist);
        }

        User user = userService.getById(UserTokenInterceptor.getUser().getUserId());
        if (user == null) {
            throw new ClientException(ClientErrorEnum.UserNotExist);
        }
        User user2 = new User();
        user2.setUserName(user.getUserName());
        user2.setUserId(user.getUserId());
        user2.setHeadImg(user.getHeadImg());

        if (like == 0) {//unlike
            //format map to list
            news.setUnlikeUsersMetaInfo(JSON.parseArray(JSON.toJSONString(news.getUnlikeUsersMetaInfo()), User.class));
            if (news.getUnlikeUsersMetaInfo().stream().map(User::getUserId).toList()
                    .contains(UserTokenInterceptor.getUser().getUserId())) {
                news.setUnlikeUsersMetaInfo(news.getUnlikeUsersMetaInfo().stream()
                        .filter(user1 -> !user1.getUserId().equals(UserTokenInterceptor.getUser().getUserId()))
                        .toList());
                news.setUnlikeNum(news.getUnlikeNum() - 1);
            } else {
                news.getUnlikeUsersMetaInfo().add(user2);
                news.setUnlikeNum(news.getUnlikeNum() + 1);
            }
        }

        if (like == 1) {//like
            //format map to list
            news.setLikeUsersMetaInfo(JSON.parseArray(JSON.toJSONString(news.getLikeUsersMetaInfo()), User.class));

            if (news.getLikeUsersMetaInfo().stream().map(User::getUserId).toList()
                    .contains(UserTokenInterceptor.getUser().getUserId())) {
                news.setLikeUsersMetaInfo(news.getLikeUsersMetaInfo().stream()
                        .filter(user1 -> !user1.getUserId().equals(UserTokenInterceptor.getUser().getUserId()))
                        .toList());
                news.setLikeNum(news.getLikeNum() - 1);
            } else {
                news.getLikeUsersMetaInfo().add(user2);
                news.setLikeNum(news.getLikeNum() + 1);
            }
        }

        boolean update = this.update(new UpdateWrapper<News>()
                .eq("news_id", newsId)
                .eq("version", news.getVersion())
                .set("version", news.getVersion() + 1)
                .set("like_num", news.getLikeNum())
                .set("unlike_num", news.getUnlikeNum())
                .set("like_users_meta_info", JSON.toJSONString(news.getLikeUsersMetaInfo()))
                .set("unlike_users_meta_info", JSON.toJSONString(news.getUnlikeUsersMetaInfo())));
        if (!update) {
            throw new ClientException(ClientErrorEnum.IOError, "too many people, please wait and try again");
        } else {
            return news;
        }
    }
}
