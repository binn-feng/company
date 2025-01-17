package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteOpportunitiesUnable;
import com.internal.quote.vo.QuoteOpportunitiesUnableVO;

/**
 * 报价系统-无法报价Service接口
 * 
 * @author internal
 * @date 2024-11-21
 */
public interface IQuoteOpportunitiesUnableService extends IService<QuoteOpportunitiesUnable>
{
    /**
     * 查询报价系统-无法报价
     * 
     * @param id 报价系统-无法报价主键
     * @return 报价系统-无法报价
     */
    public QuoteOpportunitiesUnableVO selectQuoteOpportunitiesUnableById(Long id);

    /**
     * 查询报价系统-无法报价列表
     * 
     * @param quoteOpportunitiesUnable 报价系统-无法报价
     * @return 报价系统-无法报价集合
     */
    public List<QuoteOpportunitiesUnable> selectQuoteOpportunitiesUnableList(QuoteOpportunitiesUnable quoteOpportunitiesUnable);

    /**
     * 新增报价系统-无法报价
     * 
     * @param quoteOpportunitiesUnable 报价系统-无法报价
     * @return 结果
     */
    public int insertQuoteOpportunitiesUnable(QuoteOpportunitiesUnable quoteOpportunitiesUnable);

    /**
     * 修改报价系统-无法报价
     * 
     * @param quoteOpportunitiesUnable 报价系统-无法报价
     * @return 结果
     */
    public int updateQuoteOpportunitiesUnable(QuoteOpportunitiesUnable quoteOpportunitiesUnable);

    /**
     * 批量删除报价系统-无法报价
     * 
     * @param ids 需要删除的报价系统-无法报价主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesUnableByIds(Long[] ids);

    /**
     * 删除报价系统-无法报价信息
     * 
     * @param id 报价系统-无法报价主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesUnableById(Long id);
}
