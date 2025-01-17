package com.internal.quote.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.domain.QuoteOpportunitiesOther;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesOtherSaveDTO;
import com.internal.quote.vo.QuoteOpportunitiesOtherVO;

/**
 * 报价系统-商机其他小记Service接口
 * 
 * @author internal
 * @date 2024-10-15
 */
public interface IQuoteOpportunitiesOtherService extends IService<QuoteOpportunitiesOther>
{
    /**
     * 查询报价系统-商机其他小记
     * 
     * @param id 报价系统-商机其他小记主键
     * @return 报价系统-商机其他小记
     */
    public QuoteOpportunitiesOther selectQuoteOpportunitiesOtherById(Long id);

    /**
     * 查询报价系统-商机其他小记列表
     * 
     * @param quoteOpportunitiesOther 报价系统-商机其他小记
     * @return 报价系统-商机其他小记集合
     */
    public List<QuoteOpportunitiesOther> selectQuoteOpportunitiesOtherList(QuoteOpportunitiesOther quoteOpportunitiesOther);

    /**
     * 新增报价系统-商机其他小记
     * 
     * @param quoteOpportunitiesOther 报价系统-商机其他小记
     * @return 结果
     */
    public int insertQuoteOpportunitiesOther(QuoteOpportunitiesOther quoteOpportunitiesOther);

    /**
     * 修改报价系统-商机其他小记
     * 
     * @param quoteOpportunitiesOther 报价系统-商机其他小记
     * @return 结果
     */
    public int updateQuoteOpportunitiesOther(QuoteOpportunitiesOther quoteOpportunitiesOther);

    /**
     * 批量删除报价系统-商机其他小记
     * 
     * @param ids 需要删除的报价系统-商机其他小记主键集合
     * @return 结果
     */
    public int deleteQuoteOpportunitiesOtherByIds(Long[] ids);

    /**
     * 删除报价系统-商机其他小记信息
     * 
     * @param id 报价系统-商机其他小记主键
     * @return 结果
     */
    public int deleteQuoteOpportunitiesOtherById(Long id);

    /**
     * 获取详细回显信息
     * @param quoteOpportunitiesDetailQuery
     * @return
     */
    QuoteOpportunitiesOtherVO getOtherInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery);

    /**
     * 批量保存明细数据
     * @param quoteOpportunitiesOtherList quoteOpportunitiesOtherList
     */
    void saveBatchDetail(List<QuoteOpportunitiesOtherSaveDTO> quoteOpportunitiesOtherList, QuoteOpportunitiesDetail quoteOpportunitiesDetail);
}
