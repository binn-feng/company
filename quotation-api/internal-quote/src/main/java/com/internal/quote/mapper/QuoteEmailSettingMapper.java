package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteEmailSetting;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-工作流邮箱设置Mapper接口
 * 
 * @author internal
 * @date 2024-11-25
 */
@Repository
public interface QuoteEmailSettingMapper extends BaseMapper<QuoteEmailSetting>
{
    /**
     * 查询报价系统-工作流邮箱设置
     * 
     * @param id 报价系统-工作流邮箱设置主键
     * @return 报价系统-工作流邮箱设置
     */
    public QuoteEmailSetting selectQuoteEmailSettingById(Long id);

    /**
     * 查询报价系统-工作流邮箱设置列表
     * 
     * @param quoteEmailSetting 报价系统-工作流邮箱设置
     * @return 报价系统-工作流邮箱设置集合
     */
    public List<QuoteEmailSetting> selectQuoteEmailSettingList(QuoteEmailSetting quoteEmailSetting);

    /**
     * 新增报价系统-工作流邮箱设置
     * 
     * @param quoteEmailSetting 报价系统-工作流邮箱设置
     * @return 结果
     */
    public int insertQuoteEmailSetting(QuoteEmailSetting quoteEmailSetting);

    /**
     * 修改报价系统-工作流邮箱设置
     * 
     * @param quoteEmailSetting 报价系统-工作流邮箱设置
     * @return 结果
     */
    public int updateQuoteEmailSetting(QuoteEmailSetting quoteEmailSetting);

    /**
     * 删除报价系统-工作流邮箱设置
     * 
     * @param id 报价系统-工作流邮箱设置主键
     * @return 结果
     */
    public int deleteQuoteEmailSettingById(Long id);

    /**
     * 批量删除报价系统-工作流邮箱设置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteEmailSettingByIds(Long[] ids);

    QuoteEmailSetting selectByType(String code);
}
