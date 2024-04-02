package com.instabook.controller;


import com.instabook.common.model.Page;
import com.instabook.common.model.R;
import com.instabook.model.dos.News;
import com.instabook.service.NewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Kuiyan Zhao
 * @since 2024-03-29
 */
@RestController
@RequestMapping("/news")
public class NewsController {

    @Resource
    private NewsService newsService;

    @GetMapping("/page")
    public R<Page<News>> page() {
        return R.success(newsService.getFriendNewsAndComments());
    }

    @PostMapping
    public R<News> publish(@RequestBody News news) {
        return R.success(newsService.publish(news));
    }

    //1 for like 0 for unlike
    @PutMapping("/{newsId}/{like}")
    public R<News> like(@PathVariable Long newsId, @PathVariable int like) {
        return R.success(newsService.like(newsId, like));
    }

}
