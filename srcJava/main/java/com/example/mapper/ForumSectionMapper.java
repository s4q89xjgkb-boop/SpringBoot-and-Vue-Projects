package com.example.mapper;

import com.example.entity.ForumSection;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ForumSectionMapper {
    void insert(ForumSection forumSection);
    void updateById(ForumSection forumSection);
    void deleteById(Integer id);
    ForumSection selectById(Integer id);
    List<ForumSection> selectAll(ForumSection forumSection);
}
