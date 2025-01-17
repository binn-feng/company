package com.internal.manager.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.internal.common.annotation.DataScope;
import com.internal.manager.domain.ManageWarning;
import org.springframework.stereotype.Repository;

/**
 * 管理系统-预警设置Mapper接口
 * 
 * @author internal
 * @date 2024-10-09
 */
@Repository
public interface ManageWarningMapper extends BaseMapper<ManageWarning>
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
    @DataScope(userAlias = "mw")
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
     * 删除管理系统-预警设置
     * 
     * @param id 管理系统-预警设置主键
     * @return 结果
     */
    public int deleteManageWarningById(Long id);

    /**
     * 批量删除管理系统-预警设置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteManageWarningByIds(Long[] ids);
}
