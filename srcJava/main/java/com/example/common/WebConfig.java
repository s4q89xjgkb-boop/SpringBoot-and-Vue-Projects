package com.example.common;

import com.example.entity.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration//一个配置类
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")//设置排除请求
                .excludePathPatterns("/login", "/register", "/files/**", "/imgs/**", "/introduction/**", "/notice/**", "/category/**", "/banner/**", "/front/**");//排除前台页面和认证接口
    }

    @Bean
    public JWTInterceptor jwtInterceptor() {
        return new JWTInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射静态资源，让前端可以直接访问files目录下的文件
        // 访问路径：http://localhost:1306/files/xxx.jpg
        // 实际路径：d:/lunwen/doc/code2025/files/xxx.jpg
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:d:/lunwen/doc/code2025/files/");
    }

}
