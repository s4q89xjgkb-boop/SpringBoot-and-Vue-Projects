package com.example.mapper;

import com.example.entity.History;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 观看历史Mapper
 */
@Mapper
public interface HistoryMapper {

    /**
     * 添加历史记录
     * @param history 历史记录
     * @return 影响行数
     */
    @Insert("INSERT INTO history (user_id, content_id, type, title, description, img, time) VALUES (#{userId}, #{contentId}, #{type}, #{title}, #{description}, #{img}, #{time})")
    Integer insert(History history);

    /**
     * 更新历史记录
     * @param history 历史记录
     * @return 影响行数
     */
    @Update("UPDATE history SET time = #{time} WHERE id = #{id}")
    Integer update(History history);

    /**
     * 根据ID查询历史记录
     * @param id 历史记录ID
     * @return 历史记录
     */
    @Select("SELECT * FROM history WHERE id = #{id}")
    History selectById(Integer id);

    /**
     * 根据用户ID和内容ID查询历史记录
     * @param userId 用户ID
     * @param contentId 内容ID
     * @param type 内容类型
     * @return 历史记录
     */
    @Select("SELECT * FROM history WHERE user_id = #{userId} AND content_id = #{contentId} AND type = #{type}")
    History selectByUserIdAndContentId(@Param("userId") Integer userId, @Param("contentId") Integer contentId, @Param("type") String type);

    /**
     * 根据用户ID和类型分页查询历史记录
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit 限制数
     * @param type 类型（可选，null表示查询所有类型）
     * @return 历史记录列表
     */
    @Select({"<script>",
            "SELECT * FROM history WHERE user_id = #{userId}",
            "<if test='type != null and type != &apos;&apos;'> AND type = #{type} </if>",
            "ORDER BY time DESC LIMIT #{offset}, #{limit}",
            "</script>"
    })
    List<History> selectPageByUserId(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("type") String type);

    /**
     * 根据用户ID和类型查询历史记录总数
     * @param userId 用户ID
     * @param type 类型（可选，null表示查询所有类型）
     * @return 总数
     */
    @Select({"<script>",
            "SELECT COUNT(*) FROM history WHERE user_id = #{userId}",
            "<if test='type != null and type != &apos;&apos;'> AND type = #{type} </if>",
            "</script>"
    })
    Integer selectCountByUserId(@Param("userId") Integer userId, @Param("type") String type);

    /**
     * 删除用户的所有历史记录
     * @param userId 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM history WHERE user_id = #{userId}")
    Integer deleteByUserId(@Param("userId") Integer userId);

    /**
     * 根据ID删除历史记录
     * @param id 历史记录ID
     * @return 影响行数
     */
    @Delete("DELETE FROM history WHERE id = #{id}")
    Integer deleteById(Integer id);
}
