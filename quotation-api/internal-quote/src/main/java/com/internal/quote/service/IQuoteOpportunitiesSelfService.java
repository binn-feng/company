package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.domain.QuoteOpportunitiesSelf;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesSelfSaveDTO;
import com.internal.quote.vo.QuoteOpportunitiesSelfVO;

/**
 * 报价系统-商机自研硬件小记Service接口
 * 
 * @author internal
 * @date 2024-10-15
 */
public interface IQuoteOpportunitiesSelfService extends IService<QuoteOpportunitiesSelf>
{
    /**
     * 查询报价系统-商机自研硬件小记
     * 
     * @param id 报价系统-商机自研硬件小记主键
     * @return 报价系统-商机自研硬件小记
     */
    public QuoteOpportunitiesSelf selectQuoteOpportunitiesSelfById(Long id);

    /**
     * 查询报价系统-商机自研硬件小记列表
     * 
     * @param quoteOpportunitiesSelf 报价系统-商机自研硬件小记
     * @return 报价系统-商机自研硬件小记集合
     */
    public List<QuoteOpportunitiesSelf> selectQuoteOpportunitiesSelfList(QuoteOpportunitiesSelf quoteOpportunitiesSelf);

    /**
     * 新增报价系统-商机自研硬件小记
     * 
     * @param quoteOpportunitiesSelf 报价系统-商机自研硬件小记
     * @return 结果
     */
    public int insertQuoteOpportunitiesSelf(QuoteOpportunitiesSelf quoteOpportunitiesSelf);

    /**
     * 修改报价系统-商机自研硬件小记
     * 
     * @param quoteOpportunitiesSelf 报价系统-商机自研硬件小记
     * @return 结果
     */
    public int updateQuoteOpportunitiesSelf(QuoteOpportunitiesSelf quoteOpportunitiesSelf);

    /**
     * 批量删除报价系统-商机自研硬件小记
     * 
     * @param ids 需要删除的报价系统-商机自研硬件小记主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesSelfByIds(Long[] ids);

    /**
     * 删除报价系统-商机自研硬件小记信息
     * 
     * @param id 报价系统-商机自研硬件小记主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesSelfById(Long id);

    /**
     * 获取详细回显信息
     * @param quoteOpportunitiesDetailQuery
     * @return
     */
    QuoteOpportunitiesSelfVO getSelfInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery);

    /**
     *  批量保存
     * @param quoteOpportunitiesSelfList quoteOpportunitiesSelfList
     */
    void saveBatchDetail(List<QuoteOpportunitiesSelfSaveDTO> quoteOpportunitiesSelfList, QuoteOpportunitiesDetail quoteOpportunitiesDetail);
}
