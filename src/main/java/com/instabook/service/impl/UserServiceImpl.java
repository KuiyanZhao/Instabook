package com.instabook.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.instabook.common.exception.ClientErrorEnum;
import com.instabook.common.exception.ClientException;
import com.instabook.interceptor.UserTokenInterceptor;
import com.instabook.model.dos.File;
import com.instabook.model.dos.User;
import com.instabook.mapper.UserMapper;
import com.instabook.model.dos.UserRelationship;
import com.instabook.service.FileService;
import com.instabook.service.UserRelationshipService;
import com.instabook.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.instabook.utils.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Kuiyan Zhao
 * @version 1.0 2024-03-29
 * 
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private FileService fileService;

    @Lazy
    @Resource
    private UserRelationshipService userRelationshipService;

    @Override
    public User register(User user) {
        //validate unique
        User userInDb = this.getOne(new QueryWrapper<User>().eq("user_name", user.getUserName()));
        if (userInDb != null) {
            throw new ClientException(ClientErrorEnum.UserExist);
        }

        user.setSalt("$1$" + RandomUtil.randomString(8));
        user.setPassword(SecureUtil.md5(user.getSalt() + user.getPassword()));
        try {
            boolean save = this.save(user);
            if (!save) {
                throw new ClientException(ClientErrorEnum.UserExist);
            }
        } catch (Exception e) {
            throw new ClientException(ClientErrorEnum.UserExist);
        }
        user.setPassword(null);
        user.setSalt(null);

        String token = JWTUtil.generateToken(user);
        user.setToken(token);
        return user;
    }

    @Override
    public User login(User user) {
        User userInDb = this.getOne(new QueryWrapper<User>().eq("user_name", user.getUserName()));
        if (userInDb == null) {
            throw new ClientException(ClientErrorEnum.UserNotExist);
        }

        //encrypt password with salt
        user.setPassword(SecureUtil.md5(userInDb.getSalt() + user.getPassword()));

        if (userInDb.getPassword().equals(user.getPassword())) {
            String token = JWTUtil.generateToken(userInDb);

            User request = new User();
            request.setUserName(userInDb.getUserName());
            request.setUserId(userInDb.getUserId());
            request.setHeadImg(userInDb.getHeadImg());
            request.setToken(token);
            return request;
        }

        throw new ClientException(ClientErrorEnum.TokenError);
    }

    @Override
    public List<User> search(String userName) {
        //validate login status
        UserTokenInterceptor.getUser();
        List<User> otherUsers = this.list(new QueryWrapper<User>().like("user_name", userName));
        if (otherUsers.size() == 0) {
            throw new ClientException(ClientErrorEnum.UserNotExist);
        }

        otherUsers.forEach(obj -> {
            obj.setPassword(null);
            obj.setSalt(null);
        });

        return otherUsers;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User uploadHeadImg(MultipartFile file) {
        User user = UserTokenInterceptor.getUser();
        File headImgFile = new File();
        headImgFile.setChannel(1);
        headImgFile.setMultipartFile(file);
        headImgFile.setOuterId(String.valueOf(user.getUserId()));
        headImgFile = fileService.upload(headImgFile);

        if (StringUtils.isNotBlank(user.getHeadImg())) {
            File oldFile = fileService.getByOuterId(user.getUserId());
            if (oldFile != null) {
                fileService.delete(oldFile.getFileId());
            }
        }

        this.update(new UpdateWrapper<User>()
                .eq("user_id", user.getUserId())
                .set("head_img", headImgFile.getUrl()));
        user.setHeadImg(headImgFile.getUrl());

        userRelationshipService.update(new UpdateWrapper<UserRelationship>()
                .eq("user_id", user.getUserId())
                .set("user_head_img", headImgFile.getUrl()));

        userRelationshipService.update(new UpdateWrapper<UserRelationship>()
                .eq("another_user_id", user.getUserId())
                .set("another_user_head_img", headImgFile.getUrl()));
        return user;
    }
}
