package com.internal.quote.service.impl;

import java.util.List;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteOpportunitiesParentMapper;
import com.internal.quote.domain.QuoteOpportunitiesParent;
import com.internal.quote.service.IQuoteOpportunitiesParentService;

/**
 * 报价系统-商机报价售前支持信息Service业务层处理
 * 
 * @author internal
 * @date 2024-11-18
 */
@Service
@AllArgsConstructor
public class QuoteOpportunitiesParentServiceImpl extends ServiceImpl<QuoteOpportunitiesParentMapper, QuoteOpportunitiesParent> implements IQuoteOpportunitiesParentService
{

    private final QuoteOpportunitiesParentMapper quoteOpportunitiesParentMapper;

    /**
     * 查询报价系统-商机报价售前支持信息
     * 
     * @param id 报价系统-商机报价售前支持信息主键
     * @return 报价系统-商机报价售前支持信息
     */
    @Override
    public QuoteOpportunitiesParent selectQuoteOpportunitiesParentById(Long id)
    {
        return quoteOpportunitiesParentMapper.selectQuoteOpportunitiesParentById(id);
    }

    /**
     * 查询报价系统-商机报价售前支持信息列表
     * 
     * @param quoteOpportunitiesParent 报价系统-商机报价售前支持信息
     * @return 报价系统-商机报价售前支持信息
     */
    @Override
    public List<QuoteOpportunitiesParent> selectQuoteOpportunitiesParentList(QuoteOpportunitiesParent quoteOpportunitiesParent)
    {
        return quoteOpportunitiesParentMapper.selectQuoteOpportunitiesParentList(quoteOpportunitiesParent);
    }

    /**
     * 新增报价系统-商机报价售前支持信息
     * 
     * @param quoteOpportunitiesParent 报价系统-商机报价售前支持信息
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesParent(QuoteOpportunitiesParent quoteOpportunitiesParent)
    {
        quoteOpportunitiesParent.setCreateTime(DateUtils.getNowDate());
        return quoteOpportunitiesParentMapper.insert(quoteOpportunitiesParent);
    }

    /**
     * 修改报价系统-商机报价售前支持信息
     * 
     * @param quoteOpportunitiesParent 报价系统-商机报价售前支持信息
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesParent(QuoteOpportunitiesParent quoteOpportunitiesParent)
    {
        quoteOpportunitiesParent.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesParentMapper.updateById(quoteOpportunitiesParent);
    }

    /**
     * 批量删除报价系统-商机报价售前支持信息
     * 
     * @param ids 需要删除的报价系统-商机报价售前支持信息主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesParentByIds(Long[] ids)
    {
        return quoteOpportunitiesParentMapper.deleteQuoteOpportunitiesParentByIds(ids);
    }

    /**
     * 删除报价系统-商机报价售前支持信息信息
     * 
     * @param id 报价系统-商机报价售前支持信息主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesParentById(Long id)
    {
        return quoteOpportunitiesParentMapper.deleteQuoteOpportunitiesParentById(id);
    }
}
