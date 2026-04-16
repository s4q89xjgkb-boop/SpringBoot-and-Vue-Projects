package com.example.controller;

import com.example.common.Result;
import com.example.entity.History;
import com.example.service.HistoryService;
import com.example.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.Map;

/**
 * 观看历史控制器
 */
@RestController
@RequestMapping("/history")
public class HistoryController {

    @Resource
    private HistoryService historyService;

    /**
     * 添加或更新观看历史
     * @param history 历史记录
     * @return 结果
     */
    @PostMapping("/addOrUpdate")
    public Result addOrUpdate(@RequestBody History history) {
        // 从token中获取用户ID
        Integer userId = TokenUtils.getUserId();
        history.setUserId(userId);

        boolean success = historyService.addOrUpdateHistory(history);
        if (success) {
            return Result.success();
        } else {
            return Result.error("操作失败");
        }
    }

    /**
     * 分页查询历史记录
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param type 类型（可选，null表示查询所有类型）
     * @return 结果
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(required = false) String type) {
        // 从token中获取用户ID
        Integer userId = TokenUtils.getUserId();

        Map<String, Object> result = historyService.selectPageByUserId(userId, pageNum, pageSize, type);
        return Result.success(result);
    }

    /**
     * 删除用户的所有历史记录
     * @return 结果
     */
    @DeleteMapping("/deleteAll")
    public Result deleteAll() {
        // 从token中获取用户ID
        Integer userId = TokenUtils.getUserId();

        boolean success = historyService.deleteAllByUserId(userId);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 根据ID删除历史记录
     * @param id 历史记录ID
     * @return 结果
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        // 从token中获取用户ID
        Integer userId = TokenUtils.getUserId();

        boolean success = historyService.deleteById(id, userId);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }
}
