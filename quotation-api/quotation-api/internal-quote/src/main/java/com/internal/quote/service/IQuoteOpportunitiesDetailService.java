package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesDetailSaveDTO;
import com.internal.quote.vo.QuoteOpportunitiesDetailVO;

/**
 * 报价系统-商机详细报价Service接口
 * 
 * @author internal
 * @date 2024-10-15
 */
public interface IQuoteOpportunitiesDetailService extends IService<QuoteOpportunitiesDetail>
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
     * 新增报价系统-商机详细报价
     * 
     * @param quoteOpportunitiesDetailSaveDTO 报价系统-商机详细报价
     * @return 结果
     */
    public int insertQuoteOpportunitiesDetail(QuoteOpportunitiesDetailSaveDTO quoteOpportunitiesDetailSaveDTO);

    /**
     * 修改报价系统-商机详细报价
     * 
     * @param quoteOpportunitiesDetail 报价系统-商机详细报价
     * @return 结果
     */
    public int updateQuoteOpportunitiesDetail(QuoteOpportunitiesDetail quoteOpportunitiesDetail);

    /**
     * 批量删除报价系统-商机详细报价
     * 
     * @param ids 需要删除的报价系统-商机详细报价主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesDetailByIds(Long[] ids);

    /**
     * 删除报价系统-商机详细报价信息
     * 
     * @param id 报价系统-商机详细报价主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesDetailById(Long id);

    /**
     * 获取详细回显信息
     * @param quoteOpportunitiesDetailQuery
     * @return
     */
    QuoteOpportunitiesDetailVO getDetailInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery);
}
