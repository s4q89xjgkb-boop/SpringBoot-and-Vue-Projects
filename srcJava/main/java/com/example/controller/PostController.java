package com.example.controller;

import com.example.common.Result;
import com.example.entity.Post;
import com.example.service.PostService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    PostService postService;

    @PostMapping("/add")
    public Result add(@RequestBody Post post) {
        postService.add(post);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Post post) {
        postService.update(post);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        postService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Post post) {
        List<Post> list = postService.selectAll(post);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Post post) {
        PageInfo<Post> pageInfo = postService.selectPage(pageNum, pageSize, post);
        return Result.success(pageInfo);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Post post = postService.selectById(id);
        return Result.success(post);
    }
}
