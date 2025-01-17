package com.internal.quote.service.impl;

import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.internal.common.enums.QuoteType;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.mapper.QuoteOpportunitiesMapper;
import com.internal.quote.vo.QuoteOpportunitiesUnableVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteOpportunitiesUnableMapper;
import com.internal.quote.domain.QuoteOpportunitiesUnable;
import com.internal.quote.service.IQuoteOpportunitiesUnableService;

/**
 * 报价系统-无法报价Service业务层处理
 * 
 * @author internal
 * @date 2024-11-21
 */
@Service
@AllArgsConstructor
public class QuoteOpportunitiesUnableServiceImpl extends ServiceImpl<QuoteOpportunitiesUnableMapper, QuoteOpportunitiesUnable> implements IQuoteOpportunitiesUnableService
{

    private final QuoteOpportunitiesUnableMapper quoteOpportunitiesUnableMapper;
    private final QuoteOpportunitiesMapper quoteMapper;

    /**
     * 查询报价系统-无法报价
     * 
     * @param id 报价系统-无法报价主键
     * @return 报价系统-无法报价
     */
    @Override
    public QuoteOpportunitiesUnableVO selectQuoteOpportunitiesUnableById(Long id)
    {
        QuoteOpportunitiesUnable result = quoteOpportunitiesUnableMapper.selectById(id);
        if(ObjectUtil.isEmpty(result)){
            throw new RuntimeException("查询失败");
        }
        QuoteOpportunitiesUnableVO vo = BeanUtil.toBean(result,QuoteOpportunitiesUnableVO.class);
        vo.setVersion(result.getValuationVersion());
        vo.setValuationVersion(QuoteType.INCAPABLE.getInfo() + result.getValuationVersion());
        return vo;
    }

    /**
     * 查询报价系统-无法报价列表
     * 
     * @param quoteOpportunitiesUnable 报价系统-无法报价
     * @return 报价系统-无法报价
     */
    @Override
    public List<QuoteOpportunitiesUnable> selectQuoteOpportunitiesUnableList(QuoteOpportunitiesUnable quoteOpportunitiesUnable)
    {
        return quoteOpportunitiesUnableMapper.selectQuoteOpportunitiesUnableList(quoteOpportunitiesUnable);
    }

    /**
     * 新增报价系统-无法报价
     * 
     * @param quoteOpportunitiesUnable 报价系统-无法报价
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesUnable(QuoteOpportunitiesUnable quoteOpportunitiesUnable)
    {
        quoteOpportunitiesUnable.setCreateTime(DateUtils.getNowDate());
        return quoteOpportunitiesUnableMapper.insert(quoteOpportunitiesUnable);
    }

    /**
     * 修改报价系统-无法报价
     * 
     * @param quoteOpportunitiesUnable 报价系统-无法报价
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesUnable(QuoteOpportunitiesUnable quoteOpportunitiesUnable)
    {
        quoteOpportunitiesUnable.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesUnableMapper.updateById(quoteOpportunitiesUnable);
    }

    /**
     * 批量删除报价系统-无法报价
     * 
     * @param ids 需要删除的报价系统-无法报价主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesUnableByIds(Long[] ids)
    {
        return quoteOpportunitiesUnableMapper.deleteQuoteOpportunitiesUnableByIds(ids);
    }

    /**
     * 删除报价系统-无法报价信息
     * 
     * @param id 报价系统-无法报价主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesUnableById(Long id)
    {
        return quoteOpportunitiesUnableMapper.deleteQuoteOpportunitiesUnableById(id);
    }
}
