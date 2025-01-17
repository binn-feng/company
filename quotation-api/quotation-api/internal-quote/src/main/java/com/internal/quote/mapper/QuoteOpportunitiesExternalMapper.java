package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesExternal;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-商机外采硬件小记Mapper接口
 * 
 * @author internal
 * @date 2024-10-15
 */
@Repository
public interface QuoteOpportunitiesExternalMapper extends BaseMapper<QuoteOpportunitiesExternal>
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
     * 删除报价系统-商机外采硬件小记
     * 
     * @param id 报价系统-商机外采硬件小记主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesExternalById(Long id);

    /**
     * 批量删除报价系统-商机外采硬件小记
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesExternalByIds(Long[] ids);

    /**
     * 插入报价系统-商机外采硬件小记
     * @param quoteOpportunitiesExternals quoteOpportunitiesExternals
     */
    void insertBatchDetail(List<QuoteOpportunitiesExternal> quoteOpportunitiesExternals);
}
