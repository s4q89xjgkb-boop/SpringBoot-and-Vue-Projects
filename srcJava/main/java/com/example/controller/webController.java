package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class webController {

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;

    //  表示这是一个 get请求的接口
    @GetMapping("/") // 接口的路径，全局唯一的
    public Result hello() {
        return Result.success("hello world");  // 接口的返回值
    }

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount = null;
        if ("admin".equals(account.getRole())) {
            dbAccount = adminService.login(account);
        } else if ("user".equals(account.getRole())) {
            dbAccount = userService.login(account);
        } else {
            throw new CustomerException("非法请求");
        }
        return Result.success(dbAccount);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.success();
    }
    @PostMapping("/updatepassword")
    public Result updatepassword(@RequestBody Account account) {
        if ("admin".equals(account.getRole())) {
           return adminService.updatepassword(account);
        }
        if ("user".equals(account.getRole())) {
           return userService.updatepassword(account);
        }
        return Result.success();
    }

}