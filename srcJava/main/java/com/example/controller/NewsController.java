package com.example.controller;

import com.example.common.Result;
import com.example.entity.News;
import com.example.service.NewsService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Resource
    private NewsService newsService;

    @PostMapping("/add")
    public Result add(@RequestBody News news) {
        newsService.add(news);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody News news) {
        newsService.update(news);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        newsService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(newsService.selectById(id));
    }

    @GetMapping("/selectAll")
    public Result selectAll(News news) {
        List<News> list = newsService.selectAll(news);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum, 
                           @RequestParam(defaultValue = "10") Integer pageSize, 
                           News news) {
        PageInfo<News> pageInfo = newsService.selectPage(pageNum, pageSize, news);
        return Result.success(pageInfo);
    }
}