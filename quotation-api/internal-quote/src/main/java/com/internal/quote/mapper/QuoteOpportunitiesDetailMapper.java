package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-商机详细报价Mapper接口
 * 
 * @author internal
 * @date 2024-10-15
 */
@Repository
public interface QuoteOpportunitiesDetailMapper extends BaseMapper<QuoteOpportunitiesDetail>
{
    /**
     * 查询报价系统-商机详细报价
     * 
     * @param quoteOpportunitiesDetailQuery 报价系统-商机查询
     * @return 报价系统-商机详细报价
     */
    public QuoteOpportunitiesDetail selectQuoteOpportunitiesDetailById(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery);

    /**
     * 查询报价系统-商机详细报价列表
     * 
     * @param quoteOpportunitiesDetail 报价系统-商机详细报价
     * @return 报价系统-商机详细报价集合
     */
    public List<QuoteOpportunitiesDetail> selectQuoteOpportunitiesDetailList(QuoteOpportunitiesDetail quoteOpportunitiesDetail);

    /**
     * 删除报价系统-商机详细报价
     * 
     * @param id 报价系统-商机详细报价主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesDetailById(Long id);

    /**
     * 批量删除报价系统-商机详细报价
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesDetailByIds(Long[] ids);
}
