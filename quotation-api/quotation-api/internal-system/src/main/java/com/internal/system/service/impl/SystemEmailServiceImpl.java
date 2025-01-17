package com.internal.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.utils.DateUtils;
import com.internal.system.domain.SystemEmail;
import com.internal.system.mapper.SystemEmailMapper;
import com.internal.system.service.ISystemEmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 内置邮箱设置Service业务层处理
 * 
 * @author internal
 * @date 2024-11-25
 */
@Service
@AllArgsConstructor
public class SystemEmailServiceImpl extends ServiceImpl<SystemEmailMapper, SystemEmail> implements ISystemEmailService
{

    private final SystemEmailMapper systemEmailMapper;

    /**
     * 查询内置邮箱设置
     * 
     * @param id 内置邮箱设置主键
     * @return 内置邮箱设置
     */
    @Override
    public SystemEmail selectSystemEmailById(Long id)
    {
        return systemEmailMapper.selectSystemEmailById(id);
    }

    /**
     * 查询内置邮箱设置列表
     * 
     * @param systemEmail 内置邮箱设置
     * @return 内置邮箱设置
     */
    @Override
    public List<SystemEmail> selectSystemEmailList(SystemEmail systemEmail)
    {
        return systemEmailMapper.selectSystemEmailList(systemEmail);
    }

    /**
     * 新增内置邮箱设置
     * 
     * @param systemEmail 内置邮箱设置
     * @return 结果
     */
    @Override
    public int insertSystemEmail(SystemEmail systemEmail)
    {
        systemEmail.setCreateTime(DateUtils.getNowDate());
        return systemEmailMapper.insert(systemEmail);
    }

    /**
     * 修改内置邮箱设置
     * 
     * @param systemEmail 内置邮箱设置
     * @return 结果
     */
    @Override
    public int updateSystemEmail(SystemEmail systemEmail)
    {
        systemEmail.setUpdateTime(DateUtils.getNowDate());
        return systemEmailMapper.updateById(systemEmail);
    }

    /**
     * 批量删除内置邮箱设置
     * 
     * @param ids 需要删除的内置邮箱设置主键
     * @return 结果
     */
    @Override
    public int deleteSystemEmailByIds(Long[] ids)
    {
        return systemEmailMapper.deleteSystemEmailByIds(ids);
    }

    /**
     * 删除内置邮箱设置信息
     * 
     * @param id 内置邮箱设置主键
     * @return 结果
     */
    @Override
    public int deleteSystemEmailById(Long id)
    {
        return systemEmailMapper.deleteSystemEmailById(id);
    }
}
