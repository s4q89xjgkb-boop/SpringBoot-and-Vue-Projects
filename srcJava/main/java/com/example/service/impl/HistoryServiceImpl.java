package com.example.service.impl;

import com.example.entity.History;
import com.example.mapper.HistoryMapper;
import com.example.service.HistoryService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 观看历史服务实现类
 */
@Service
public class HistoryServiceImpl implements HistoryService {

    @Resource
    private HistoryMapper historyMapper;

    @Override
    public boolean addOrUpdateHistory(History history) {
        // 查询是否已存在相同的历史记录
        History existingHistory = historyMapper.selectByUserIdAndContentId(
                history.getUserId(),
                history.getContentId(),
                history.getType()
        );

        if (existingHistory != null) {
            // 如果存在，更新观看时间
            existingHistory.setTime(new Date());
            return historyMapper.update(existingHistory) > 0;
        } else {
            // 如果不存在，添加新记录
            history.setTime(new Date());
            return historyMapper.insert(history) > 0;
        }
    }

    @Override
    public Map<String, Object> selectPageByUserId(Integer userId, Integer pageNum, Integer pageSize, String type) {
        Map<String, Object> result = new HashMap<>();

        // 计算偏移量
        Integer offset = (pageNum - 1) * pageSize;

        // 查询历史记录列表
        List<History> historyList = historyMapper.selectPageByUserId(userId, offset, pageSize, type);

        // 查询总数
        Integer total = historyMapper.selectCountByUserId(userId, type);

        result.put("list", historyList);
        result.put("total", total);

        return result;
    }

    @Override
    public boolean deleteAllByUserId(Integer userId) {
        return historyMapper.deleteByUserId(userId) > 0;
    }

    @Override
    public boolean deleteById(Integer id, Integer userId) {
        // 查询历史记录
        History history = historyMapper.selectById(id);
        if (history == null) {
            return false;
        }

        // 验证权限，确保用户只能删除自己的历史记录
        if (!history.getUserId().equals(userId)) {
            return false;
        }

        return historyMapper.deleteById(id) > 0;
    }
}
