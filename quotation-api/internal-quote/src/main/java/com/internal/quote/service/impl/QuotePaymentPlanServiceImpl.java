package com.internal.quote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.common.utils.DateUtils;
import com.internal.quote.domain.QuotePaymentPlan;
import com.internal.quote.mapper.QuotePaymentPlanMapper;
import com.internal.quote.service.IQuotePaymentPlanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报价系统-收款计划Service业务层处理
 *
 * @author internal
 * @date 2024-12-19
 */
@Service
@AllArgsConstructor
public class QuotePaymentPlanServiceImpl extends ServiceImpl<QuotePaymentPlanMapper, QuotePaymentPlan> implements IQuotePaymentPlanService {

    private final QuotePaymentPlanMapper quotePaymentPlanMapper;

    /**
     * 查询报价系统-收款计划
     *
     * @param id 报价系统-收款计划主键
     * @return 报价系统-收款计划
     */
    @Override
    public QuotePaymentPlan selectQuotePaymentPlanById(Long id) {
        return quotePaymentPlanMapper.selectQuotePaymentPlanById(id);
    }

    /**
     * 查询报价系统-收款计划列表
     *
     * @param quotePaymentPlan 报价系统-收款计划
     * @return 报价系统-收款计划
     */
    @Override
    public List<QuotePaymentPlan> selectQuotePaymentPlanList(QuotePaymentPlan quotePaymentPlan) {
        return quotePaymentPlanMapper.selectQuotePaymentPlanList(quotePaymentPlan);
    }

    /**
     * 新增报价系统-收款计划
     *
     * @param quotePaymentPlan 报价系统-收款计划
     * @return 结果
     */
    @Override
    public int insertQuotePaymentPlan(QuotePaymentPlan quotePaymentPlan) {
        quotePaymentPlan.setCreateTime(DateUtils.getNowDate());
        return quotePaymentPlanMapper.insert(quotePaymentPlan);
    }

    /**
     * 修改报价系统-收款计划
     *
     * @param quotePaymentPlan 报价系统-收款计划
     * @return 结果
     */
    @Override
    public int updateQuotePaymentPlan(QuotePaymentPlan quotePaymentPlan) {
        quotePaymentPlan.setUpdateTime(DateUtils.getNowDate());
        return quotePaymentPlanMapper.updateById(quotePaymentPlan);
    }

    /**
     * 批量删除报价系统-收款计划
     *
     * @param ids 需要删除的报价系统-收款计划主键
     * @return 结果
     */
    @Override
    public int deleteQuotePaymentPlanByIds(Long[] ids) {
        return quotePaymentPlanMapper.deleteQuotePaymentPlanByIds(ids);
    }

    /**
     * 删除报价系统-收款计划信息
     *
     * @param id 报价系统-收款计划主键
     * @return 结果
     */
    @Override
    public int deleteQuotePaymentPlanById(Long id) {
        return quotePaymentPlanMapper.deleteQuotePaymentPlanById(id);
    }
}
