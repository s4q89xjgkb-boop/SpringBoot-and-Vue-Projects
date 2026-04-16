package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.entity.Banner;
import com.example.exception.CustomerException;
import com.example.mapper.BannerMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService {
    @Resource
    BannerMapper bannerMapper;

    public void add(Banner banner) {
        if (StrUtil.isBlank(banner.getImg())) {
            throw new CustomerException("请上传轮播图");
        }
        if (StrUtil.isBlank(banner.getTitle())) {
            throw new CustomerException("请输入标题");
        }
        if (StrUtil.isBlank(banner.getLink())) {
            throw new CustomerException("请输入跳转链接");
        }
        banner.setTime(DateUtil.now());
        bannerMapper.insert(banner);
    }

    public void update(Banner banner) {
        if (StrUtil.isBlank(banner.getImg())) {
            throw new CustomerException("请上传轮播图");
        }
        if (StrUtil.isBlank(banner.getTitle())) {
            throw new CustomerException("请输入标题");
        }
        if (StrUtil.isBlank(banner.getLink())) {
            throw new CustomerException("请输入跳转链接");
        }
        bannerMapper.updateById(banner);
    }

    public void deleteById(Integer id) {
        bannerMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public List<Banner> selectAll() {
        return bannerMapper.selectAll(null);
    }

    public PageInfo<Banner> selectPage(Integer pageNum, Integer pageSize, Banner banner) {
        PageHelper.startPage(pageNum, pageSize);
        List<Banner> list = bannerMapper.selectAll(banner);
        return PageInfo.of(list);
    }

    public Banner selectById(Integer id) {
        List<Banner> list = bannerMapper.selectAll(null);
        for (Banner banner : list) {
            if (banner.getId().equals(id)) {
                return banner;
            }
        }
        return null;
    }
}
