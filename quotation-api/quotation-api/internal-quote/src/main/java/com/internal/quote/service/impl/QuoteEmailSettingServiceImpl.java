package com.internal.quote.service.impl;

import java.util.List;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteEmailSettingMapper;
import com.internal.quote.domain.QuoteEmailSetting;
import com.internal.quote.service.IQuoteEmailSettingService;

/**
 * 报价系统-工作流邮箱设置Service业务层处理
 * 
 * @author internal
 * @date 2024-11-25
 */
@Service
@AllArgsConstructor
public class QuoteEmailSettingServiceImpl extends ServiceImpl<QuoteEmailSettingMapper, QuoteEmailSetting> implements IQuoteEmailSettingService
{

    private final QuoteEmailSettingMapper quoteEmailSettingMapper;

    /**
     * 查询报价系统-工作流邮箱设置
     * 
     * @param id 报价系统-工作流邮箱设置主键
     * @return 报价系统-工作流邮箱设置
     */
    @Override
    public QuoteEmailSetting selectQuoteEmailSettingById(Long id)
    {
        return quoteEmailSettingMapper.selectQuoteEmailSettingById(id);
    }

    /**
     * 查询报价系统-工作流邮箱设置列表
     * 
     * @param quoteEmailSetting 报价系统-工作流邮箱设置
     * @return 报价系统-工作流邮箱设置
     */
    @Override
    public List<QuoteEmailSetting> selectQuoteEmailSettingList(QuoteEmailSetting quoteEmailSetting)
    {
        return quoteEmailSettingMapper.selectQuoteEmailSettingList(quoteEmailSetting);
    }

    /**
     * 新增报价系统-工作流邮箱设置
     * 
     * @param quoteEmailSetting 报价系统-工作流邮箱设置
     * @return 结果
     */
    @Override
    public int insertQuoteEmailSetting(QuoteEmailSetting quoteEmailSetting)
    {
        quoteEmailSetting.setCreateTime(DateUtils.getNowDate());
        return quoteEmailSettingMapper.insert(quoteEmailSetting);
    }

    /**
     * 修改报价系统-工作流邮箱设置
     * 
     * @param quoteEmailSetting 报价系统-工作流邮箱设置
     * @return 结果
     */
    @Override
    public int updateQuoteEmailSetting(QuoteEmailSetting quoteEmailSetting)
    {
        quoteEmailSetting.setUpdateTime(DateUtils.getNowDate());
        return quoteEmailSettingMapper.updateById(quoteEmailSetting);
    }

    /**
     * 批量删除报价系统-工作流邮箱设置
     * 
     * @param ids 需要删除的报价系统-工作流邮箱设置主键
     * @return 结果
     */
    @Override
    public int deleteQuoteEmailSettingByIds(Long[] ids)
    {
        return quoteEmailSettingMapper.deleteQuoteEmailSettingByIds(ids);
    }

    /**
     * 删除报价系统-工作流邮箱设置信息
     * 
     * @param id 报价系统-工作流邮箱设置主键
     * @return 结果
     */
    @Override
    public int deleteQuoteEmailSettingById(Long id)
    {
        return quoteEmailSettingMapper.deleteQuoteEmailSettingById(id);
    }
}
