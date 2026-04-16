package com.example.controller;

import com.example.common.Result;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    NoticeService noticeService;
    //service有了查询方法交给control用

    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) {  // @RequestBody 接收前端传来的 json参数
        noticeService.add(notice);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Notice notice) {  // @RequestBody 接收前端传来的 json参数
        noticeService.update(notice);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        noticeService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Notice> list) {  //  @RequestBody 接收前端传来的 json数组
        noticeService.deleteBatch(list);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/notice/selectAll
    public Result selectAll(Notice notice) {
        List<Notice> noticeList = noticeService.selectAll();
        return Result.success(noticeList);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Notice notice = noticeService.selectById(id);
        return Result.success(notice);
    }


    /**
     * 分页查询数据库
     * pageNum 当前页码
     * pageSize 每页个数
     *required = false不强制输入，Noticenotice是用于后端接受前端的实体管理员的所有数据
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Notice notice) {
        PageInfo<Notice> pageInfo = noticeService.selectPage(pageNum, pageSize, notice);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}