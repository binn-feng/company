package com.internal.quote.mapper;

import java.util.List;
import com.internal.quote.domain.QuoteOpportunitiesProduct;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 报价系统-商机产品平台小记Mapper接口
 * 
 * @author internal
 * @date 2024-10-15
 */
@Repository
public interface QuoteOpportunitiesProductMapper extends BaseMapper<QuoteOpportunitiesProduct>
{
    /**
     * 查询报价系统-商机产品平台小记
     * 
     * @param id 报价系统-商机产品平台小记主键
     * @return 报价系统-商机产品平台小记
     */
    public QuoteOpportunitiesProduct selectQuoteOpportunitiesProductById(Long id);

    /**
     * 查询报价系统-商机产品平台小记列表
     * 
     * @param quoteOpportunitiesProduct 报价系统-商机产品平台小记
     * @return 报价系统-商机产品平台小记集合
     */
    public List<QuoteOpportunitiesProduct> selectQuoteOpportunitiesProductList(QuoteOpportunitiesProduct quoteOpportunitiesProduct);

    /**
     * 删除报价系统-商机产品平台小记
     * 
     * @param id 报价系统-商机产品平台小记主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesProductById(Long id);

    /**
     * 批量删除报价系统-商机产品平台小记
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesProductByIds(Long[] ids);
}
