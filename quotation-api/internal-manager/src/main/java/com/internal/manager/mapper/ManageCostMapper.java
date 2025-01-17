package com.internal.manager.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.internal.common.annotation.DataScope;
import com.internal.manager.domain.ManageCost;
import org.springframework.stereotype.Repository;

/**
 * 管理系统-成本设置Mapper接口
 * 
 * @author internal
 * @date 2024-10-09
 */
@Repository
public interface ManageCostMapper extends BaseMapper<ManageCost>
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
    @DataScope(userAlias = "mc")
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
     * 删除管理系统-成本设置
     * 
     * @param id 管理系统-成本设置主键
     * @return 结果
     */
    public int deleteManageCostById(Long id);

    /**
     * 批量删除管理系统-成本设置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteManageCostByIds(Long[] ids);
}
