package com.example.entity;

import java.time.LocalDateTime;

public class Collect {
    private Integer id;
    private Integer userId;
    private Integer introductionId;
    private String type;
    private LocalDateTime time;
    private String introductionTitle;
    private String introductionDescription;
    private String introductionImg;

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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getIntroductionTitle() {
        return introductionTitle;
    }

    public void setIntroductionTitle(String introductionTitle) {
        this.introductionTitle = introductionTitle;
    }

    public String getIntroductionDescription() {
        return introductionDescription;
    }

    public void setIntroductionDescription(String introductionDescription) {
        this.introductionDescription = introductionDescription;
    }

    public String getIntroductionImg() {
        return introductionImg;
    }

    public void setIntroductionImg(String introductionImg) {
        this.introductionImg = introductionImg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
