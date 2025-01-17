package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.domain.QuoteOpportunitiesImplement;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesImplementSaveDTO;

/**
 * 报价系统-商机实施小记Service接口
 * 
 * @author internal
 * @date 2024-10-15
 */
public interface IQuoteOpportunitiesImplementService extends IService<QuoteOpportunitiesImplement>
{
    /**
     * 查询报价系统-商机实施小记
     * 
     * @param id 报价系统-商机实施小记主键
     * @return 报价系统-商机实施小记
     */
    public QuoteOpportunitiesImplement selectQuoteOpportunitiesImplementById(Long id);

    /**
     * 查询报价系统-商机实施小记列表
     * 
     * @param quoteOpportunitiesImplement 报价系统-商机实施小记
     * @return 报价系统-商机实施小记集合
     */
    public List<QuoteOpportunitiesImplement> selectQuoteOpportunitiesImplementList(QuoteOpportunitiesImplement quoteOpportunitiesImplement);

    /**
     * 新增报价系统-商机实施小记
     * 
     * @param quoteOpportunitiesImplement 报价系统-商机实施小记
     * @return 结果
     */
    public int insertQuoteOpportunitiesImplement(QuoteOpportunitiesImplement quoteOpportunitiesImplement);

    /**
     * 修改报价系统-商机实施小记
     * 
     * @param quoteOpportunitiesImplement 报价系统-商机实施小记
     * @return 结果
     */
    public int updateQuoteOpportunitiesImplement(QuoteOpportunitiesImplement quoteOpportunitiesImplement);

    /**
     * 批量删除报价系统-商机实施小记
     * 
     * @param ids 需要删除的报价系统-商机实施小记主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesImplementByIds(Long[] ids);

    /**
     * 删除报价系统-商机实施小记信息
     * 
     * @param id 报价系统-商机实施小记主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesImplementById(Long id);

    /**
     * 批量新增报价系统-商机实施小记
     * @param quoteOpportunitiesImplementList quoteOpportunitiesImplementList
     */
    void saveBatchDetail(List<QuoteOpportunitiesImplementSaveDTO> quoteOpportunitiesImplementList, QuoteOpportunitiesDetail quoteOpportunitiesDetail);

    /**
     * 获取回显信息
     * @param quoteOpportunitiesDetailQuery
     * @return
     */
    QuoteOpportunitiesImplement getImportInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery);
}
