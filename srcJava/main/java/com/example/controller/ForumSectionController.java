package com.example.controller;

import com.example.common.Result;
import com.example.entity.ForumSection;
import com.example.service.ForumSectionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forumSection")
public class ForumSectionController {

    @Resource
    ForumSectionService forumSectionService;

    @PostMapping("/add")
    public Result add(@RequestBody ForumSection forumSection) {
        forumSectionService.add(forumSection);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody ForumSection forumSection) {
        forumSectionService.update(forumSection);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        forumSectionService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(ForumSection forumSection) {
        List<ForumSection> list = forumSectionService.selectAll(forumSection);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ForumSection forumSection = forumSectionService.selectById(id);
        return Result.success(forumSection);
    }
}
