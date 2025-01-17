package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesImplement;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-商机实施小记Mapper接口
 * 
 * @author internal
 * @date 2024-10-15
 */
@Repository
public interface QuoteOpportunitiesImplementMapper extends BaseMapper<QuoteOpportunitiesImplement>
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
     * 删除报价系统-商机实施小记
     * 
     * @param id 报价系统-商机实施小记主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesImplementById(Long id);

    /**
     * 批量删除报价系统-商机实施小记
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesImplementByIds(Long[] ids);

    /**
     * 批量新增数据
     * @param quoteOpportunitiesImplements quoteOpportunitiesImplements
     */
    void insertBatchSomeColumn(List<QuoteOpportunitiesImplement> quoteOpportunitiesImplements);
}
