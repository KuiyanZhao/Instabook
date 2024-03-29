package com.example.wechatdemo.mapper;

import com.example.wechatdemo.model.dos.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Kuiyan Zhao
 * @since 2024-03-29
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {

}
