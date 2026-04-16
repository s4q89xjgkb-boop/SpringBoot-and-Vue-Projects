package com.example.controller;

import com.example.common.Result;
import com.example.entity.Introduction;
import com.example.service.IntroductionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/introduction")
public class IntroductionController {

    @Resource
    IntroductionService introductionService;
    //service有了查询方法交给control用

    @PostMapping("/add")
    public Result add(@RequestBody Introduction introduction) {  // @RequestBody 接收前端传来的 json参数
        introductionService.add(introduction);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Introduction introduction) {  // @RequestBody 接收前端传来的 json参数
        introductionService.update(introduction);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        introductionService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Introduction> list) {  //  @RequestBody 接收前端传来的 json数组
        introductionService.deleteBatch(list);
        return Result.success();
    }




    /**
     * 分页查询数据库
     * pageNum 当前页码
     * pageSize 每页个数
     * required = false不强制输入，Introductionintroduction是用于后端接受前端的实体管理员的所有数据
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Introduction introduction) {
        PageInfo<Introduction> pageInfo = introductionService.selectPage(pageNum, pageSize, introduction);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

    /**
     * 根据id查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Introduction introduction = introductionService.selectById(id);
        return Result.success(introduction);

    }

    /**
     * 查询所有数据
     */
    @GetMapping("/selectAll")
    public Result selectAll(Introduction introduction) {
        List<Introduction> list = introductionService.selectAll(introduction);
        return Result.success(list);
    }
}