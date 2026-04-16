package com.example.entity;

import java.time.LocalDateTime;

public class Reply {
    private Integer id;
    private Integer userId;
    private Integer introductionId;
    private String content;
    private String time;
    private String type; // 评论类型：news（新闻评论）或 post（帖子评论）
    
    private String userName;
    private String userAvatar;
    private String introductionTitle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIntroductionId() {
        return introductionId;
    }

    public void setIntroductionId(Integer introductionId) {
        this.introductionId = introductionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getIntroductionTitle() {
        return introductionTitle;
    }

    public void setIntroductionTitle(String introductionTitle) {
        this.introductionTitle = introductionTitle;
    }
}
