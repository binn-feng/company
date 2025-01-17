package com.internal.quote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.quote.domain.QuotePaymentPlan;

import java.util.List;

/**
 * 报价系统-收款计划Service接口
 *
 * @author internal
 * @date 2024-12-19
 */
public interface IQuotePaymentPlanService extends IService<QuotePaymentPlan> {
    /**
     * 查询报价系统-收款计划
     *
     * @param id 报价系统-收款计划主键
     * @return 报价系统-收款计划
     */
    public QuotePaymentPlan selectQuotePaymentPlanById(Long id);

    /**
     * 查询报价系统-收款计划列表
     *
     * @param quotePaymentPlan 报价系统-收款计划
     * @return 报价系统-收款计划集合
     */
    public List<QuotePaymentPlan> selectQuotePaymentPlanList(QuotePaymentPlan quotePaymentPlan);

    /**
     * 新增报价系统-收款计划
     *
     * @param quotePaymentPlan 报价系统-收款计划
     * @return 结果
     */
    public int insertQuotePaymentPlan(QuotePaymentPlan quotePaymentPlan);

    /**
     * 修改报价系统-收款计划
     *
     * @param quotePaymentPlan 报价系统-收款计划
     * @return 结果
     */
    public int updateQuotePaymentPlan(QuotePaymentPlan quotePaymentPlan);

    /**
     * 批量删除报价系统-收款计划
     *
     * @param ids 需要删除的报价系统-收款计划主键集合
     * @return 结果
     */
    public int deleteQuotePaymentPlanByIds(Long[] ids);

    /**
     * 删除报价系统-收款计划信息
     *
     * @param id 报价系统-收款计划主键
     * @return 结果
     */
    public int deleteQuotePaymentPlanById(Long id);
}
