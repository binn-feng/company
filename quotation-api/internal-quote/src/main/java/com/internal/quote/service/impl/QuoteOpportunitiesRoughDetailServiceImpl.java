package com.internal.quote.service.impl;

import java.util.List;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteOpportunitiesRoughDetailMapper;
import com.internal.quote.domain.QuoteOpportunitiesRoughDetail;
import com.internal.quote.service.IQuoteOpportunitiesRoughDetailService;

/**
 * 报价系统-商机粗略详细报价Service业务层处理
 * 
 * @author internal
 * @date 2024-10-29
 */
@Service
@AllArgsConstructor
public class QuoteOpportunitiesRoughDetailServiceImpl extends ServiceImpl<QuoteOpportunitiesRoughDetailMapper, QuoteOpportunitiesRoughDetail> implements IQuoteOpportunitiesRoughDetailService
{

    private final QuoteOpportunitiesRoughDetailMapper quoteOpportunitiesRoughDetailMapper;

    /**
     * 查询报价系统-商机粗略详细报价
     * 
     * @param id 报价系统-商机粗略详细报价主键
     * @return 报价系统-商机粗略详细报价
     */
    @Override
    public QuoteOpportunitiesRoughDetail selectQuoteOpportunitiesRoughDetailById(Long id)
    {
        return quoteOpportunitiesRoughDetailMapper.selectQuoteOpportunitiesRoughDetailById(id);
    }

    /**
     * 查询报价系统-商机粗略详细报价列表
     * 
     * @param quoteOpportunitiesRoughDetail 报价系统-商机粗略详细报价
     * @return 报价系统-商机粗略详细报价
     */
    @Override
    public List<QuoteOpportunitiesRoughDetail> selectQuoteOpportunitiesRoughDetailList(QuoteOpportunitiesRoughDetail quoteOpportunitiesRoughDetail)
    {
        return quoteOpportunitiesRoughDetailMapper.selectQuoteOpportunitiesRoughDetailList(quoteOpportunitiesRoughDetail);
    }

    /**
     * 新增报价系统-商机粗略详细报价
     * 
     * @param quoteOpportunitiesRoughDetail 报价系统-商机粗略详细报价
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesRoughDetail(QuoteOpportunitiesRoughDetail quoteOpportunitiesRoughDetail)
    {
        quoteOpportunitiesRoughDetail.setCreateTime(DateUtils.getNowDate());
        return quoteOpportunitiesRoughDetailMapper.insert(quoteOpportunitiesRoughDetail);
    }

    /**
     * 修改报价系统-商机粗略详细报价
     * 
     * @param quoteOpportunitiesRoughDetail 报价系统-商机粗略详细报价
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesRoughDetail(QuoteOpportunitiesRoughDetail quoteOpportunitiesRoughDetail)
    {
        quoteOpportunitiesRoughDetail.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesRoughDetailMapper.updateById(quoteOpportunitiesRoughDetail);
    }

    /**
     * 批量删除报价系统-商机粗略详细报价
     * 
     * @param ids 需要删除的报价系统-商机粗略详细报价主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesRoughDetailByIds(Long[] ids)
    {
        return quoteOpportunitiesRoughDetailMapper.deleteQuoteOpportunitiesRoughDetailByIds(ids);
    }

    /**
     * 删除报价系统-商机粗略详细报价信息
     * 
     * @param id 报价系统-商机粗略详细报价主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesRoughDetailById(Long id)
    {
        return quoteOpportunitiesRoughDetailMapper.deleteQuoteOpportunitiesRoughDetailById(id);
    }
}
