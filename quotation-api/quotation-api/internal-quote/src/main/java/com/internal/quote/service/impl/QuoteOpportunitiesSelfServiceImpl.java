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
import com.internal.quote.dto.QuoteOpportunitiesSelfSaveDTO;
import com.internal.quote.vo.QuoteOpportunitiesProductVO;
import com.internal.quote.vo.QuoteOpportunitiesSelfVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteOpportunitiesSelfMapper;
import com.internal.quote.domain.QuoteOpportunitiesSelf;
import com.internal.quote.service.IQuoteOpportunitiesSelfService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报价系统-商机自研硬件小记Service业务层处理
 * 
 * @author internal
 * @date 2024-10-15
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QuoteOpportunitiesSelfServiceImpl extends ServiceImpl<QuoteOpportunitiesSelfMapper, QuoteOpportunitiesSelf> implements IQuoteOpportunitiesSelfService
{

    private final QuoteOpportunitiesSelfMapper quoteOpportunitiesSelfMapper;

    /**
     * 查询报价系统-商机自研硬件小记
     * 
     * @param id 报价系统-商机自研硬件小记主键
     * @return 报价系统-商机自研硬件小记
     */
    @Override
    public QuoteOpportunitiesSelf selectQuoteOpportunitiesSelfById(Long id)
    {
        return quoteOpportunitiesSelfMapper.selectQuoteOpportunitiesSelfById(id);
    }

    /**
     * 查询报价系统-商机自研硬件小记列表
     * 
     * @param quoteOpportunitiesSelf 报价系统-商机自研硬件小记
     * @return 报价系统-商机自研硬件小记
     */
    @Override
    public List<QuoteOpportunitiesSelf> selectQuoteOpportunitiesSelfList(QuoteOpportunitiesSelf quoteOpportunitiesSelf)
    {
        return quoteOpportunitiesSelfMapper.selectQuoteOpportunitiesSelfList(quoteOpportunitiesSelf);
    }

    /**
     * 新增报价系统-商机自研硬件小记
     * 
     * @param quoteOpportunitiesSelf 报价系统-商机自研硬件小记
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesSelf(QuoteOpportunitiesSelf quoteOpportunitiesSelf)
    {
        quoteOpportunitiesSelf.setCreateTime(DateUtils.getNowDate());
        return quoteOpportunitiesSelfMapper.insert(quoteOpportunitiesSelf);
    }

    /**
     * 修改报价系统-商机自研硬件小记
     * 
     * @param quoteOpportunitiesSelf 报价系统-商机自研硬件小记
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesSelf(QuoteOpportunitiesSelf quoteOpportunitiesSelf)
    {
        quoteOpportunitiesSelf.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesSelfMapper.updateById(quoteOpportunitiesSelf);
    }

    /**
     * 批量删除报价系统-商机自研硬件小记
     * 
     * @param ids 需要删除的报价系统-商机自研硬件小记主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesSelfByIds(Long[] ids)
    {
        return quoteOpportunitiesSelfMapper.deleteQuoteOpportunitiesSelfByIds(ids);
    }

    /**
     * 删除报价系统-商机自研硬件小记信息
     * 
     * @param id 报价系统-商机自研硬件小记主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesSelfById(Long id)
    {
        return quoteOpportunitiesSelfMapper.deleteQuoteOpportunitiesSelfById(id);
    }

    @Override
    public QuoteOpportunitiesSelfVO getSelfInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery) {
        QuoteOpportunitiesSelfVO quoteOpportunitiesSelfVO = new QuoteOpportunitiesSelfVO();
        List<QuoteOpportunitiesSelf> quoteOpportunitiesProductList = baseMapper
                .selectList(Wrappers.<QuoteOpportunitiesSelf>lambdaQuery().eq(QuoteOpportunitiesSelf::getOpportunitiesDetailId, quoteOpportunitiesDetailQuery.getOpportunitiesDetailId()));
        if (CollUtil.isNotEmpty(quoteOpportunitiesProductList)){
        quoteOpportunitiesSelfVO.setQuoteOpportunitiesSelfList(quoteOpportunitiesProductList);
            quoteOpportunitiesSelfVO.setTotalCost(quoteOpportunitiesProductList.stream()
                    .map(QuoteOpportunitiesSelf::getAmount).filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
            return quoteOpportunitiesSelfVO;
        }
        return null;
    }

    @Override
    public void saveBatchDetail(List<QuoteOpportunitiesSelfSaveDTO> quoteOpportunitiesSelfList, QuoteOpportunitiesDetail quoteOpportunitiesDetail) {
        List<QuoteOpportunitiesSelf> quoteOpportunitiesSelfs = BeanUtil.copyToList(quoteOpportunitiesSelfList, QuoteOpportunitiesSelf.class);
        quoteOpportunitiesSelfs.forEach(quoteOpportunitiesSelf -> {
            quoteOpportunitiesSelf.setOpportunitiesId(quoteOpportunitiesDetail.getOpportunitiesId());
            quoteOpportunitiesSelf.setOpportunitiesDetailId(quoteOpportunitiesDetail.getId());
        });
        saveBatch(quoteOpportunitiesSelfs);
    }
}
