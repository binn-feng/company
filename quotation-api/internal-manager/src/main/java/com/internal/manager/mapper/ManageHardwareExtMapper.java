package com.internal.manager.mapper;

import java.util.List;

import com.internal.manager.domain.ManageHardwareExt;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 管理系统-硬件外采Mapper接口
 * 
 * @author internal
 * @date 2024-12-19
 */
@Repository
public interface ManageHardwareExtMapper extends BaseMapper<ManageHardwareExt>
{
    /**
     * 查询管理系统-硬件外采
     * 
     * @param id 管理系统-硬件外采主键
     * @return 管理系统-硬件外采
     */
    public ManageHardwareExt selectManageHardwareExtById(Long id);

    /**
     * 查询管理系统-硬件外采列表
     * 
     * @param manageHardwareExt 管理系统-硬件外采
     * @return 管理系统-硬件外采集合
     */
    public List<ManageHardwareExt> selectManageHardwareExtList(ManageHardwareExt manageHardwareExt);

    /**
     * 删除管理系统-硬件外采
     * 
     * @param id 管理系统-硬件外采主键
     * @return 结果
     */
    public int deleteManageHardwareExtById(Long id);

    /**
     * 批量删除管理系统-硬件外采
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteManageHardwareExtByIds(Long[] ids);
}
