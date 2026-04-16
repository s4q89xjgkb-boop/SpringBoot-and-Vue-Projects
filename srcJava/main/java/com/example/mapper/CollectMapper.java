package com.example.mapper;

import com.example.entity.Collect;
import java.util.List;

public interface CollectMapper {

    void add(Collect collect);

    void delete(Collect collect);

    void deleteById(Integer id);

    Collect selectById(Integer id);

    int check(Integer userId, Integer introductionId, String type);

    List<Collect> selectAll(Integer userId, Integer introductionId, String type);

    List<Collect> selectPage(Integer userId, String type);
}
