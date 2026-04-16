package com.example.controller;

import com.example.common.Result;
import com.example.entity.Apply;
import com.example.service.ApplyService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apply")
public class ApplyController {

    @Resource
    ApplyService applyService;
    //service有了查询方法交给control用

    @PostMapping("/add")
    public Result add(@RequestBody Apply apply) {  // @RequestBody 接收前端传来的 json参数
        applyService.add(apply);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Apply apply) {  // @RequestBody 接收前端传来的 json参数
        applyService.update(apply);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        applyService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Apply> list) {  //  @RequestBody 接收前端传来的 json数组
        applyService.deleteBatch(list);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/apply/selectAll
    public Result selectAll(Apply apply) {
        List<Apply> applyList = applyService.selectAll();
        return Result.success(applyList);
    }


    /**
     * 分页查询数据库
     * pageNum 当前页码
     * pageSize 每页个数
     *required = false不强制输入，Applyapply是用于后端接受前端的实体管理员的所有数据
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Apply apply) {
        PageInfo<Apply> pageInfo = applyService.selectPage(pageNum, pageSize, apply);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}