package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesUnable;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-无法报价Mapper接口
 * 
 * @author internal
 * @date 2024-11-21
 */
@Repository
public interface QuoteOpportunitiesUnableMapper extends BaseMapper<QuoteOpportunitiesUnable>
{
    /**
     * 查询报价系统-无法报价
     * 
     * @param id 报价系统-无法报价主键
     * @return 报价系统-无法报价
     */
    public QuoteOpportunitiesUnable selectQuoteOpportunitiesUnableById(Long id);

    /**
     * 查询报价系统-无法报价列表
     * 
     * @param quoteOpportunitiesUnable 报价系统-无法报价
     * @return 报价系统-无法报价集合
     */
    public List<QuoteOpportunitiesUnable> selectQuoteOpportunitiesUnableList(QuoteOpportunitiesUnable quoteOpportunitiesUnable);

    /**
     * 新增报价系统-无法报价
     * 
     * @param quoteOpportunitiesUnable 报价系统-无法报价
     * @return 结果
     */
    public int insertQuoteOpportunitiesUnable(QuoteOpportunitiesUnable quoteOpportunitiesUnable);

    /**
     * 修改报价系统-无法报价
     * 
     * @param quoteOpportunitiesUnable 报价系统-无法报价
     * @return 结果
     */
    public int updateQuoteOpportunitiesUnable(QuoteOpportunitiesUnable quoteOpportunitiesUnable);

    /**
     * 删除报价系统-无法报价
     * 
     * @param id 报价系统-无法报价主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesUnableById(Long id);

    /**
     * 批量删除报价系统-无法报价
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesUnableByIds(Long[] ids);
}
