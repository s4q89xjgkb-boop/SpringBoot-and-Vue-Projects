package com.example.mapper;

import com.example.entity.Introduction;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface IntroductionMapper {

    List<Introduction> selectAll(Introduction introduction);

    void insert(Introduction introduction);

    void updateById(Introduction introduction);

    @Delete("delete from `introduction` where id = #{id}")
    void deleteById(Integer id);

    Introduction selectById(Integer id);

    @Update("update `introduction` set like_count = like_count + 1 where id = #{id}")
    void incrementLikeCount(Integer id);

    @Update("update `introduction` set like_count = like_count - 1 where id = #{id} and like_count > 0")
    void decrementLikeCount(Integer id);

    @Update("update `introduction` set collect_count = collect_count + 1 where id = #{id}")
    void incrementCollectCount(Integer id);

    @Update("update `introduction` set collect_count = collect_count - 1 where id = #{id} and collect_count > 0")
    void decrementCollectCount(Integer id);

    @Update("update `introduction` set reply_count = reply_count + 1 where id = #{id}")
    void incrementReplyCount(Integer id);

    @Update("update `introduction` set reply_count = reply_count - 1 where id = #{id} and reply_count > 0")
    void decrementReplyCount(Integer id);
}