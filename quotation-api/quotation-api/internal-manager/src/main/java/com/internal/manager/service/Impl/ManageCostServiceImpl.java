package com.internal.manager.service.Impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.enums.CostType;
import com.internal.common.utils.DateUtils;
import com.internal.manager.domain.ManageCost;
import com.internal.manager.mapper.ManageCostMapper;
import com.internal.manager.service.IManageCostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 管理系统-成本设置Service业务层处理
 * 
 * @author internal
 * @date 2024-10-09
 */
@Service
@AllArgsConstructor
public class ManageCostServiceImpl extends ServiceImpl<ManageCostMapper, ManageCost> implements IManageCostService
{

    private final ManageCostMapper manageCostMapper;

    /**
     * 查询管理系统-成本设置
     * 
     * @param id 管理系统-成本设置主键
     * @return 管理系统-成本设置
     */
    @Override
    public ManageCost selectManageCostById(Long id)
    {
        return manageCostMapper.selectManageCostById(id);
    }

    /**
     * 查询管理系统-成本设置列表
     * 
     * @param manageCost 管理系统-成本设置
     * @return 管理系统-成本设置
     */
    @Override
    public List<ManageCost> selectManageCostList(ManageCost manageCost)
    {
        return manageCostMapper.selectManageCostList(manageCost);
    }

    /**
     * 新增管理系统-成本设置
     * 
     * @param manageCost 管理系统-成本设置
     * @return 结果
     */
    @Override
    public int insertManageCost(ManageCost manageCost)
    {
        manageCost.setCreateTime(DateUtils.getNowDate());
        return manageCostMapper.insert(manageCost);
    }

    /**
     * 修改管理系统-成本设置
     * 
     * @param manageCost 管理系统-成本设置
     * @return 结果
     */
    @Override
    public int updateManageCost(ManageCost manageCost)
    {
        manageCost.setUpdateTime(DateUtils.getNowDate());
        return manageCostMapper.updateById(manageCost);
    }

    /**
     * 批量删除管理系统-成本设置
     * 
     * @param ids 需要删除的管理系统-成本设置主键
     * @return 结果
     */
    @Override
    public int deleteManageCostByIds(Long[] ids)
    {
        return manageCostMapper.deleteManageCostByIds(ids);
    }

    /**
     * 删除管理系统-成本设置信息
     * 
     * @param id 管理系统-成本设置主键
     * @return 结果
     */
    @Override
    public int deleteManageCostById(Long id)
    {
        return manageCostMapper.deleteManageCostById(id);
    }

    @Override
    public Map<CostType, ManageCost> getCostMap() {
        Map<CostType, ManageCost> map = baseMapper.selectList(Wrappers.<ManageCost>lambdaQuery())
                .stream().collect(Collectors.toMap(
                        x -> CostType.valueOf(x.getType()),x -> x
                ));
        return map;
    }
}
