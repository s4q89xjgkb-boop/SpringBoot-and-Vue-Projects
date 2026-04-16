package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.News;
import com.example.mapper.NewsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    @Resource
    private NewsMapper newsMapper;

    public void add(News news) {
        news.setTime(DateUtil.now());
        news.setStatus(1);
        news.setViews(0);
        news.setLikeCount(0);
        news.setCollectCount(0);
        news.setReplyCount(0);
        // 管理员操作，设置默认用户ID为1（管理员）
        news.setUserId(1); // 默认管理员ID
        newsMapper.insert(news);
    }

    public void update(News news) {
        newsMapper.updateById(news);
    }

    public void deleteById(Integer id) {
        newsMapper.deleteById(id);
    }

    public List<News> selectAll(News news) {
        return newsMapper.selectAll(news);
    }

    public PageInfo<News> selectPage(Integer pageNum, Integer pageSize, News news) {
        PageHelper.startPage(pageNum, pageSize);
        List<News> list = newsMapper.selectAll(news);
        return PageInfo.of(list);
    }

    public News selectById(Integer id) {
        News dbNews = newsMapper.selectById(id);
        if (dbNews != null) {
            Integer currentViews = dbNews.getViews();
            dbNews.setViews(currentViews == null ? 1 : currentViews + 1);
            newsMapper.updateById(dbNews);
        }
        return dbNews;
    }
}