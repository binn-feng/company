package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteOpportunitiesCustomizable;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.dto.QuoteOpportunitiesCustomizableSaveDTO;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.vo.QuoteOpportunitiesCustomizableVO;

/**
 * 报价系统-商机定制开发小记Service接口
 * 
 * @author internal
 * @date 2024-10-15
 */
public interface IQuoteOpportunitiesCustomizableService extends IService<QuoteOpportunitiesCustomizable>
{
    /**
     * 查询报价系统-商机定制开发小记
     * 
     * @param id 报价系统-商机定制开发小记主键
     * @return 报价系统-商机定制开发小记
     */
    public QuoteOpportunitiesCustomizable selectQuoteOpportunitiesCustomizableById(Long id);

    /**
     * 查询报价系统-商机定制开发小记列表
     * 
     * @param quoteOpportunitiesCustomizable 报价系统-商机定制开发小记
     * @return 报价系统-商机定制开发小记集合
     */
    public List<QuoteOpportunitiesCustomizable> selectQuoteOpportunitiesCustomizableList(QuoteOpportunitiesCustomizable quoteOpportunitiesCustomizable);

    /**
     * 新增报价系统-商机定制开发小记
     * 
     * @param quoteOpportunitiesCustomizable 报价系统-商机定制开发小记
     * @return 结果
     */
    public int insertQuoteOpportunitiesCustomizable(QuoteOpportunitiesCustomizable quoteOpportunitiesCustomizable);

    /**
     * 修改报价系统-商机定制开发小记
     * 
     * @param quoteOpportunitiesCustomizable 报价系统-商机定制开发小记
     * @return 结果
     */
    public int updateQuoteOpportunitiesCustomizable(QuoteOpportunitiesCustomizable quoteOpportunitiesCustomizable);

    /**
     * 批量删除报价系统-商机定制开发小记
     * 
     * @param ids 需要删除的报价系统-商机定制开发小记主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesCustomizableByIds(Long[] ids);

    /**
     * 删除报价系统-商机定制开发小记信息
     * 
     * @param id 报价系统-商机定制开发小记主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesCustomizableById(Long id);

    /**
     * 批量新增报价系统-商机定制开发小记
     * @param quoteOpportunitiesCustomizableList
     */
    void saveBatchDetail(List<QuoteOpportunitiesCustomizableSaveDTO> quoteOpportunitiesCustomizableList,QuoteOpportunitiesDetail quoteOpportunitiesDetail);

    /**
     * 查询商机定制开发小记信息
     * @param quoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery
     * @return
     */
    QuoteOpportunitiesCustomizableVO getCustomizableInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery);
}
