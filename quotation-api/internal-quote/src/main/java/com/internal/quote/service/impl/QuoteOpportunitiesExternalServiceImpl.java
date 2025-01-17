package com.internal.quote.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.domain.QuoteOpportunitiesProduct;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesExternalSaveDTO;
import com.internal.quote.vo.QuoteOpportunitiesExternalVO;
import com.internal.quote.vo.QuoteOpportunitiesProductVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteOpportunitiesExternalMapper;
import com.internal.quote.domain.QuoteOpportunitiesExternal;
import com.internal.quote.service.IQuoteOpportunitiesExternalService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报价系统-商机外采硬件小记Service业务层处理
 * 
 * @author internal
 * @date 2024-10-15
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QuoteOpportunitiesExternalServiceImpl extends ServiceImpl<QuoteOpportunitiesExternalMapper, QuoteOpportunitiesExternal> implements IQuoteOpportunitiesExternalService
{

    private final QuoteOpportunitiesExternalMapper quoteOpportunitiesExternalMapper;

    /**
     * 查询报价系统-商机外采硬件小记
     * 
     * @param id 报价系统-商机外采硬件小记主键
     * @return 报价系统-商机外采硬件小记
     */
    @Override
    public QuoteOpportunitiesExternal selectQuoteOpportunitiesExternalById(Long id)
    {
        return quoteOpportunitiesExternalMapper.selectQuoteOpportunitiesExternalById(id);
    }

    /**
     * 查询报价系统-商机外采硬件小记列表
     * 
     * @param quoteOpportunitiesExternal 报价系统-商机外采硬件小记
     * @return 报价系统-商机外采硬件小记
     */
    @Override
    public List<QuoteOpportunitiesExternal> selectQuoteOpportunitiesExternalList(QuoteOpportunitiesExternal quoteOpportunitiesExternal)
    {
        return quoteOpportunitiesExternalMapper.selectQuoteOpportunitiesExternalList(quoteOpportunitiesExternal);
    }

    /**
     * 新增报价系统-商机外采硬件小记
     * 
     * @param quoteOpportunitiesExternal 报价系统-商机外采硬件小记
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesExternal(QuoteOpportunitiesExternal quoteOpportunitiesExternal)
    {
        quoteOpportunitiesExternal.setCreateTime(DateUtils.getNowDate());
        return quoteOpportunitiesExternalMapper.insert(quoteOpportunitiesExternal);
    }

    /**
     * 修改报价系统-商机外采硬件小记
     * 
     * @param quoteOpportunitiesExternal 报价系统-商机外采硬件小记
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesExternal(QuoteOpportunitiesExternal quoteOpportunitiesExternal)
    {
        quoteOpportunitiesExternal.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesExternalMapper.updateById(quoteOpportunitiesExternal);
    }

    /**
     * 批量删除报价系统-商机外采硬件小记
     * 
     * @param ids 需要删除的报价系统-商机外采硬件小记主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesExternalByIds(Long[] ids)
    {
        return quoteOpportunitiesExternalMapper.deleteQuoteOpportunitiesExternalByIds(ids);
    }

    /**
     * 删除报价系统-商机外采硬件小记信息
     * 
     * @param id 报价系统-商机外采硬件小记主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesExternalById(Long id)
    {
        return quoteOpportunitiesExternalMapper.deleteQuoteOpportunitiesExternalById(id);
    }

    @Override
    public QuoteOpportunitiesExternalVO getExternalInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery) {
        QuoteOpportunitiesExternalVO quoteOpportunitiesExternalVO = new QuoteOpportunitiesExternalVO();
        List<QuoteOpportunitiesExternal> quoteOpportunitiesExternalList = baseMapper
                .selectList(Wrappers.<QuoteOpportunitiesExternal>lambdaQuery().eq(QuoteOpportunitiesExternal::getOpportunitiesDetailId, quoteOpportunitiesDetailQuery.getOpportunitiesDetailId()));
        if (CollUtil.isNotEmpty(quoteOpportunitiesExternalList)) {
            quoteOpportunitiesExternalVO.setQuoteOpportunitiesExternalList(quoteOpportunitiesExternalList);
            quoteOpportunitiesExternalVO.setTotalCost(quoteOpportunitiesExternalList.stream()
                    .map(QuoteOpportunitiesExternal::getPurchaseInquiry).filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
            quoteOpportunitiesExternalVO.setTotalEstimatedlCost(quoteOpportunitiesExternalList.stream()
                    .map(QuoteOpportunitiesExternal::getEstimatedCost).filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
            quoteOpportunitiesExternalVO.setTotalExternalquotation(quoteOpportunitiesExternalList.stream()
                    .map(QuoteOpportunitiesExternal::getExternalQuote).filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
            return quoteOpportunitiesExternalVO;
        }
        return null;
    }

    @Override
    public void saveBatchDetail(List<QuoteOpportunitiesExternalSaveDTO> quoteOpportunitiesExternalList, QuoteOpportunitiesDetail quoteOpportunitiesDetail) {
        List<QuoteOpportunitiesExternal> quoteOpportunitiesExternals = BeanUtil.copyToList(quoteOpportunitiesExternalList, QuoteOpportunitiesExternal.class);
        quoteOpportunitiesExternals.forEach(quoteOpportunitiesExternal -> {
            quoteOpportunitiesExternal.setOpportunitiesId(quoteOpportunitiesDetail.getOpportunitiesId());
            quoteOpportunitiesExternal.setOpportunitiesDetailId(quoteOpportunitiesDetail.getId());
        });
        saveBatch(quoteOpportunitiesExternals);
    }
}
