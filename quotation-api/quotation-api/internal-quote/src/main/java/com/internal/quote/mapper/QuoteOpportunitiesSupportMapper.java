package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesSupport;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-商机售前支持详情Mapper接口
 * 
 * @author internal
 * @date 2024-10-15
 */
@Repository
public interface QuoteOpportunitiesSupportMapper extends BaseMapper<QuoteOpportunitiesSupport>
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
     * 删除报价系统-商机售前支持详情
     * 
     * @param id 报价系统-商机售前支持详情主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesSupportById(Long id);

    /**
     * 批量删除报价系统-商机售前支持详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesSupportByIds(Long[] ids);

    /**
     * 批量保存详细信息
     * @param quoteOpportunitiesSupports
     */
    void insertBatchDetail(List<QuoteOpportunitiesSupport> quoteOpportunitiesSupports);
}
