package com.example.service;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HtmlUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.Category;
import com.example.entity.Introduction;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.AdminMapper;
import com.example.mapper.CategoryMapper;
import com.example.mapper.IntroductionMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntroductionService {

    @Resource
    IntroductionMapper introductionMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    AdminMapper adminMapper;

    public void add(Introduction introduction) {
        introduction.setTime(DateUtil.now());
        introduction.setStatus(0);
        introductionMapper.insert(introduction);
    }

    public void update(Introduction introduction) {
        introductionMapper.updateById(introduction);
    }

    public void deleteById(Integer id) {
        introductionMapper.deleteById(id);
    }

    public void deleteBatch(List<Introduction> list) {
        for (Introduction introduction : list) {
            this.deleteById(introduction.getId());
        }
    }

    public List<Introduction> selectAll(Introduction introduction) {

        List<Introduction> introductions=introductionMapper.selectAll(introduction);
        for(Introduction dbIntroduction:introductions){
            if (dbIntroduction.getContent() != null) {
                dbIntroduction.setDescription(HtmlUtil.cleanHtmlTag(dbIntroduction.getContent()));
            } else {
                dbIntroduction.setDescription("");
            }
            Integer categoryId = dbIntroduction.getCategoryId();
            Integer userId = dbIntroduction.getUserId();
            Category category = categoryMapper.selectById(categoryId);
            if (ObjectUtil.isNotEmpty(category)) {
                dbIntroduction.setCategoryTitle(category.getTitle());
            }
            if (userId != null) {
                User user = userMapper.selectById(userId);
                if (ObjectUtil.isNotEmpty(user)) {
                    dbIntroduction.setUserName(user.getName());
                    dbIntroduction.setUserAvatar(user.getAvatar());
                } else {
                    Admin admin = adminMapper.selectById(String.valueOf(userId));
                    if (ObjectUtil.isNotEmpty(admin)) {
                        dbIntroduction.setUserName(admin.getName());
                        dbIntroduction.setUserAvatar(admin.getAvatar());
                    }
                }
            }
        }
        return introductions;
    }

    public PageInfo<Introduction> selectPage(Integer pageNum, Integer pageSize, Introduction introduction) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Introduction> list = introductionMapper.selectAll(introduction);
        // 这个list里面存储了游戏攻略的原始数据（只有分类id，categoryId）
        for (Introduction dbIntroduction : list) {
            if (dbIntroduction.getContent() != null) {
                dbIntroduction.setDescription(HtmlUtil.cleanHtmlTag(dbIntroduction.getContent()));
            } else {
                dbIntroduction.setDescription("");
            }
            // 先拿到categoryId
            Integer categoryId = dbIntroduction.getCategoryId();
            Integer userId = dbIntroduction.getUserId();
            // 通过categoryId从category表里通过主键查询出分类数据
            Category category = categoryMapper.selectById(categoryId);
            if (ObjectUtil.isNotEmpty(category)) {
                // 把分类的title赋值给categoryTitle
                dbIntroduction.setCategoryTitle(category.getTitle());
            }
            if (userId != null) {
                User user = userMapper.selectById(userId);
                if (ObjectUtil.isNotEmpty(user)) {
                    dbIntroduction.setUserName(user.getName());
                    dbIntroduction.setUserAvatar(user.getAvatar());
                } else {
                    Admin admin = adminMapper.selectById(String.valueOf(userId));
                    if (ObjectUtil.isNotEmpty(admin)) {
                        dbIntroduction.setUserName(admin.getName());
                        dbIntroduction.setUserAvatar(admin.getAvatar());
                    }
                }
            }
        }
        return PageInfo.of(list);
    }
    public Introduction selectById(Integer id) {
        Introduction dbIntroduction = introductionMapper.selectById(id);
        // 增加浏览量
        if (dbIntroduction != null) {
            Integer currentViews = dbIntroduction.getViews();
            dbIntroduction.setViews(currentViews == null ? 1 : currentViews + 1);
            introductionMapper.updateById(dbIntroduction);
            
            // 先拿到categoryId
            Integer categoryId = dbIntroduction.getCategoryId();
            Integer userId = dbIntroduction.getUserId();
            // 通过categoryId从category表里通过主键查询出分类数据
            Category category = categoryMapper.selectById(categoryId);
            if (ObjectUtil.isNotEmpty(category)) {
                // 把分类的title赋值给categoryTitle
                dbIntroduction.setCategoryTitle(category.getTitle());
            }
            if (userId != null) {
                User user = userMapper.selectById(userId);
                if (ObjectUtil.isNotEmpty(user)) {
                    dbIntroduction.setUserName(user.getName());
                    dbIntroduction.setUserAvatar(user.getAvatar());
                } else {
                    Admin admin = adminMapper.selectById(String.valueOf(userId));
                    if (ObjectUtil.isNotEmpty(admin)) {
                        dbIntroduction.setUserName(admin.getName());
                        dbIntroduction.setUserAvatar(admin.getAvatar());
                    }
                }
            }
        }
        return dbIntroduction;
    }

}