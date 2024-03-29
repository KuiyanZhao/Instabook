package com.example.wechatdemo.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wechatdemo.common.exception.ClientErrorEnum;
import com.example.wechatdemo.common.exception.ClientException;
import com.example.wechatdemo.interceptor.UserTokenInterceptor;
import com.example.wechatdemo.model.dos.User;
import com.example.wechatdemo.mapper.UserMapper;
import com.example.wechatdemo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wechatdemo.utils.JWTUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 作者
 * 
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

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
}
