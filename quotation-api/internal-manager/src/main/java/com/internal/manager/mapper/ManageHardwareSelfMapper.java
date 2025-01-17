package com.internal.manager.mapper;

import java.util.List;

import com.internal.manager.domain.ManageHardwareSelf;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 管理系统-硬件自研Mapper接口
 * 
 * @author internal
 * @date 2024-12-19
 */
@Repository
public interface ManageHardwareSelfMapper extends BaseMapper<ManageHardwareSelf>
{
    /**
     * 查询管理系统-硬件自研
     * 
     * @param id 管理系统-硬件自研主键
     * @return 管理系统-硬件自研
     */
    public ManageHardwareSelf selectManageHardwareSelfById(Long id);

    /**
     * 查询管理系统-硬件自研列表
     * 
     * @param manageHardwareSelf 管理系统-硬件自研
     * @return 管理系统-硬件自研集合
     */
    public List<ManageHardwareSelf> selectManageHardwareSelfList(ManageHardwareSelf manageHardwareSelf);

    /**
     * 删除管理系统-硬件自研
     * 
     * @param id 管理系统-硬件自研主键
     * @return 结果
     */
    public int deleteManageHardwareSelfById(Long id);

    /**
     * 批量删除管理系统-硬件自研
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteManageHardwareSelfByIds(Long[] ids);
}
