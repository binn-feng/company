package com.internal.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.manager.domain.ManagerBuGoals;
import com.internal.manager.dto.ManagerBuGoalsQueryDTO;
import com.internal.manager.vo.ManagerBuGoalsVO;

import java.util.List;


/**
 * 管理系统-目标管理子Service接口
 *
 * @author internal
 * @date 2024-12-30
 */
public interface IManagerBuGoalsService extends IService<ManagerBuGoals> {
    /**
     * 查询管理系统-目标管理子
     *
     * @param id 管理系统-目标管理子主键
     * @return 管理系统-目标管理子
     */
    public ManagerBuGoals selectManagerBuGoalsById(Long id);

    /**
     * 查询管理系统-目标管理子列表
     *
     * @param managerBuGoals 管理系统-目标管理子
     * @return 管理系统-目标管理子集合
     */
    public List<ManagerBuGoalsVO> selectManagerBuGoalsList(ManagerBuGoalsQueryDTO managerBuGoals);

    /**
     * 新增管理系统-目标管理子
     *
     * @param managerBuGoals 管理系统-目标管理子
     * @return 结果
     */
    public int insertManagerBuGoals(ManagerBuGoals managerBuGoals);

    /**
     * 修改管理系统-目标管理子
     *
     * @param managerBuGoals 管理系统-目标管理子
     * @return 结果
     */
    public int updateManagerBuGoals(ManagerBuGoals managerBuGoals);

    /**
     * 批量删除管理系统-目标管理子
     *
     * @param ids 需要删除的管理系统-目标管理子主键集合
     * @return 结果
     */
    public int deleteManagerBuGoalsByIds(Long[] ids);

    /**
     * 删除管理系统-目标管理子信息
     *
     * @param id 管理系统-目标管理子主键
     * @return 结果
     */
    public int deleteManagerBuGoalsById(Long id);
}
