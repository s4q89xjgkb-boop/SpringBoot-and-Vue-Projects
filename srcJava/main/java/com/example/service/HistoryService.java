package com.example.service;

import com.example.entity.History;

import java.util.Map;

/**
 * 观看历史服务接口
 */
public interface HistoryService {

    /**
     * 添加或更新观看历史
     * @param history 历史记录
     * @return 是否成功
     */
    boolean addOrUpdateHistory(History history);

    /**
     * 根据用户ID分页查询历史记录
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param type 类型（可选，null表示查询所有类型）
     * @return 包含历史记录列表和总数的Map
     */
    Map<String, Object> selectPageByUserId(Integer userId, Integer pageNum, Integer pageSize, String type);

    /**
     * 删除用户的所有历史记录
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteAllByUserId(Integer userId);

    /**
     * 根据ID删除历史记录
     * @param id 历史记录ID
     * @param userId 用户ID（用于验证权限）
     * @return 是否成功
     */
    boolean deleteById(Integer id, Integer userId);
}
