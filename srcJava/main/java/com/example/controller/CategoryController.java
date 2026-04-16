package com.example.controller;

import com.example.common.Result;
import com.example.entity.Category;
import com.example.service.CategoryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    CategoryService categoryService;
    //service有了查询方法交给control用

    @PostMapping("/add")
    public Result add(@RequestBody Category category) {  // @RequestBody 接收前端传来的 json参数
        categoryService.add(category);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Category category) {  // @RequestBody 接收前端传来的 json参数
        categoryService.update(category);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {  // @PathVariable 接收前端传来的路径参数
        categoryService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Category> list) {  //  @RequestBody 接收前端传来的 json数组
        categoryService.deleteBatch(list);
        return Result.success();
    }

    @GetMapping("/selectAll")  //   完整的请求路径：http://ip:port/category/selectAll
    public Result selectAll(Category category) {
        List<Category> categoryList = categoryService.selectAll();
        return Result.success(categoryList);
    }


    /**
     * 分页查询数据库
     * pageNum 当前页码
     * pageSize 每页个数
     *required = false不强制输入，Categorycategory是用于后端接受前端的实体管理员的所有数据
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Category category) {
        PageInfo<Category> pageInfo = categoryService.selectPage(pageNum, pageSize, category);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }

}