package com.example.controller;

import com.example.common.Result;
import com.example.entity.Feedback;
import com.example.service.FeedbackService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    @PostMapping("/add")
    public Result add(@RequestBody Feedback feedback) {
        feedbackService.add(feedback);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Feedback> list = feedbackService.selectAll();
        return Result.success(list);
    }

    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Integer userId) {
        List<Feedback> list = feedbackService.selectByUserId(userId);
        return Result.success(list);
    }

    @GetMapping("/count")
    public Result count() {
        int count = feedbackService.count();
        return Result.success(count);
    }
}