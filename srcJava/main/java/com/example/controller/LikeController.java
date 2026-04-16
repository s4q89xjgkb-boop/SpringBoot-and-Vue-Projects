package com.example.controller;

import com.example.common.Result;
import com.example.entity.Like;
import com.example.service.LikeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Resource
    private LikeService likeService;

    @PostMapping("/add")
    public Result add(@RequestBody Like like) {
        likeService.add(like);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody Like like) {
        likeService.delete(like);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        likeService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/check")
    public Result check(@RequestParam Integer userId, @RequestParam Integer introductionId, @RequestParam(required = false, defaultValue = "article") String type) {
        boolean isLiked = likeService.check(userId, introductionId, type);
        return Result.success(isLiked);
    }

    @GetMapping("/selectAll")
    public Result selectAll(@RequestParam Integer userId, @RequestParam Integer introductionId, @RequestParam(required = false, defaultValue = "article") String type) {
        java.util.List<Like> list = likeService.selectAll(userId, introductionId, type);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam Integer userId,
                              @RequestParam(required = false) String type) {
        PageInfo<Like> pageInfo = likeService.selectPage(pageNum, pageSize, userId, type);
        return Result.success(pageInfo);
    }
}
