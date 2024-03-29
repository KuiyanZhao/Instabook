package com.example.wechatdemo.service;

import com.example.wechatdemo.model.dos.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 作者
 * 
 */
public interface UserService extends IService<User> {

    User register(User user);

    User login(User user);

    List<User> search(String userName);

    User uploadHeadImg(MultipartFile file);
}
