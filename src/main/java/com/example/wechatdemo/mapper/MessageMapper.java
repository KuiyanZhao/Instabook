package com.example.wechatdemo.mapper;

import com.example.wechatdemo.model.dos.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 作者
 * 
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}