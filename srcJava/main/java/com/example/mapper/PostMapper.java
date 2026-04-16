package com.example.mapper;

import com.example.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    void insert(Post post);
    void updateById(Post post);
    void deleteById(Integer id);
    Post selectById(Integer id);
    List<Post> selectAll(Post post);
    void incrementLikeCount(Integer id);
    void decrementLikeCount(Integer id);
    void incrementCollectCount(Integer id);
    void decrementCollectCount(Integer id);
    void incrementReplyCount(Integer id);
    void decrementReplyCount(Integer id);
}
