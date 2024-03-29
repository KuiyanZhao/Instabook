package com.example.wechatdemo.utils;

import com.example.wechatdemo.common.model.Page;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class PageInfoUtil {
    public static <E> Page<E> startPage() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            String pageNo = attributes.getRequest().getParameter("pageNo");
            String size = attributes.getRequest().getParameter("pageSize");
            return new Page<>(Long.parseLong(pageNo == null ? "1" : pageNo),
                    Long.parseLong(size == null ? "10" : size));
        }
        return new Page<>();
    }
}
