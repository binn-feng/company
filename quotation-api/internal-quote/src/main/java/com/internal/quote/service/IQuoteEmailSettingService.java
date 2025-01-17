package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteEmailSetting;

/**
 * 报价系统-工作流邮箱设置Service接口
 * 
 * @author internal
 * @date 2024-11-25
 */
public interface IQuoteEmailSettingService extends IService<QuoteEmailSetting>
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
     * 批量删除报价系统-工作流邮箱设置
     * 
     * @param ids 需要删除的报价系统-工作流邮箱设置主键集合
     * @return 结果
     */
    public int deleteQuoteEmailSettingByIds(Long[] ids);

    /**
     * 删除报价系统-工作流邮箱设置信息
     * 
     * @param id 报价系统-工作流邮箱设置主键
     * @return 结果
     */
    public int deleteQuoteEmailSettingById(Long id);
}
