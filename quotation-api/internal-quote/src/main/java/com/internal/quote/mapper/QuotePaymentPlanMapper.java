package com.internal.quote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.internal.quote.domain.QuotePaymentPlan;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报价系统-收款计划Mapper接口
 *
 * @author internal
 * @date 2024-12-19
 */
@Repository
public interface QuotePaymentPlanMapper extends BaseMapper<QuotePaymentPlan> {
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
     * 删除报价系统-收款计划
     *
     * @param id 报价系统-收款计划主键
     * @return 结果
     */
    public int deleteQuotePaymentPlanById(Long id);

    /**
     * 批量删除报价系统-收款计划
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuotePaymentPlanByIds(Long[] ids);
}
