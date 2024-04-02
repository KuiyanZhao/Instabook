package com.instabook.service;

import com.instabook.model.dos.UserApplication;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 作者
 * 
 */
public interface UserApplicationService extends IService<UserApplication> {

    UserApplication apply(Long userId);

    UserApplication reply(Long applicationId, int status);
}
