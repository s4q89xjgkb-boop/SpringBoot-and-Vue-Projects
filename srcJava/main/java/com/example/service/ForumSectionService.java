package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.ForumSection;
import com.example.entity.Post;
import com.example.mapper.ForumSectionMapper;
import com.example.mapper.PostMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumSectionService {

    @Resource
    ForumSectionMapper forumSectionMapper;

    @Resource
    PostMapper postMapper;

    public void add(ForumSection forumSection) {
        forumSection.setTime(DateUtil.now());
        forumSection.setPostCount(0);
        forumSectionMapper.insert(forumSection);
    }

    public void update(ForumSection forumSection) {
        forumSectionMapper.updateById(forumSection);
    }

    public void deleteById(Integer id) {
        forumSectionMapper.deleteById(id);
    }

    public List<ForumSection> selectAll(ForumSection forumSection) {
        List<ForumSection> sections = forumSectionMapper.selectAll(forumSection);
        for (ForumSection section : sections) {
            Post post = new Post();
            post.setSectionId(section.getId());
            post.setStatus(1);
            List<Post> posts = postMapper.selectAll(post);
            section.setPostCount(posts.size());
        }
        return sections;
    }

    public ForumSection selectById(Integer id) {
        return forumSectionMapper.selectById(id);
    }
}
