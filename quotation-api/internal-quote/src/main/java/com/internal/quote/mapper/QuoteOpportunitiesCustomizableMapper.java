package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesCustomizable;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-商机定制开发小记Mapper接口
 * 
 * @author internal
 * @date 2024-10-15
 */
@Repository
public interface QuoteOpportunitiesCustomizableMapper extends BaseMapper<QuoteOpportunitiesCustomizable>
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
     * 删除报价系统-商机定制开发小记
     * 
     * @param id 报价系统-商机定制开发小记主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesCustomizableById(Long id);

    /**
     * 批量删除报价系统-商机定制开发小记
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesCustomizableByIds(Long[] ids);

    /**
     * 批量新增
     * @param quoteOpportunitiesCustomizableLists quoteOpportunitiesCustomizableLists
     */
    void insertBatchDetail(List<QuoteOpportunitiesCustomizable> quoteOpportunitiesCustomizableLists);
}
