package com.example.controller;

import com.example.common.Result;
import com.example.entity.Reply;
import com.example.service.ReplyService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
public class ReplyController {

    @Resource
    private ReplyService replyService;

    @PostMapping("/add")
    public Result add(@RequestBody Reply reply) {
        replyService.add(reply);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        replyService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectByNewsId/{newsId}")
    public Result selectByNewsId(@PathVariable Integer newsId) {
        List<Reply> replies = replyService.selectByNewsId(newsId);
        return Result.success(replies);
    }

    @GetMapping("/selectByPostId/{postId}")
    public Result selectByPostId(@PathVariable Integer postId) {
        List<Reply> replies = replyService.selectByPostId(postId);
        return Result.success(replies);
    }

    @GetMapping("/selectByArticleId/{articleId}")
    public Result selectByArticleId(@PathVariable Integer articleId) {
        List<Reply> replies = replyService.selectByArticleId(articleId);
        return Result.success(replies);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam Integer userId,
                              @RequestParam(required = false) String type) {
        PageInfo<Reply> pageInfo = replyService.selectPage(pageNum, pageSize, userId, type);
        return Result.success(pageInfo);
    }
}
