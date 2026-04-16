package com.example.service;

import com.example.entity.Collect;
import com.example.mapper.CollectMapper;
import com.example.mapper.PostMapper;
import com.example.mapper.IntroductionMapper;
import com.example.mapper.NewsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CollectService {

    @Resource
    private CollectMapper collectMapper;

    @Resource
    private PostMapper postMapper;

    @Resource
    private IntroductionMapper introductionMapper;

    @Resource
    private NewsMapper newsMapper;

    public void add(Collect collect) {
        collectMapper.add(collect);
        if ("post".equals(collect.getType())) {
            postMapper.incrementCollectCount(collect.getIntroductionId());
        } else if ("news".equals(collect.getType())) {
            newsMapper.incrementCollectCount(collect.getIntroductionId());
        } else {
            introductionMapper.incrementCollectCount(collect.getIntroductionId());
        }
    }

    public void delete(Collect collect) {
        collectMapper.delete(collect);
        if ("post".equals(collect.getType())) {
            postMapper.decrementCollectCount(collect.getIntroductionId());
        } else if ("news".equals(collect.getType())) {
            newsMapper.decrementCollectCount(collect.getIntroductionId());
        } else {
            introductionMapper.decrementCollectCount(collect.getIntroductionId());
        }
    }

    public void deleteById(Integer id) {
        Collect collect = collectMapper.selectById(id);
        if (collect != null) {
            collectMapper.deleteById(id);
            // 根据收藏类型更新对应的收藏计数
            try {
                Integer targetId = collect.getIntroductionId();
                if (targetId != null) {
                    if ("post".equals(collect.getType())) {
                        postMapper.decrementCollectCount(targetId);
                    } else if ("news".equals(collect.getType())) {
                        newsMapper.decrementCollectCount(targetId);
                    } else {
                        introductionMapper.decrementCollectCount(targetId);
                    }
                }
            } catch (Exception e) {
                // 记录错误但不影响取消收藏
                System.err.println("更新收藏计数失败: " + e.getMessage());
            }
        }
    }

    public boolean check(Integer userId, Integer introductionId, String type) {
        return collectMapper.check(userId, introductionId, type) > 0;
    }

    public java.util.List<Collect> selectAll(Integer userId, Integer introductionId, String type) {
        return collectMapper.selectAll(userId, introductionId, type);
    }

    public com.github.pagehelper.PageInfo<Collect> selectPage(Integer pageNum, Integer pageSize, Integer userId, String type) {
        com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);
        java.util.List<Collect> list = collectMapper.selectPage(userId, type);
        return new com.github.pagehelper.PageInfo<>(list);
    }
}
