package com.internal.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.internal.system.domain.SystemEmail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 内置邮箱设置Mapper接口
 * 
 * @author internal
 * @date 2024-11-25
 */
@Repository
public interface SystemEmailMapper extends BaseMapper<SystemEmail>
{
    /**
     * 查询内置邮箱设置
     * 
     * @param id 内置邮箱设置主键
     * @return 内置邮箱设置
     */
    public SystemEmail selectSystemEmailById(Long id);

    /**
     * 查询内置邮箱设置列表
     * 
     * @param systemEmail 内置邮箱设置
     * @return 内置邮箱设置集合
     */
    public List<SystemEmail> selectSystemEmailList(SystemEmail systemEmail);

    /**
     * 新增内置邮箱设置
     * 
     * @param systemEmail 内置邮箱设置
     * @return 结果
     */
    public int insertSystemEmail(SystemEmail systemEmail);

    /**
     * 修改内置邮箱设置
     * 
     * @param systemEmail 内置邮箱设置
     * @return 结果
     */
    public int updateSystemEmail(SystemEmail systemEmail);

    /**
     * 删除内置邮箱设置
     * 
     * @param id 内置邮箱设置主键
     * @return 结果
     */
    public int deleteSystemEmailById(Long id);

    /**
     * 批量删除内置邮箱设置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSystemEmailByIds(Long[] ids);
}
