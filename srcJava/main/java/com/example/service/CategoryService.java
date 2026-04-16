package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Category;
import com.example.entity.ForumSection;
import com.example.exception.CustomerException;
import com.example.mapper.CategoryMapper;
import com.example.mapper.ForumSectionMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    ForumSectionMapper forumSectionMapper;

    public void add(Category category) {
        categoryMapper.insert(category);
        
        ForumSection forumSection = new ForumSection();
        forumSection.setTitle(category.getTitle());
        forumSection.setDescription(category.getTitle() + "游戏讨论区");
        forumSection.setSort(100);
        forumSection.setStatus(1);
        forumSectionMapper.insert(forumSection);
    }

    public void update(Category category) {
        Category oldCategory = categoryMapper.selectById(category.getId());
        if (oldCategory != null) {
            categoryMapper.updateById(category);
            
            ForumSection querySection = new ForumSection();
            querySection.setTitle(oldCategory.getTitle());
            List<ForumSection> sections = forumSectionMapper.selectAll(querySection);
            for (ForumSection section : sections) {
                section.setTitle(category.getTitle());
                section.setDescription(category.getTitle() + "游戏讨论区");
                forumSectionMapper.updateById(section);
            }
        }
    }

    public void deleteById(Integer id) {
        Category category = categoryMapper.selectById(id);
        if (category != null) {
            ForumSection querySection = new ForumSection();
            querySection.setTitle(category.getTitle());
            List<ForumSection> sections = forumSectionMapper.selectAll(querySection);
            for (ForumSection section : sections) {
                forumSectionMapper.deleteById(section.getId());
            }
        }
        categoryMapper.deleteById(id);
    }

    public void deleteBatch(List<Category> list) {
        for (Category category : list) {
            this.deleteById(category.getId());
        }
    }
    public Category selectById(Integer id) {
        return categoryMapper.selectById(id);
    }

    public List<Category> selectAll() {
        return categoryMapper.selectAll(null);
    }

    public PageInfo<Category> selectPage(Integer pageNum, Integer pageSize, Category category) {
        PageHelper.startPage(pageNum, pageSize);
        List<Category> list = categoryMapper.selectAll(category);
        return PageInfo.of(list);
    }
}
