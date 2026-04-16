package com.example.mapper;

import com.example.entity.Like;
import java.util.List;

public interface LikeMapper {

    void add(Like like);

    void delete(Like like);

    void deleteById(Integer id);

    Like selectById(Integer id);

    int check(Integer userId, Integer introductionId, String type);

    List<Like> selectAll(Integer userId, Integer introductionId, String type);

    List<Like> selectPage(Integer userId, String type);
}
