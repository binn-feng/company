package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteOpportunitiesRoughDetail;

/**
 * 报价系统-商机粗略详细报价Service接口
 * 
 * @author internal
 * @date 2024-10-29
 */
public interface IQuoteOpportunitiesRoughDetailService extends IService<QuoteOpportunitiesRoughDetail>
{
    /**
     * 查询报价系统-商机粗略详细报价
     * 
     * @param id 报价系统-商机粗略详细报价主键
     * @return 报价系统-商机粗略详细报价
     */
    public QuoteOpportunitiesRoughDetail selectQuoteOpportunitiesRoughDetailById(Long id);

    /**
     * 查询报价系统-商机粗略详细报价列表
     * 
     * @param quoteOpportunitiesRoughDetail 报价系统-商机粗略详细报价
     * @return 报价系统-商机粗略详细报价集合
     */
    public List<QuoteOpportunitiesRoughDetail> selectQuoteOpportunitiesRoughDetailList(QuoteOpportunitiesRoughDetail quoteOpportunitiesRoughDetail);

    /**
     * 新增报价系统-商机粗略详细报价
     * 
     * @param quoteOpportunitiesRoughDetail 报价系统-商机粗略详细报价
     * @return 结果
     */
    public int insertQuoteOpportunitiesRoughDetail(QuoteOpportunitiesRoughDetail quoteOpportunitiesRoughDetail);

    /**
     * 修改报价系统-商机粗略详细报价
     * 
     * @param quoteOpportunitiesRoughDetail 报价系统-商机粗略详细报价
     * @return 结果
     */
    public int updateQuoteOpportunitiesRoughDetail(QuoteOpportunitiesRoughDetail quoteOpportunitiesRoughDetail);

    /**
     * 批量删除报价系统-商机粗略详细报价
     * 
     * @param ids 需要删除的报价系统-商机粗略详细报价主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesRoughDetailByIds(Long[] ids);

    /**
     * 删除报价系统-商机粗略详细报价信息
     * 
     * @param id 报价系统-商机粗略详细报价主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesRoughDetailById(Long id);
}
