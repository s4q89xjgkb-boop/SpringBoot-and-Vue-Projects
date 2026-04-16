package com.example.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
@Component

public class TokenUtils {

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;

    static AdminService staticAdminService;
    static UserService staticUserService;

    // springboot工程启动后会加载这段代码,将bean赋值给静态变量
    @PostConstruct
    public void init() {
        staticAdminService = adminService;
        staticUserService = userService;
    }

    /**
     * 生成token
     */
    public static String createToken(String data, String sign) {
        // 使用固定密钥生成token，避免因为密码问题导致的验证失败
        String secretKey = "your_secret_key_here"; // 生产环境中应该从配置文件读取
        return JWT.create().withAudience(data) // 将 userId-role 保存到 token 里面,作为载荷
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1)) // 1天后token过期,重新登录
                .sign(Algorithm.HMAC256(secretKey)); // 使用固定密钥作为 token 的密钥, HMAC256算法加密
    }

    /**
     * 获取当前登录的用户信息
     */
    public static Account getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                token = request.getParameter("token");
            }
            if (StrUtil.isBlank(token)) {
                return null;
            }
            // 拿到token 的载荷数据
            String audience = JWT.decode(token).getAudience().get(0);
            String[] split = audience.split("-");
            if (split.length < 2) {
                return null;
            }
            String userId = split[0];
            String role = split[1];
            // 根据token解析出来的userId去对应的表查询用户信息
            if ("admin".equals(role) || "super_admin".equals(role)) {
                return staticAdminService.selectById(userId);
            } else if ("user".equals(role)) {
                return staticUserService.selectById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            // 捕获所有异常，防止token无效导致系统崩溃
            return null;
        }
        return null;
    }

    /**
     * 获取当前登录用户的ID
     */
    public static Integer getUserId() {
        Account account = getCurrentUser();
        if (account != null) {
            return account.getId();
        }
        return null;
    }
}
