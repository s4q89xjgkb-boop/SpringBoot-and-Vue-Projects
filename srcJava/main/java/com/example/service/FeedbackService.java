package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Feedback;
import com.example.mapper.FeedbackMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Resource
    private FeedbackMapper feedbackMapper;

    public void add(Feedback feedback) {
        feedback.setTime(DateUtil.now());
        feedbackMapper.insert(feedback);
    }

    public List<Feedback> selectAll() {
        return feedbackMapper.selectAll();
    }

    public List<Feedback> selectByUserId(Integer userId) {
        return feedbackMapper.selectByUserId(userId);
    }

    public int count() {
        return feedbackMapper.count();
    }
}