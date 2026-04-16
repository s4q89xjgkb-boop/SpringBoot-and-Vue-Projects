package com.example.controller;

import com.example.common.Result;
import com.example.entity.Banner;
import com.example.service.BannerService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Resource
    BannerService bannerService;

    @PostMapping("/add")
    public Result add(@RequestBody Banner banner) {
        bannerService.add(banner);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Banner banner) {
        bannerService.update(banner);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        bannerService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        bannerService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Banner> list = bannerService.selectAll();
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Banner banner) {
        PageInfo<Banner> pageInfo = bannerService.selectPage(pageNum, pageSize, banner);
        return Result.success(pageInfo);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Banner banner = bannerService.selectById(id);
        return Result.success(banner);
    }
}
