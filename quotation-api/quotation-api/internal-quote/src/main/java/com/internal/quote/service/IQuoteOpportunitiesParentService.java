package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunitiesParent;

/**
 * 报价系统-商机报价售前支持信息Service接口
 * 
 * @author internal
 * @date 2024-11-18
 */
public interface IQuoteOpportunitiesParentService extends IService<QuoteOpportunitiesParent>
{
    /**
     * 查询报价系统-商机报价售前支持信息
     * 
     * @param id 报价系统-商机报价售前支持信息主键
     * @return 报价系统-商机报价售前支持信息
     */
    public QuoteOpportunitiesParent selectQuoteOpportunitiesParentById(Long id);

    /**
     * 查询报价系统-商机报价售前支持信息列表
     * 
     * @param quoteOpportunitiesParent 报价系统-商机报价售前支持信息
     * @return 报价系统-商机报价售前支持信息集合
     */
    public List<QuoteOpportunitiesParent> selectQuoteOpportunitiesParentList(QuoteOpportunitiesParent quoteOpportunitiesParent);

    /**
     * 新增报价系统-商机报价售前支持信息
     * 
     * @param quoteOpportunitiesParent 报价系统-商机报价售前支持信息
     * @return 结果
     */
    public int insertQuoteOpportunitiesParent(QuoteOpportunitiesParent quoteOpportunitiesParent);

    /**
     * 修改报价系统-商机报价售前支持信息
     * 
     * @param quoteOpportunitiesParent 报价系统-商机报价售前支持信息
     * @return 结果
     */
    public int updateQuoteOpportunitiesParent(QuoteOpportunitiesParent quoteOpportunitiesParent);

    /**
     * 批量删除报价系统-商机报价售前支持信息
     * 
     * @param ids 需要删除的报价系统-商机报价售前支持信息主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesParentByIds(Long[] ids);

    /**
     * 删除报价系统-商机报价售前支持信息信息
     * 
     * @param id 报价系统-商机报价售前支持信息主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesParentById(Long id);
}
