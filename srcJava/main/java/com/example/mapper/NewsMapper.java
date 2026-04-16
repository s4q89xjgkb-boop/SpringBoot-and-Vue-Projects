package com.example.mapper;

import com.example.entity.News;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsMapper {
    void insert(News news);
    void updateById(News news);
    void deleteById(Integer id);
    News selectById(Integer id);
    List<News> selectAll(News news);
    void incrementReplyCount(Integer id);
    void decrementReplyCount(Integer id);
    void incrementLikeCount(Integer id);
    void decrementLikeCount(Integer id);
    void incrementCollectCount(Integer id);
    void decrementCollectCount(Integer id);
}