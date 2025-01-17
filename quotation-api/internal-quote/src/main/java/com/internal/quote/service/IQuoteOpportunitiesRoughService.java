package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteOpportunitiesRough;
import com.internal.quote.domain.QuoteOpportunitiesRoughDetail;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesRoughSaveDTO;
import com.internal.quote.vo.QuoteOpportunitiesRoughVO;

/**
 * 报价系统-商机粗略报价Service接口
 * 
 * @author internal
 * @date 2024-10-17
 */
public interface IQuoteOpportunitiesRoughService extends IService<QuoteOpportunitiesRough>
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
    public int insertQuoteOpportunitiesRough(QuoteOpportunitiesRoughSaveDTO quoteOpportunitiesRough);

    /**
     * 修改报价系统-商机粗略报价
     * 
     * @param quoteOpportunitiesRough 报价系统-商机粗略报价
     * @return 结果
     */
    public int updateQuoteOpportunitiesRough(QuoteOpportunitiesRough quoteOpportunitiesRough);

    /**
     * 批量删除报价系统-商机粗略报价
     * 
     * @param ids 需要删除的报价系统-商机粗略报价主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesRoughByIds(Long[] ids);

    /**
     * 删除报价系统-商机粗略报价信息
     * 
     * @param id 报价系统-商机粗略报价主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesRoughById(Long id);

    /**
     * 获取详细信息
     * @param quoteOpportunitiesDetailQuery
     * @return
     */
    QuoteOpportunitiesRoughVO getRoughInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery);
}
