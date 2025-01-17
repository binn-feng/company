package com.internal.quote.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.internal.common.enums.CostType;
import com.internal.common.enums.UnitType;
import com.internal.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.utils.DecimalUtil;
import com.internal.manager.domain.ManageCost;
import com.internal.manager.service.IManageCostService;
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesSupportSaveDTO;
import com.internal.quote.vo.QuoteOpportunitiesSupportVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.internal.quote.mapper.QuoteOpportunitiesSupportMapper;
import com.internal.quote.domain.QuoteOpportunitiesSupport;
import com.internal.quote.service.IQuoteOpportunitiesSupportService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 报价系统-商机售前支持详情Service业务层处理
 * 
 * @author internal
 * @date 2024-10-15
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QuoteOpportunitiesSupportServiceImpl extends ServiceImpl<QuoteOpportunitiesSupportMapper, QuoteOpportunitiesSupport> implements IQuoteOpportunitiesSupportService
{

    private final QuoteOpportunitiesSupportMapper quoteOpportunitiesSupportMapper;
    private final IManageCostService manageCostService;
    private static final BigDecimal PERSON_DAY_TO_MONTH_RATE = BigDecimal.valueOf(21.75);

    /**
     * 查询报价系统-商机售前支持详情
     * 
     * @param id 报价系统-商机售前支持详情主键
     * @return 报价系统-商机售前支持详情
     */
    @Override
    public QuoteOpportunitiesSupport selectQuoteOpportunitiesSupportById(Long id)
    {
        return quoteOpportunitiesSupportMapper.selectQuoteOpportunitiesSupportById(id);
    }

    /**
     * 查询报价系统-商机售前支持详情列表
     * 
     * @param quoteOpportunitiesSupport 报价系统-商机售前支持详情
     * @return 报价系统-商机售前支持详情
     */
    @Override
    public List<QuoteOpportunitiesSupport> selectQuoteOpportunitiesSupportList(QuoteOpportunitiesSupport quoteOpportunitiesSupport)
    {
        return quoteOpportunitiesSupportMapper.selectQuoteOpportunitiesSupportList(quoteOpportunitiesSupport);
    }

    /**
     * 新增报价系统-商机售前支持详情
     * 
     * @param quoteOpportunitiesSupport 报价系统-商机售前支持详情
     * @return 结果
     */
    @Override
    public int insertQuoteOpportunitiesSupport(QuoteOpportunitiesSupport quoteOpportunitiesSupport)
    {
        quoteOpportunitiesSupport.setCreateTime(DateUtils.getNowDate());
        return quoteOpportunitiesSupportMapper.insert(quoteOpportunitiesSupport);
    }

    /**
     * 修改报价系统-商机售前支持详情
     * 
     * @param quoteOpportunitiesSupport 报价系统-商机售前支持详情
     * @return 结果
     */
    @Override
    public int updateQuoteOpportunitiesSupport(QuoteOpportunitiesSupport quoteOpportunitiesSupport)
    {
        quoteOpportunitiesSupport.setUpdateTime(DateUtils.getNowDate());
        return quoteOpportunitiesSupportMapper.updateById(quoteOpportunitiesSupport);
    }

    /**
     * 批量删除报价系统-商机售前支持详情
     * 
     * @param ids 需要删除的报价系统-商机售前支持详情主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesSupportByIds(Long[] ids)
    {
        return quoteOpportunitiesSupportMapper.deleteQuoteOpportunitiesSupportByIds(ids);
    }

    /**
     * 删除报价系统-商机售前支持详情信息
     * 
     * @param id 报价系统-商机售前支持详情主键
     * @return 结果
     */
    @Override
    public int deleteQuoteOpportunitiesSupportById(Long id)
    {
        return quoteOpportunitiesSupportMapper.deleteQuoteOpportunitiesSupportById(id);
    }

    @Override
    public void saveBatchDetail(List<QuoteOpportunitiesSupportSaveDTO> quoteOpportunitiesSupportList, QuoteOpportunitiesDetail quoteOpportunitiesDetail) {
        List<QuoteOpportunitiesSupport> quoteOpportunitiesSupports = BeanUtil.copyToList(quoteOpportunitiesSupportList, QuoteOpportunitiesSupport.class);
        quoteOpportunitiesSupports.forEach(item ->{
                    item.setId(null);
                    item.setOpportunitiesId(quoteOpportunitiesDetail.getOpportunitiesId());
                    item.setOpportunitiesDetailId(quoteOpportunitiesDetail.getId());
                }
        );
       saveBatch(quoteOpportunitiesSupports);
    }

    @Override
    public QuoteOpportunitiesSupportVO getSupportInfo(QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery) {

        QuoteOpportunitiesSupportVO quoteOpportunitiesSupportVO = new QuoteOpportunitiesSupportVO();
        List<QuoteOpportunitiesSupport> quoteOpportunitiesSupportList = baseMapper
                .selectList(Wrappers.<QuoteOpportunitiesSupport>lambdaQuery().eq(QuoteOpportunitiesSupport::getOpportunitiesDetailId, quoteOpportunitiesDetailQuery.getOpportunitiesDetailId()));
        if (CollUtil.isNotEmpty(quoteOpportunitiesSupportList)) {
            //管理系统-成本管理
            Map<CostType, ManageCost> manageCostMap = manageCostService.getCostMap();

            quoteOpportunitiesSupportVO.setQuoteOpportunitiesSupportList(quoteOpportunitiesSupportList);
            AtomicReference<BigDecimal> within = new AtomicReference<>(BigDecimal.ZERO);  // 公司内部（人月）
            AtomicReference<BigDecimal> local = new AtomicReference<>(BigDecimal.ZERO);   // 本地（人月）
            AtomicReference<BigDecimal> remote = new AtomicReference<>(BigDecimal.ZERO);  // 外地（人月）
            quoteOpportunitiesSupportList.forEach(item -> {
                if (item.getUnit().equals(UnitType.HUMAN_MONTH.getCode())) {
                    if (ObjectUtil.isNotEmpty(item.getWithin())) {
                        within.updateAndGet(v -> v.add(item.getWithin()));
                    }
                    if (ObjectUtil.isNotEmpty(item.getLocal())) {
                        local.updateAndGet(v -> v.add(item.getLocal()));
                    }
                    if (ObjectUtil.isNotEmpty(item.getRemote())) {
                        remote.updateAndGet(v -> v.add(item.getRemote()));
                    }
                } else {
                    if (ObjectUtil.isNotEmpty(item.getWithin()) && item.getWithin().compareTo(BigDecimal.ZERO) > 0) {
                        within.updateAndGet(v -> v.add(item.getWithin().divide(PERSON_DAY_TO_MONTH_RATE, DecimalUtil.defaultScale, RoundingMode.HALF_DOWN)));
                    }
                    if (ObjectUtil.isNotEmpty(item.getLocal()) && item.getLocal().compareTo(BigDecimal.ZERO) > 0) {
                        local.updateAndGet(v -> v.add(item.getLocal().divide(PERSON_DAY_TO_MONTH_RATE, DecimalUtil.defaultScale, RoundingMode.HALF_DOWN)));
                    }
                    if (ObjectUtil.isNotEmpty(item.getRemote()) && item.getRemote().compareTo(BigDecimal.ZERO) > 0) {
                        remote.updateAndGet(v -> v.add(item.getRemote().divide(PERSON_DAY_TO_MONTH_RATE, DecimalUtil.defaultScale, RoundingMode.HALF_DOWN)));
                    }
                }
            });

            // 计算人月,保留2位小数，四舍五入
            quoteOpportunitiesSupportVO.setTotalInternal(within.get());
            quoteOpportunitiesSupportVO.setTotalLocal(local.get());
            quoteOpportunitiesSupportVO.setTotalExternal(remote.get());
            quoteOpportunitiesSupportVO.setTotalWorkHours(within.get().add(local.get()).add(remote.get()));
            // 计算合计
            quoteOpportunitiesSupportVO.setWithinCost(within.get().multiply(manageCostMap.get(CostType.PRE).getInsiderMonth()));
            quoteOpportunitiesSupportVO.setLocalCost(local.get().multiply(manageCostMap.get(CostType.PRE).getLocalPresenceDays()).multiply(PERSON_DAY_TO_MONTH_RATE));
            quoteOpportunitiesSupportVO.setRemoteCost(remote.get().multiply(manageCostMap.get(CostType.PRE).getRemotePresenceDays()).multiply(PERSON_DAY_TO_MONTH_RATE));
            quoteOpportunitiesSupportVO.setTotalCost(quoteOpportunitiesSupportVO.getWithinCost().add(quoteOpportunitiesSupportVO.getLocalCost()).add(quoteOpportunitiesSupportVO.getRemoteCost()));
            return quoteOpportunitiesSupportVO;
        }
        return null;
    }

}
