package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesSelf;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-商机自研硬件小记Mapper接口
 * 
 * @author internal
 * @date 2024-10-15
 */
@Repository
public interface QuoteOpportunitiesSelfMapper extends BaseMapper<QuoteOpportunitiesSelf>
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
     * 删除报价系统-商机自研硬件小记
     * 
     * @param id 报价系统-商机自研硬件小记主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesSelfById(Long id);

    /**
     * 批量删除报价系统-商机自研硬件小记
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesSelfByIds(Long[] ids);
}
