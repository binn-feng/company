package com.internal.manager.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.utils.DateUtils;
import com.internal.manager.domain.ManagerBuGoal;
import com.internal.manager.mapper.ManagerBuGoalMapper;
import com.internal.manager.service.IManagerBuGoalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理系统-目标管理Service业务层处理
 *
 * @author internal
 * @date 2024-12-30
 */
@Service
@AllArgsConstructor
public class ManagerBuGoalServiceImpl extends ServiceImpl<ManagerBuGoalMapper, ManagerBuGoal> implements IManagerBuGoalService {

    private final ManagerBuGoalMapper managerBuGoalMapper;

    /**
     * 查询管理系统-目标管理
     *
     * @param id 管理系统-目标管理主键
     * @return 管理系统-目标管理
     */
    @Override
    public ManagerBuGoal selectManagerBuGoalById(Long id) {
        return managerBuGoalMapper.selectManagerBuGoalById(id);
    }

    /**
     * 查询管理系统-目标管理列表
     *
     * @param managerBuGoal 管理系统-目标管理
     * @return 管理系统-目标管理
     */
    @Override
    public List<ManagerBuGoal> selectManagerBuGoalList(ManagerBuGoal managerBuGoal) {
        return managerBuGoalMapper.selectManagerBuGoalList(managerBuGoal);
    }

    /**
     * 新增管理系统-目标管理
     *
     * @param managerBuGoal 管理系统-目标管理
     * @return 结果
     */
    @Override
    public int insertManagerBuGoal(ManagerBuGoal managerBuGoal) {
        managerBuGoal.setCreateTime(DateUtils.getNowDate());
        return managerBuGoalMapper.insert(managerBuGoal);
    }

    /**
     * 修改管理系统-目标管理
     *
     * @param managerBuGoal 管理系统-目标管理
     * @return 结果
     */
    @Override
    public int updateManagerBuGoal(ManagerBuGoal managerBuGoal) {
        managerBuGoal.setUpdateTime(DateUtils.getNowDate());
        return managerBuGoalMapper.updateById(managerBuGoal);
    }

    /**
     * 批量删除管理系统-目标管理
     *
     * @param ids 需要删除的管理系统-目标管理主键
     * @return 结果
     */
    @Override
    public int deleteManagerBuGoalByIds(Long[] ids) {
        return managerBuGoalMapper.deleteManagerBuGoalByIds(ids);
    }

    /**
     * 删除管理系统-目标管理信息
     *
     * @param id 管理系统-目标管理主键
     * @return 结果
     */
    @Override
    public int deleteManagerBuGoalById(Long id) {
        return managerBuGoalMapper.deleteManagerBuGoalById(id);
    }
}
