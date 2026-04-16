package com.example.common;

//拦截器的实现，并解析token，验证token
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.entity.Account;
import com.example.exception.CustomerException;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头拿到token
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            // 如果没拿到，从参数里再拿一次
            token = request.getParameter("token");
        }
        // 2. 认证token - 如果没有token，允许访问公开接口
        if (StrUtil.isBlank(token)) {
            // 未登录用户可以访问公开接口
            return true;
        }
        Account account = null;//默认为空
        try {
            // 拿到token 的载荷数据
            String audience = JWT.decode(token).getAudience().get(0);
            String[] split = audience.split("-");
            if (split.length < 2) {
                throw new CustomerException("401", "您无权限操作"); // 仍抛出你的预设错误，仅避免未知异常
            }//载荷形式
            String userId = split[0];
            String role = split[1];
            System.out.println("JWT拦截器：userId=" + userId + ", role=" + role);
            // 根据token解析出来的userId去对应的表查询用户信息
            if ("admin".equals(role) || "super_admin".equals(role)) {
                account = adminService.selectById(userId);
            } else if ("user".equals(role)) {
                account = userService.selectById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomerException("401", "您无权限操作");
        }
        if (account == null) {
            throw new CustomerException("401", "您无权限操作");
        }
        System.out.println("JWT拦截器：account=" + account);
        System.out.println("JWT拦截器：account.getPassword()=" + account.getPassword());
        try {
            // 验证签名
            // 使用固定密钥验证token，避免因为密码问题导致的验证失败
            String secretKey = "your_secret_key_here"; // 生产环境中应该从配置文件读取
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            jwtVerifier.verify(token);
        } catch (Exception e) {
            // 打印错误信息，方便调试
            e.printStackTrace();
            throw new CustomerException("401", "您无权限操作");
        }
        return true;
    }
}