package com.internal.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.common.enums.CostType;
import com.internal.manager.domain.ManageCost;

import java.util.List;
import java.util.Map;


/**
 * 管理系统-成本设置Service接口
 * 
 * @author internal
 * @date 2024-10-09
 */
public interface IManageCostService extends IService<ManageCost>
{
    /**
     * 查询管理系统-成本设置
     * 
     * @param id 管理系统-成本设置主键
     * @return 管理系统-成本设置
     */
    public ManageCost selectManageCostById(Long id);

    /**
     * 查询管理系统-成本设置列表
     * 
     * @param manageCost 管理系统-成本设置
     * @return 管理系统-成本设置集合
     */
    public List<ManageCost> selectManageCostList(ManageCost manageCost);

    /**
     * 新增管理系统-成本设置
     * 
     * @param manageCost 管理系统-成本设置
     * @return 结果
     */
    public int insertManageCost(ManageCost manageCost);

    /**
     * 修改管理系统-成本设置
     * 
     * @param manageCost 管理系统-成本设置
     * @return 结果
     */
    public int updateManageCost(ManageCost manageCost);

    /**
     * 批量删除管理系统-成本设置
     * 
     * @param ids 需要删除的管理系统-成本设置主键集合
     * @return 结果
     */
    public int deleteManageCostByIds(Long[] ids);

    /**
     * 删除管理系统-成本设置信息
     * 
     * @param id 管理系统-成本设置主键
     * @return 结果
     */
    public int deleteManageCostById(Long id);

    Map<CostType, ManageCost> getCostMap();
}
