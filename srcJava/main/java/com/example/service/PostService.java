package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Admin;
import com.example.entity.ForumSection;
import com.example.entity.Post;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.AdminMapper;
import com.example.mapper.ForumSectionMapper;
import com.example.mapper.PostMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Resource
    PostMapper postMapper;

    @Resource
    ForumSectionMapper forumSectionMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    AdminMapper adminMapper;

    public void add(Post post) {
        post.setTime(DateUtil.now());
        post.setStatus(0);
        post.setLikeCount(0);
        post.setCollectCount(0);
        post.setReplyCount(0);
        post.setViews(0);
        postMapper.insert(post);
    }

    public void update(Post post) {
        postMapper.updateById(post);
    }

    public void deleteById(Integer id) {
        postMapper.deleteById(id);
    }

    public List<Post> selectAll(Post post) {
        List<Post> posts = postMapper.selectAll(post);
        for (Post dbPost : posts) {
            Integer sectionId = dbPost.getSectionId();
            Integer userId = dbPost.getUserId();
            ForumSection section = forumSectionMapper.selectById(sectionId);
            if (ObjectUtil.isNotEmpty(section)) {
                dbPost.setSectionTitle(section.getTitle());
            }
            if (userId != null) {
                User user = userMapper.selectById(userId);
                if (ObjectUtil.isNotEmpty(user)) {
                    dbPost.setUserName(user.getName());
                    dbPost.setUserAvatar(user.getAvatar());
                } else {
                    Admin admin = adminMapper.selectById(String.valueOf(userId));
                    if (ObjectUtil.isNotEmpty(admin)) {
                        dbPost.setUserName(admin.getName());
                        dbPost.setUserAvatar(admin.getAvatar());
                    }
                }
            }
        }
        return posts;
    }

    public PageInfo<Post> selectPage(Integer pageNum, Integer pageSize, Post post) {
        PageHelper.startPage(pageNum, pageSize);
        List<Post> list = postMapper.selectAll(post);
        for (Post dbPost : list) {
            Integer sectionId = dbPost.getSectionId();
            Integer userId = dbPost.getUserId();
            ForumSection section = forumSectionMapper.selectById(sectionId);
            if (ObjectUtil.isNotEmpty(section)) {
                dbPost.setSectionTitle(section.getTitle());
            }
            if (userId != null) {
                User user = userMapper.selectById(userId);
                if (ObjectUtil.isNotEmpty(user)) {
                    dbPost.setUserName(user.getName());
                    dbPost.setUserAvatar(user.getAvatar());
                } else {
                    Admin admin = adminMapper.selectById(String.valueOf(userId));
                    if (ObjectUtil.isNotEmpty(admin)) {
                        dbPost.setUserName(admin.getName());
                        dbPost.setUserAvatar(admin.getAvatar());
                    }
                }
            }
        }
        return PageInfo.of(list);
    }

    public Post selectById(Integer id) {
        Post dbPost = postMapper.selectById(id);
        if (dbPost != null) {
            Integer currentViews = dbPost.getViews();
            dbPost.setViews(currentViews == null ? 1 : currentViews + 1);
            postMapper.updateById(dbPost);
            
            Integer sectionId = dbPost.getSectionId();
            Integer userId = dbPost.getUserId();
            ForumSection section = forumSectionMapper.selectById(sectionId);
            if (ObjectUtil.isNotEmpty(section)) {
                dbPost.setSectionTitle(section.getTitle());
            }
            if (userId != null) {
                User user = userMapper.selectById(userId);
                if (ObjectUtil.isNotEmpty(user)) {
                    dbPost.setUserName(user.getName());
                    dbPost.setUserAvatar(user.getAvatar());
                } else {
                    Admin admin = adminMapper.selectById(String.valueOf(userId));
                    if (ObjectUtil.isNotEmpty(admin)) {
                        dbPost.setUserName(admin.getName());
                        dbPost.setUserAvatar(admin.getAvatar());
                    }
                }
            }
        }
        return dbPost;
    }
}
