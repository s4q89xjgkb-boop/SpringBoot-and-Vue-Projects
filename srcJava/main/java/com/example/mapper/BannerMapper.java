package com.example.mapper;

import com.example.entity.Banner;

import java.util.List;

public interface BannerMapper {
    List<Banner> selectAll(Banner banner);

    void insert(Banner banner);

    void updateById(Banner banner);

    void deleteById(Integer id);

    void deleteBatch(List<Integer> ids);
}
