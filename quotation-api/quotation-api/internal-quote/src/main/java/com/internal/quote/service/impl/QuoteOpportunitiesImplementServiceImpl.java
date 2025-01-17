package com.internal.quote.service.impl;

import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesImplementSaveDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteOpportunitiesImplementMapper;
import com.internal.quote.domain.QuoteOpportunitiesImplement;
import com.internal.quote.service.IQuoteOpportunitiesImplementService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报价系统-商机实施小记Service业务层处理
 * 
 * @author internal
 * @date 2024-10-15
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QuoteOpportunitiesImplementServiceImpl extends ServiceImpl<QuoteOpportunitiesImplementMapper, QuoteOpportunitiesImplement> implements IQuoteOpportunitiesImplementService
{

    private final QuoteOpportunitiesImplementMapper quoteOpportunitiesImplementMapper;

    /**
     * 查询报价系统-商机实施小记
     * 
     * @param id 报价系统-商机实施小记主键
     * @return 报价系统-商机实施小记
     */
    @Override
    public QuoteOpportunitiesImplement selectQuoteOpportunitiesImplementById(Long id)
    {
        return quoteOpportunitiesImplementMapper.selectQuoteOpportunitiesImplementById(id);
    }

    /**
     * 查询报价系统-商机实施小记列表
     * 
     * @param quoteOpportunitiesImplement 报价系统-商机实施小记
     * @return 报价系统-商机实施小记
     */
    @Override
    public List<QuoteOpportunitiesImplement> selectQuoteOpportunitiesImplementList(QuoteOpportunitiesImplement quoteOpportunitiesImplement)
    {
        return quoteOpportunitiesImplementMapper.selectQuoteOpportunitiesImplementList(quoteOpportunitiesImplement);
    }

    /**
     * 新增报价系统-商机实施小记
     * 
     * @param quoteOpportunitiesImplement 报价系统-商机实施小记
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesImplement(QuoteOpportunitiesImplement quoteOpportunitiesImplement)
    {
        quoteOpportunitiesImplement.setCreateTime(DateUtils.getNowDate());
        return quoteOpportunitiesImplementMapper.insert(quoteOpportunitiesImplement);
    }

    /**
     * 修改报价系统-商机实施小记
     * 
     * @param quoteOpportunitiesImplement 报价系统-商机实施小记
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesImplement(QuoteOpportunitiesImplement quoteOpportunitiesImplement)
    {
        quoteOpportunitiesImplement.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesImplementMapper.updateById(quoteOpportunitiesImplement);
    }

    /**
     * 批量删除报价系统-商机实施小记
     * 
     * @param ids 需要删除的报价系统-商机实施小记主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesImplementByIds(Long[] ids)
    {
        return quoteOpportunitiesImplementMapper.deleteQuoteOpportunitiesImplementByIds(ids);
    }

    /**
     * 删除报价系统-商机实施小记信息
     * 
     * @param id 报价系统-商机实施小记主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesImplementById(Long id)
    {
        return quoteOpportunitiesImplementMapper.deleteQuoteOpportunitiesImplementById(id);
    }

    @Override
    public void saveBatchDetail(List<QuoteOpportunitiesImplementSaveDTO> quoteOpportunitiesImplementList, QuoteOpportunitiesDetail quoteOpportunitiesDetail) {
        List<QuoteOpportunitiesImplement> quoteOpportunitiesImplements = BeanUtil.copyToList(quoteOpportunitiesImplementList, QuoteOpportunitiesImplement.class);
        quoteOpportunitiesImplements.forEach(quoteOpportunitiesImplement -> {
            quoteOpportunitiesImplement.setOpportunitiesId(quoteOpportunitiesDetail.getOpportunitiesId());
            quoteOpportunitiesImplement.setOpportunitiesDetailId(quoteOpportunitiesDetail.getId());
        });
        saveBatch(quoteOpportunitiesImplements);
    }

    @Override
    public QuoteOpportunitiesImplement getImportInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery) {
        QuoteOpportunitiesImplement quoteOpportunitiesImplement = baseMapper.selectOne(Wrappers.<QuoteOpportunitiesImplement>lambdaQuery()
                .eq(QuoteOpportunitiesImplement::getOpportunitiesDetailId, quoteOpportunitiesDetailQuery.getOpportunitiesDetailId()));
        if (ObjectUtil.isNotEmpty(quoteOpportunitiesImplement)){
            return quoteOpportunitiesImplement;
        }
        return null;
    }
}
