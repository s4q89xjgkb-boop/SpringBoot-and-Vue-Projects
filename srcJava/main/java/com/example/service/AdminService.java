package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomerException;
import com.example.mapper.AdminMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Resource
    AdminMapper adminMapper;

    public void add(Admin admin) {
        // 根据新的账号查询数据库  是否存在同样账号的数据
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (dbAdmin != null) {
            throw new CustomerException("账号重复");
        }
        // 默认密码
        if (StrUtil.isBlank(admin.getPassword())) {
            admin.setPassword("admin");
        }
        admin.setRole("admin");
        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        // 检查当前用户是否为超级管理员
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"super_admin".equals(currentUser.getRole())) {
            throw new CustomerException("只有超级管理员才能删除管理员账号");
        }
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Admin> list) {
        // 检查当前用户是否为超级管理员
        Account currentUser = TokenUtils.getCurrentUser();
        if (!"super_admin".equals(currentUser.getRole())) {
            throw new CustomerException("只有超级管理员才能批量删除管理员账号");
        }
        for (Admin admin : list) {
            this.deleteById(admin.getId());
        }
    }
    public Admin selectById(String id) {
        return adminMapper.selectById(id);
    }

    public List<Admin> selectAll() {
        return adminMapper.selectAll(new Admin());
    }

    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize, Admin admin) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }
    public Admin login(Account account) {
        // 验证账号是否存在
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (dbAdmin == null) {
            throw new CustomerException("账号不存在");
        }
        // 验证密码是否正确
        if (!dbAdmin.getPassword().equals(account.getPassword())) {
            throw new CustomerException("账号或密码错误");
        }
        //创建token并返回给前端，使用实际角色
        String token = TokenUtils.createToken(dbAdmin.getId() + "-" + dbAdmin.getRole(), account.getPassword());
        dbAdmin.setToken(token);
        return dbAdmin;
    }

    public Result updatepassword(Account account) {
        //判断新密码与确认的是否一致
        // 1. 校验参数非空
        if (account.getPassword() == null || account.getNewpassword() == null || account.getNewpassword2() == null) {
            return Result.error("500", "参数不能为空");
        }


        // 2. 获取当前登录用户，校验原密码是否正确
        Account currentUser = TokenUtils.getCurrentUser();
        // 注意：如果数据库密码是加密存储的，这里需要用加密工具对比（如BCrypt.checkpw）
        if (!account.getPassword().equals(currentUser.getPassword())) {
            throw new CustomerException("500", "原密码错误");
        }

        // 3. 更新密码（可选：添加BCrypt密码加密）
        Admin admin = adminMapper.selectById(currentUser.getId().toString());
        // 密码加密（推荐）
        // String encryptPassword = BCrypt.hashpw(account.getNewpassword(), BCrypt.gensalt());
        // user.setPassword(encryptPassword);
        admin.setPassword(account.getNewpassword()); // 明文存储（仅测试用，生产环境替换为加密）
        adminMapper.updateById(admin);

        // 5. 返回成功响应，前端可接收code=200
        return Result.success("密码修改成功");
    }

}