package com.example.mapper;

import com.example.entity.Reply;
import java.util.List;

public interface ReplyMapper {

    void add(Reply reply);

    void deleteById(Integer id);

    Reply selectById(Integer id);

    List<Reply> selectByIntroductionId(Integer introductionId);
    
    List<Reply> selectByNewsId(Integer newsId);
    
    List<Reply> selectByPostId(Integer postId);
    
    List<Reply> selectByArticleId(Integer articleId);

    List<Reply> selectPage(Integer userId, String type);
}
