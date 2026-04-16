package com.example.service;

import com.example.entity.Like;
import com.example.mapper.LikeMapper;
import com.example.mapper.PostMapper;
import com.example.mapper.IntroductionMapper;
import com.example.mapper.NewsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private PostMapper postMapper;

    @Resource
    private IntroductionMapper introductionMapper;

    @Resource
    private NewsMapper newsMapper;

    public void add(Like like) {
        likeMapper.add(like);
        if ("post".equals(like.getType())) {
            postMapper.incrementLikeCount(like.getIntroductionId());
        } else if ("news".equals(like.getType())) {
            newsMapper.incrementLikeCount(like.getIntroductionId());
        } else {
            introductionMapper.incrementLikeCount(like.getIntroductionId());
        }
    }

    public void delete(Like like) {
        likeMapper.delete(like);
        if ("post".equals(like.getType())) {
            postMapper.decrementLikeCount(like.getIntroductionId());
        } else if ("news".equals(like.getType())) {
            newsMapper.decrementLikeCount(like.getIntroductionId());
        } else {
            introductionMapper.decrementLikeCount(like.getIntroductionId());
        }
    }

    public void deleteById(Integer id) {
        Like like = likeMapper.selectById(id);
        if (like != null) {
            likeMapper.deleteById(id);
            if ("post".equals(like.getType())) {
                postMapper.decrementLikeCount(like.getIntroductionId());
            } else if ("news".equals(like.getType())) {
                newsMapper.decrementLikeCount(like.getIntroductionId());
            } else {
                introductionMapper.decrementLikeCount(like.getIntroductionId());
            }
        }
    }

    public boolean check(Integer userId, Integer introductionId, String type) {
        return likeMapper.check(userId, introductionId, type) > 0;
    }

    public java.util.List<Like> selectAll(Integer userId, Integer introductionId, String type) {
        return likeMapper.selectAll(userId, introductionId, type);
    }

    public com.github.pagehelper.PageInfo<Like> selectPage(Integer pageNum, Integer pageSize, Integer userId, String type) {
        com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);
        java.util.List<Like> list = likeMapper.selectPage(userId, type);
        return new com.github.pagehelper.PageInfo<>(list);
    }
}
