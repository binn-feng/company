package com.internal.manager.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.utils.DateUtils;
import com.internal.manager.domain.ManagerBuGoals;
import com.internal.manager.dto.ManagerBuGoalsQueryDTO;
import com.internal.manager.mapper.ManagerBuGoalsMapper;
import com.internal.manager.service.IManagerBuGoalsService;
import com.internal.manager.vo.ManagerBuGoalsVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 管理系统-目标管理子Service业务层处理
 *
 * @author internal
 * @date 2024-12-30
 */
@Service
@AllArgsConstructor
public class ManagerBuGoalsServiceImpl extends ServiceImpl<ManagerBuGoalsMapper, ManagerBuGoals> implements IManagerBuGoalsService {

    private final ManagerBuGoalsMapper managerBuGoalsMapper;

    /**
     * 查询管理系统-目标管理子
     *
     * @param id 管理系统-目标管理子主键
     * @return 管理系统-目标管理子
     */
    @Override
    public ManagerBuGoals selectManagerBuGoalsById(Long id) {
        return managerBuGoalsMapper.selectManagerBuGoalsById(id);
    }

    /**
     * 查询管理系统-目标管理子列表
     *
     * @param managerBuGoalsQueryDTO 管理系统-目标管理子
     * @return 管理系统-目标管理子
     */
    @Override
    public List<ManagerBuGoalsVO> selectManagerBuGoalsList(ManagerBuGoalsQueryDTO managerBuGoalsQueryDTO) {
        return managerBuGoalsMapper.selectManagerBuGoalsList(managerBuGoalsQueryDTO);
    }

    /**
     * 新增管理系统-目标管理子
     *
     * @param managerBuGoals 管理系统-目标管理子
     * @return 结果
     */
    @Override
    public int insertManagerBuGoals(ManagerBuGoals managerBuGoals) {
        managerBuGoals.setCreateTime(DateUtils.getNowDate());
        return managerBuGoalsMapper.insert(managerBuGoals);
    }

    /**
     * 修改管理系统-目标管理子
     *
     * @param managerBuGoals 管理系统-目标管理子
     * @return 结果
     */
    @Override
    public int updateManagerBuGoals(ManagerBuGoals managerBuGoals) {
        managerBuGoals.setUpdateTime(DateUtils.getNowDate());
        return managerBuGoalsMapper.updateById(managerBuGoals);
    }

    /**
     * 批量删除管理系统-目标管理子
     *
     * @param ids 需要删除的管理系统-目标管理子主键
     * @return 结果
     */
    @Override
    public int deleteManagerBuGoalsByIds(Long[] ids) {
        return managerBuGoalsMapper.deleteManagerBuGoalsByIds(ids);
    }

    /**
     * 删除管理系统-目标管理子信息
     *
     * @param id 管理系统-目标管理子主键
     * @return 结果
     */
    @Override
    public int deleteManagerBuGoalsById(Long id) {
        return managerBuGoalsMapper.deleteManagerBuGoalsById(id);
    }
}
