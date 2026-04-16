package com.example.service;


import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Notice;
import com.example.exception.CustomerException;
import com.example.mapper.NoticeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Resource
    NoticeMapper noticeMapper;

    public void add(Notice notice) {
        Account currentUser = TokenUtils.getCurrentUser();//用户无权操作的代码
        if("user".equals(currentUser.getRole())){
            throw new CustomerException("500","你无权限进行次操作");
        }
        notice.setTime(DateUtil.now());
        noticeMapper.insert(notice);
    }

    public void update(Notice notice) {
        noticeMapper.updateById(notice);
    }

    public void deleteById(Integer id) {
        noticeMapper.deleteById(id);
    }

    public void deleteBatch(List<Notice> list) {
        for (Notice notice : list) {
            this.deleteById(notice.getId());
        }
    }
    public Notice selectById(Integer id) {
        return noticeMapper.selectById(id.toString());
    }

    public List<Notice> selectAll() {
        return noticeMapper.selectAll(null);
    }

    public PageInfo<Notice> selectPage(Integer pageNum, Integer pageSize, Notice notice) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }
}