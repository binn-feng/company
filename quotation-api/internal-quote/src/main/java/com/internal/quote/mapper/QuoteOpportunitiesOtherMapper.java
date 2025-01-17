package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesOther;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-商机其他小记Mapper接口
 * 
 * @author internal
 * @date 2024-10-15
 */
@Repository
public interface QuoteOpportunitiesOtherMapper extends BaseMapper<QuoteOpportunitiesOther>
{
    /**
     * 查询报价系统-商机其他小记
     * 
     * @param id 报价系统-商机其他小记主键
     * @return 报价系统-商机其他小记
     */
    public QuoteOpportunitiesOther selectQuoteOpportunitiesOtherById(Long id);

    /**
     * 查询报价系统-商机其他小记列表
     * 
     * @param quoteOpportunitiesOther 报价系统-商机其他小记
     * @return 报价系统-商机其他小记集合
     */
    public List<QuoteOpportunitiesOther> selectQuoteOpportunitiesOtherList(QuoteOpportunitiesOther quoteOpportunitiesOther);

    /**
     * 新增报价系统-商机其他小记
     * 
     * @param quoteOpportunitiesOther 报价系统-商机其他小记
     * @return 结果
     */
    public int insertQuoteOpportunitiesOther(QuoteOpportunitiesOther quoteOpportunitiesOther);

    /**
     * 修改报价系统-商机其他小记
     * 
     * @param quoteOpportunitiesOther 报价系统-商机其他小记
     * @return 结果
     */
    public int updateQuoteOpportunitiesOther(QuoteOpportunitiesOther quoteOpportunitiesOther);

    /**
     * 删除报价系统-商机其他小记
     * 
     * @param id 报价系统-商机其他小记主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesOtherById(Long id);

    /**
     * 批量删除报价系统-商机其他小记
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesOtherByIds(Long[] ids);
}
