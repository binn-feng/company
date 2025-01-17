package com.internal.manager.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.manager.domain.ManageHardwareExt;
import com.internal.manager.domain.ManageProduct;


/**
 * 管理系统-硬件外采Service接口
 * 
 * @author internal
 * @date 2024-12-19
 */
public interface IManageHardwareExtService extends IService<ManageHardwareExt>
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
     * 新增管理系统-硬件外采
     * 
     * @param manageHardwareExt 管理系统-硬件外采
     * @return 结果
     */
    public int insertManageHardwareExt(ManageHardwareExt manageHardwareExt);

    /**
     * 修改管理系统-硬件外采
     * 
     * @param manageHardwareExt 管理系统-硬件外采
     * @return 结果
     */
    public int updateManageHardwareExt(ManageHardwareExt manageHardwareExt);

    /**
     * 批量删除管理系统-硬件外采
     * 
     * @param ids 需要删除的管理系统-硬件外采主键集合
     * @return 结果
     */
    public int deleteManageHardwareExtByIds(Long[] ids);

    /**
     * 删除管理系统-硬件外采信息
     * 
     * @param id 管理系统-硬件外采主键
     * @return 结果
     */
    public int deleteManageHardwareExtById(Long id);

    Boolean publish(ManageHardwareExt ext);

    Boolean unPublish(ManageHardwareExt ext);
}
