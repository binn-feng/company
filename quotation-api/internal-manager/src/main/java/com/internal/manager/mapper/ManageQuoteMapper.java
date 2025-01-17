package com.internal.manager.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.internal.common.annotation.DataScope;
import com.internal.manager.domain.ManageQuote;
import org.springframework.stereotype.Repository;

/**
 * 管理系统-报价设置Mapper接口
 * 
 * @author internal
 * @date 2024-10-09
 */
@Repository
public interface ManageQuoteMapper extends BaseMapper<ManageQuote>
{
    /**
     * 查询管理系统-报价设置
     * 
     * @param id 管理系统-报价设置主键
     * @return 管理系统-报价设置
     */
    public ManageQuote selectManageQuoteById(Long id);

    /**
     * 查询管理系统-报价设置列表
     * 
     * @param manageQuote 管理系统-报价设置
     * @return 管理系统-报价设置集合
     */
    @DataScope(userAlias = "mq")
    public List<ManageQuote> selectManageQuoteList(ManageQuote manageQuote);

    /**
     * 新增管理系统-报价设置
     * 
     * @param manageQuote 管理系统-报价设置
     * @return 结果
     */
    public int insertManageQuote(ManageQuote manageQuote);

    /**
     * 修改管理系统-报价设置
     * 
     * @param manageQuote 管理系统-报价设置
     * @return 结果
     */
    public int updateManageQuote(ManageQuote manageQuote);

    /**
     * 删除管理系统-报价设置
     * 
     * @param id 管理系统-报价设置主键
     * @return 结果
     */
    public int deleteManageQuoteById(Long id);

    /**
     * 批量删除管理系统-报价设置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteManageQuoteByIds(Long[] ids);
}
