package com.example.service;

import com.example.entity.Reply;
import com.example.entity.User;
import com.example.mapper.ReplyMapper;
import com.example.mapper.UserMapper;
import com.example.mapper.PostMapper;
import com.example.mapper.NewsMapper;
import com.example.mapper.IntroductionMapper;
import cn.hutool.core.date.DateUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PostMapper postMapper;
    
    @Resource
    private NewsMapper newsMapper;
    
    @Resource
    private IntroductionMapper introductionMapper;

    public void add(Reply reply) {
        reply.setTime(DateUtil.now());
        // 为type字段设置默认值，防止前端未传递
        if (reply.getType() == null || reply.getType().isEmpty()) {
            reply.setType("post");
        }
        replyMapper.add(reply);
        // 根据评论类型更新对应的回复计数
        try {
            if ("post".equals(reply.getType())) {
                postMapper.incrementReplyCount(reply.getIntroductionId());
            } else if ("news".equals(reply.getType())) {
                newsMapper.incrementReplyCount(reply.getIntroductionId());
            } else if ("article".equals(reply.getType())) {
                introductionMapper.incrementReplyCount(reply.getIntroductionId());
            }
        } catch (Exception e) {
            // 记录错误但不影响评论添加
            System.err.println("更新回复计数失败: " + e.getMessage());
        }
    }

    public void deleteById(Integer id) {
        Reply reply = replyMapper.selectById(id);
        if (reply != null) {
            replyMapper.deleteById(id);
            // 根据评论类型更新对应的回复计数
            try {
                Integer targetId = reply.getIntroductionId();
                if (targetId != null) {
                    if ("post".equals(reply.getType())) {
                        postMapper.decrementReplyCount(targetId);
                    } else if ("news".equals(reply.getType())) {
                        newsMapper.decrementReplyCount(targetId);
                    } else if ("article".equals(reply.getType())) {
                        introductionMapper.decrementReplyCount(targetId);
                    }
                }
            } catch (Exception e) {
                // 记录错误但不影响回复删除
                System.err.println("更新回复计数失败: " + e.getMessage());
            }
        }
    }

    public java.util.List<Reply> selectByIntroductionId(Integer introductionId) {
        java.util.List<Reply> replies = replyMapper.selectByIntroductionId(introductionId);
        for (Reply reply : replies) {
            User user = userMapper.selectById(reply.getUserId());
            if (user != null) {
                reply.setUserName(user.getName());
                reply.setUserAvatar(user.getAvatar());
            }
        }
        return replies;
    }
    
    public java.util.List<Reply> selectByNewsId(Integer newsId) {
        java.util.List<Reply> replies = replyMapper.selectByNewsId(newsId);
        for (Reply reply : replies) {
            User user = userMapper.selectById(reply.getUserId());
            if (user != null) {
                reply.setUserName(user.getName());
                reply.setUserAvatar(user.getAvatar());
            }
        }
        return replies;
    }
    
    public java.util.List<Reply> selectByPostId(Integer postId) {
        java.util.List<Reply> replies = replyMapper.selectByPostId(postId);
        for (Reply reply : replies) {
            User user = userMapper.selectById(reply.getUserId());
            if (user != null) {
                reply.setUserName(user.getName());
                reply.setUserAvatar(user.getAvatar());
            }
        }
        return replies;
    }
    
    public java.util.List<Reply> selectByArticleId(Integer articleId) {
        java.util.List<Reply> replies = replyMapper.selectByArticleId(articleId);
        for (Reply reply : replies) {
            User user = userMapper.selectById(reply.getUserId());
            if (user != null) {
                reply.setUserName(user.getName());
                reply.setUserAvatar(user.getAvatar());
            }
        }
        return replies;
    }

    public com.github.pagehelper.PageInfo<Reply> selectPage(Integer pageNum, Integer pageSize, Integer userId, String type) {
        com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);
        java.util.List<Reply> list = replyMapper.selectPage(userId, type);
        for (Reply reply : list) {
            User user = userMapper.selectById(reply.getUserId());
            if (user != null) {
                reply.setUserName(user.getName());
                reply.setUserAvatar(user.getAvatar());
            }
        }
        return new com.github.pagehelper.PageInfo<>(list);
    }
}
