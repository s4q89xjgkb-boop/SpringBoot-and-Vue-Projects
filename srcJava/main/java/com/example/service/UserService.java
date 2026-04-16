package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.User; // 普通用户实体类
import com.example.exception.CustomerException;
import com.example.mapper.UserMapper; // 普通用户Mapper
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    // ========== 其他基础CRUD方法（参考AdminService，略） ==========
    public void add(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (dbUser != null) {
            throw new CustomerException("用户名已存在");
        }
        if (StrUtil.isBlank(user.getPassword())) {
            user.setPassword("123456"); // 普通用户默认密码
        }
        user.setRole("user"); // 标记为普通用户
        userMapper.insert(user);
    }

    public void register(User user) {
        add(user);
    }

    public void update(User user) {
        userMapper.updateById(user);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<User> list) {
        for (User user : list) {
            this.deleteById(user.getId());
        }
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public List<User> selectAll() {
        return userMapper.selectAll(null);
    }

    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize, User user) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    // ========== 重点修复：前台用户登录核心方法 ==========
    public User login(Account account) {
        // 1. 补充非空校验（避免空指针，友好提示）
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())) {
            throw new CustomerException("用户名或密码不能为空");
        }
        // 2. 验证用户是否存在
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (dbUser == null) {
            throw new CustomerException("用户名不存在");
        }
        // 3. 验证密码（若数据库密码加密，需替换为BCrypt加密对比，示例见注释）
        // 【明文对比（测试用）】
        if (!dbUser.getPassword().equals(account.getPassword())) {
            throw new CustomerException("用户名或密码错误");
        }
        // 【加密对比（生产环境推荐）】
        // if (!BCrypt.checkpw(account.getPassword(), dbUser.getPassword())) {
        //     throw new CustomerException("用户名或密码错误");
        // }

        // 4. 创建token并返回给前端
        String token = TokenUtils.createToken(dbUser.getId() + "-user", dbUser.getPassword());
        dbUser.setToken(token);
        // 5. 隐藏敏感字段（密码），避免前端解析异常
        dbUser.setPassword(null);
        return dbUser;
    }

    // ========== 修复：用户改密码方法（避免影响登录态） ==========
    public Result updatepassword(Account account) {
        // 1. 严格校验参数非空（兼容空字符串）
        if (StrUtil.isBlank(account.getPassword()) || StrUtil.isBlank(account.getNewpassword()) || StrUtil.isBlank(account.getNewpassword2())) {
            return Result.error("500", "参数不能为空");
        }
        // 2. 校验新密码一致性
        if (!account.getNewpassword().equals(account.getNewpassword2())) {
            return Result.error("500", "两次新密码输入不一致");
        }
        // 3. 获取当前登录用户（User），校验原密码
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            throw new CustomerException("500", "登录状态失效，请重新登录");
        }
        // 从数据库查最新用户信息（避免Token中密码不一致）
        User dbUser = userMapper.selectById(currentUser.getId());
        if (dbUser == null) {
            throw new CustomerException("500", "用户不存在");
        }
        // 4. 校验原密码（加密场景需替换为BCrypt.checkpw）
        if (!dbUser.getPassword().equals(account.getPassword())) {
            throw new CustomerException("500", "原密码错误");
        }
        // 5. 更新密码（加密场景需替换为BCrypt.hashpw）
        dbUser.setPassword(account.getNewpassword());
        userMapper.updateById(dbUser);
        return Result.success("密码修改成功");
    }
}