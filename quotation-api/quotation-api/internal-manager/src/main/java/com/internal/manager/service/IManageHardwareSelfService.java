package com.internal.manager.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.manager.domain.ManageHardwareSelf;

/**
 * 管理系统-硬件自研Service接口
 * 
 * @author internal
 * @date 2024-12-19
 */
public interface IManageHardwareSelfService extends IService<ManageHardwareSelf>
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
     * 新增管理系统-硬件自研
     * 
     * @param manageHardwareSelf 管理系统-硬件自研
     * @return 结果
     */
    public int insertManageHardwareSelf(ManageHardwareSelf manageHardwareSelf);

    /**
     * 修改管理系统-硬件自研
     * 
     * @param manageHardwareSelf 管理系统-硬件自研
     * @return 结果
     */
    public int updateManageHardwareSelf(ManageHardwareSelf manageHardwareSelf);

    /**
     * 批量删除管理系统-硬件自研
     * 
     * @param ids 需要删除的管理系统-硬件自研主键集合
     * @return 结果
     */
    public int deleteManageHardwareSelfByIds(Long[] ids);

    /**
     * 删除管理系统-硬件自研信息
     * 
     * @param id 管理系统-硬件自研主键
     * @return 结果
     */
    public int deleteManageHardwareSelfById(Long id);

    Boolean publish(ManageHardwareSelf self);

    Boolean unPublish(ManageHardwareSelf self);
}
