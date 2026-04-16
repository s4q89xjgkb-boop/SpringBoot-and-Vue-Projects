package com.example.controller;

import com.example.common.Result;
import com.example.entity.Collect;
import com.example.service.CollectService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    @PostMapping("/add")
    public Result add(@RequestBody Collect collect) {
        collectService.add(collect);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody Collect collect) {
        collectService.delete(collect);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        collectService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/check")
    public Result check(@RequestParam Integer userId, @RequestParam Integer introductionId, @RequestParam(required = false, defaultValue = "article") String type) {
        boolean isCollected = collectService.check(userId, introductionId, type);
        return Result.success(isCollected);
    }

    @GetMapping("/selectAll")
    public Result selectAll(@RequestParam Integer userId, @RequestParam Integer introductionId, @RequestParam(required = false, defaultValue = "article") String type) {
        java.util.List<Collect> list = collectService.selectAll(userId, introductionId, type);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam Integer userId,
                               @RequestParam(required = false) String type) {
        PageInfo<Collect> pageInfo = collectService.selectPage(pageNum, pageSize, userId, type);
        return Result.success(pageInfo);
    }
}
