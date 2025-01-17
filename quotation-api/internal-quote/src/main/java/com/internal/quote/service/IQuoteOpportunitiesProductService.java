package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.domain.QuoteOpportunitiesProduct;
import com.internal.quote.dto.QuoteOpportunitiesProductSaveDTO;
import com.internal.quote.vo.QuoteOpportunitiesProductVO;

/**
 * 报价系统-商机产品平台小记Service接口
 * 
 * @author internal
 * @date 2024-10-15
 */
public interface IQuoteOpportunitiesProductService extends IService<QuoteOpportunitiesProduct>
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
     * 新增报价系统-商机产品平台小记
     * 
     * @param quoteOpportunitiesProduct 报价系统-商机产品平台小记
     * @return 结果
     */
    public int insertQuoteOpportunitiesProduct(QuoteOpportunitiesProduct quoteOpportunitiesProduct);

    /**
     * 修改报价系统-商机产品平台小记
     * 
     * @param quoteOpportunitiesProduct 报价系统-商机产品平台小记
     * @return 结果
     */
    public int updateQuoteOpportunitiesProduct(QuoteOpportunitiesProduct quoteOpportunitiesProduct);

    /**
     * 批量删除报价系统-商机产品平台小记
     * 
     * @param ids 需要删除的报价系统-商机产品平台小记主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesProductByIds(Long[] ids);

    /**
     * 删除报价系统-商机产品平台小记信息
     * 
     * @param id 报价系统-商机产品平台小记主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesProductById(Long id);

    /**
     * 获取产品详细信息列表
     * @param opportunitiesId
     * @return QuoteOpportunitiesProductVO
     */
    QuoteOpportunitiesProductVO getProductInfo(Long opportunitiesId);

    /**
     * 批量新增产品信息
     * @param quoteOpportunitiesProductList quoteOpportunitiesProductList
     */
    void saveBatchDetail(List<QuoteOpportunitiesProductSaveDTO> quoteOpportunitiesProductList, QuoteOpportunitiesDetail quoteOpportunitiesDetail);
}
