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
import com.internal.quote.dto.QuoteOpportunitiesOtherSaveDTO;
import com.internal.quote.vo.QuoteOpportunitiesOtherVO;
import com.internal.quote.vo.QuoteOpportunitiesProductVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteOpportunitiesOtherMapper;
import com.internal.quote.domain.QuoteOpportunitiesOther;
import com.internal.quote.service.IQuoteOpportunitiesOtherService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报价系统-商机其他小记Service业务层处理
 * 
 * @author internal
 * @date 2024-10-15
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QuoteOpportunitiesOtherServiceImpl extends ServiceImpl<QuoteOpportunitiesOtherMapper, QuoteOpportunitiesOther> implements IQuoteOpportunitiesOtherService
{

    private final QuoteOpportunitiesOtherMapper quoteOpportunitiesOtherMapper;

    /**
     * 查询报价系统-商机其他小记
     * 
     * @param id 报价系统-商机其他小记主键
     * @return 报价系统-商机其他小记
     */
    @Override
    public QuoteOpportunitiesOther selectQuoteOpportunitiesOtherById(Long id)
    {
        return quoteOpportunitiesOtherMapper.selectQuoteOpportunitiesOtherById(id);
    }

    /**
     * 查询报价系统-商机其他小记列表
     * 
     * @param quoteOpportunitiesOther 报价系统-商机其他小记
     * @return 报价系统-商机其他小记
     */
    @Override
    public List<QuoteOpportunitiesOther> selectQuoteOpportunitiesOtherList(QuoteOpportunitiesOther quoteOpportunitiesOther)
    {
        return quoteOpportunitiesOtherMapper.selectQuoteOpportunitiesOtherList(quoteOpportunitiesOther);
    }

    /**
     * 新增报价系统-商机其他小记
     * 
     * @param quoteOpportunitiesOther 报价系统-商机其他小记
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesOther(QuoteOpportunitiesOther quoteOpportunitiesOther)
    {
        quoteOpportunitiesOther.setCreateTime(DateUtils.getNowDate());
        return quoteOpportunitiesOtherMapper.insert(quoteOpportunitiesOther);
    }

    /**
     * 修改报价系统-商机其他小记
     * 
     * @param quoteOpportunitiesOther 报价系统-商机其他小记
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesOther(QuoteOpportunitiesOther quoteOpportunitiesOther)
    {
        quoteOpportunitiesOther.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesOtherMapper.updateById(quoteOpportunitiesOther);
    }

    /**
     * 批量删除报价系统-商机其他小记
     * 
     * @param ids 需要删除的报价系统-商机其他小记主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesOtherByIds(Long[] ids)
    {
        return quoteOpportunitiesOtherMapper.deleteQuoteOpportunitiesOtherByIds(ids);
    }

    /**
     * 删除报价系统-商机其他小记信息
     * 
     * @param id 报价系统-商机其他小记主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesOtherById(Long id)
    {
        return quoteOpportunitiesOtherMapper.deleteQuoteOpportunitiesOtherById(id);
    }

    @Override
    public QuoteOpportunitiesOtherVO getOtherInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery) {
        QuoteOpportunitiesOtherVO quoteOpportunitiesProductVO = new QuoteOpportunitiesOtherVO();
        List<QuoteOpportunitiesOther> quoteOpportunitiesProductList = baseMapper
                .selectList(Wrappers.<QuoteOpportunitiesOther>lambdaQuery().eq(QuoteOpportunitiesOther::getOpportunitiesDetailId, quoteOpportunitiesDetailQuery.getOpportunitiesDetailId()));
        if (CollUtil.isNotEmpty(quoteOpportunitiesProductList)) {
            quoteOpportunitiesProductVO.setQuoteOpportunitiesOtherList(quoteOpportunitiesProductList);
            quoteOpportunitiesProductVO.setTotalCost(quoteOpportunitiesProductList.stream()
                    .map(QuoteOpportunitiesOther::getAmount).filter(Objects::nonNull).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
            return quoteOpportunitiesProductVO;
        }
        return null;
    }

    @Override
    public void saveBatchDetail(List<QuoteOpportunitiesOtherSaveDTO> quoteOpportunitiesOtherList, QuoteOpportunitiesDetail quoteOpportunitiesDetail) {
        List<QuoteOpportunitiesOther> quoteOpportunitiesOthers = BeanUtil.copyToList(quoteOpportunitiesOtherList, QuoteOpportunitiesOther.class);
        quoteOpportunitiesOthers.forEach(quoteOpportunitiesOther -> {
            quoteOpportunitiesOther.setOpportunitiesId(quoteOpportunitiesDetail.getOpportunitiesId());
            quoteOpportunitiesOther.setOpportunitiesDetailId(quoteOpportunitiesDetail.getId());
        });
        saveBatch(quoteOpportunitiesOthers);
    }
}
