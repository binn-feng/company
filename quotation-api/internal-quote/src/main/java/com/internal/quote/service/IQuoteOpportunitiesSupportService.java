package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.domain.QuoteOpportunitiesSupport;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesSupportSaveDTO;
import com.internal.quote.vo.QuoteOpportunitiesSupportVO;

/**
 * 报价系统-商机售前支持详情Service接口
 * 
 * @author internal
 * @date 2024-10-15
 */
public interface IQuoteOpportunitiesSupportService extends IService<QuoteOpportunitiesSupport>
{
    /**
     * 查询报价系统-商机售前支持详情
     * 
     * @param id 报价系统-商机售前支持详情主键
     * @return 报价系统-商机售前支持详情
     */
    public QuoteOpportunitiesSupport selectQuoteOpportunitiesSupportById(Long id);

    /**
     * 查询报价系统-商机售前支持详情列表
     * 
     * @param quoteOpportunitiesSupport 报价系统-商机售前支持详情
     * @return 报价系统-商机售前支持详情集合
     */
    public List<QuoteOpportunitiesSupport> selectQuoteOpportunitiesSupportList(QuoteOpportunitiesSupport quoteOpportunitiesSupport);

    /**
     * 新增报价系统-商机售前支持详情
     * 
     * @param quoteOpportunitiesSupport 报价系统-商机售前支持详情
     * @return 结果
     */
    public int insertQuoteOpportunitiesSupport(QuoteOpportunitiesSupport quoteOpportunitiesSupport);

    /**
     * 修改报价系统-商机售前支持详情
     * 
     * @param quoteOpportunitiesSupport 报价系统-商机售前支持详情
     * @return 结果
     */
    public int updateQuoteOpportunitiesSupport(QuoteOpportunitiesSupport quoteOpportunitiesSupport);

    /**
     * 批量删除报价系统-商机售前支持详情
     * 
     * @param ids 需要删除的报价系统-商机售前支持详情主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesSupportByIds(Long[] ids);

    /**
     * 删除报价系统-商机售前支持详情信息
     * 
     * @param id 报价系统-商机售前支持详情主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesSupportById(Long id);

    /**
     * 批量保存详细信息
     * @param quoteOpportunitiesSupportList
     */
    void saveBatchDetail(List<QuoteOpportunitiesSupportSaveDTO> quoteOpportunitiesSupportList, QuoteOpportunitiesDetail quoteOpportunitiesDetail);

    /**
     * 获取详细回显信息
     * @param quoteOpportunitiesDetailQuery 查询参数
     * @return
     */
    QuoteOpportunitiesSupportVO getSupportInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery);


}
