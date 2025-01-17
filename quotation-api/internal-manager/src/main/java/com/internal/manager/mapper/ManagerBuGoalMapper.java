package com.internal.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.internal.manager.domain.ManagerBuGoal;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 管理系统-目标管理Mapper接口
 *
 * @author internal
 * @date 2024-12-30
 */
@Repository
public interface ManagerBuGoalMapper extends BaseMapper<ManagerBuGoal> {
    /**
     * 查询管理系统-目标管理
     *
     * @param id 管理系统-目标管理主键
     * @return 管理系统-目标管理
     */
    public ManagerBuGoal selectManagerBuGoalById(Long id);

    /**
     * 查询管理系统-目标管理列表
     *
     * @param managerBuGoal 管理系统-目标管理
     * @return 管理系统-目标管理集合
     */
    public List<ManagerBuGoal> selectManagerBuGoalList(ManagerBuGoal managerBuGoal);

    /**
     * 新增管理系统-目标管理
     *
     * @param managerBuGoal 管理系统-目标管理
     * @return 结果
     */
    public int insertManagerBuGoal(ManagerBuGoal managerBuGoal);

    /**
     * 修改管理系统-目标管理
     *
     * @param managerBuGoal 管理系统-目标管理
     * @return 结果
     */
    public int updateManagerBuGoal(ManagerBuGoal managerBuGoal);

    /**
     * 删除管理系统-目标管理
     *
     * @param id 管理系统-目标管理主键
     * @return 结果
     */
    public int deleteManagerBuGoalById(Long id);

    /**
     * 批量删除管理系统-目标管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteManagerBuGoalByIds(Long[] ids);
}
