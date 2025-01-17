package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.domain.QuoteOpportunitiesExternal;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesExternalSaveDTO;
import com.internal.quote.vo.QuoteOpportunitiesExternalVO;

/**
 * 报价系统-商机外采硬件小记Service接口
 * 
 * @author internal
 * @date 2024-10-15
 */
public interface IQuoteOpportunitiesExternalService extends IService<QuoteOpportunitiesExternal>
{
    /**
     * 查询报价系统-商机外采硬件小记
     * 
     * @param id 报价系统-商机外采硬件小记主键
     * @return 报价系统-商机外采硬件小记
     */
    public QuoteOpportunitiesExternal selectQuoteOpportunitiesExternalById(Long id);

    /**
     * 查询报价系统-商机外采硬件小记列表
     * 
     * @param quoteOpportunitiesExternal 报价系统-商机外采硬件小记
     * @return 报价系统-商机外采硬件小记集合
     */
    public List<QuoteOpportunitiesExternal> selectQuoteOpportunitiesExternalList(QuoteOpportunitiesExternal quoteOpportunitiesExternal);

    /**
     * 新增报价系统-商机外采硬件小记
     * 
     * @param quoteOpportunitiesExternal 报价系统-商机外采硬件小记
     * @return 结果
     */
    public int insertQuoteOpportunitiesExternal(QuoteOpportunitiesExternal quoteOpportunitiesExternal);

    /**
     * 修改报价系统-商机外采硬件小记
     * 
     * @param quoteOpportunitiesExternal 报价系统-商机外采硬件小记
     * @return 结果
     */
    public int updateQuoteOpportunitiesExternal(QuoteOpportunitiesExternal quoteOpportunitiesExternal);

    /**
     * 批量删除报价系统-商机外采硬件小记
     * 
     * @param ids 需要删除的报价系统-商机外采硬件小记主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesExternalByIds(Long[] ids);

    /**
     * 删除报价系统-商机外采硬件小记信息
     * 
     * @param id 报价系统-商机外采硬件小记主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesExternalById(Long id);

    /**
     * 获取详细回显信息
     * @param quoteOpportunitiesDetailQuery
     * @return
     */
    QuoteOpportunitiesExternalVO getExternalInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery);

    /**
     * 批量新增报价系统-商机外采硬件小记
     * @param quoteOpportunitiesExternalList quoteOpportunitiesExternalList
     */
    void saveBatchDetail(List<QuoteOpportunitiesExternalSaveDTO> quoteOpportunitiesExternalList, QuoteOpportunitiesDetail quoteOpportunitiesDetail);
}
