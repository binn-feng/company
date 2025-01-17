package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesRough;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-商机粗略报价Mapper接口
 * 
 * @author internal
 * @date 2024-10-17
 */
@Repository
public interface QuoteOpportunitiesRoughMapper extends BaseMapper<QuoteOpportunitiesRough>
{
    /**
     * 查询报价系统-商机粗略报价
     * 
     * @param id 报价系统-商机粗略报价主键
     * @return 报价系统-商机粗略报价
     */
    public QuoteOpportunitiesRough selectQuoteOpportunitiesRoughById(Long id);

    /**
     * 查询报价系统-商机粗略报价列表
     * 
     * @param quoteOpportunitiesRough 报价系统-商机粗略报价
     * @return 报价系统-商机粗略报价集合
     */
    public List<QuoteOpportunitiesRough> selectQuoteOpportunitiesRoughList(QuoteOpportunitiesRough quoteOpportunitiesRough);

    /**
     * 新增报价系统-商机粗略报价
     * 
     * @param quoteOpportunitiesRough 报价系统-商机粗略报价
     * @return 结果
     */
    public int insertQuoteOpportunitiesRough(QuoteOpportunitiesRough quoteOpportunitiesRough);

    /**
     * 修改报价系统-商机粗略报价
     * 
     * @param quoteOpportunitiesRough 报价系统-商机粗略报价
     * @return 结果
     */
    public int updateQuoteOpportunitiesRough(QuoteOpportunitiesRough quoteOpportunitiesRough);

    /**
     * 删除报价系统-商机粗略报价
     * 
     * @param id 报价系统-商机粗略报价主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesRoughById(Long id);

    /**
     * 批量删除报价系统-商机粗略报价
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesRoughByIds(Long[] ids);
}
