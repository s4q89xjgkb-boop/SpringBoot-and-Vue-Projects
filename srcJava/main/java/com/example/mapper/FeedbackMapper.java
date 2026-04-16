package com.example.mapper;

import com.example.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedbackMapper {
    // 添加反馈
    int insert(Feedback feedback);
    
    // 查询所有反馈
    List<Feedback> selectAll();
    
    // 根据用户ID查询反馈
    List<Feedback> selectByUserId(@Param("userId") Integer userId);
    
    // 查询反馈总数
    int count();
}