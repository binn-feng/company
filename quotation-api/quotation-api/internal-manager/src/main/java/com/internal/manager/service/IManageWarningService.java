package com.internal.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.manager.domain.ManageWarning;

import java.util.List;

/**
 * 管理系统-预警设置Service接口
 * 
 * @author internal
 * @date 2024-10-09
 */
public interface IManageWarningService extends IService<ManageWarning>
{
    /**
     * 查询管理系统-预警设置
     * 
     * @param id 管理系统-预警设置主键
     * @return 管理系统-预警设置
     */
    public ManageWarning selectManageWarningById(Long id);

    /**
     * 查询管理系统-预警设置列表
     * 
     * @param manageWarning 管理系统-预警设置
     * @return 管理系统-预警设置集合
     */
    public List<ManageWarning> selectManageWarningList(ManageWarning manageWarning);

    /**
     * 新增管理系统-预警设置
     * 
     * @param manageWarning 管理系统-预警设置
     * @return 结果
     */
    public int insertManageWarning(ManageWarning manageWarning);

    /**
     * 修改管理系统-预警设置
     * 
     * @param manageWarning 管理系统-预警设置
     * @return 结果
     */
    public int updateManageWarning(ManageWarning manageWarning);

    /**
     * 批量删除管理系统-预警设置
     * 
     * @param ids 需要删除的管理系统-预警设置主键集合
     * @return 结果
     */
    public int deleteManageWarningByIds(Long[] ids);

    /**
     * 删除管理系统-预警设置信息
     * 
     * @param id 管理系统-预警设置主键
     * @return 结果
     */
    public int deleteManageWarningById(Long id);

    ManageWarning getProfit();
}
