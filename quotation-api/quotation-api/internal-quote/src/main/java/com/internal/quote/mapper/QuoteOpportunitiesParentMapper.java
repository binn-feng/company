package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesParent;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-商机报价售前支持信息Mapper接口
 * 
 * @author internal
 * @date 2024-11-18
 */
@Repository
public interface QuoteOpportunitiesParentMapper extends BaseMapper<QuoteOpportunitiesParent>
{
    /**
     * 查询报价系统-商机报价售前支持信息
     * 
     * @param id 报价系统-商机报价售前支持信息主键
     * @return 报价系统-商机报价售前支持信息
     */
    public QuoteOpportunitiesParent selectQuoteOpportunitiesParentById(Long id);

    /**
     * 查询报价系统-商机报价售前支持信息列表
     * 
     * @param quoteOpportunitiesParent 报价系统-商机报价售前支持信息
     * @return 报价系统-商机报价售前支持信息集合
     */
    public List<QuoteOpportunitiesParent> selectQuoteOpportunitiesParentList(QuoteOpportunitiesParent quoteOpportunitiesParent);

    /**
     * 删除报价系统-商机报价售前支持信息
     * 
     * @param id 报价系统-商机报价售前支持信息主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesParentById(Long id);

    /**
     * 批量删除报价系统-商机报价售前支持信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesParentByIds(Long[] ids);
}
