package com.internal.web.controller.quote;

import com.internal.common.annotation.Log;
import com.internal.common.core.controller.BaseController;
import com.internal.common.core.domain.R;
import com.internal.common.core.page.TableDataInfo;
import com.internal.common.enums.BusinessType;
import com.internal.common.request.PageParams;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.quote.domain.QuotePaymentPlan;
import com.internal.quote.service.IQuotePaymentPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 报价系统-收款计划Controller
 *
 * @author internal
 * @date 2024-12-19
 */
@RestController
@AllArgsConstructor
@Api(value = "QuotePaymentPlanController", tags = "报价系统-收款计划表")
@RequestMapping("/quote/plan")
public class QuotePaymentPlanController extends BaseController {

    private final IQuotePaymentPlanService quotePaymentPlanService;

    /**
     * 查询报价系统-收款计划列表
     */
    @ApiOperation(value = "查询报价系统-收款计划列表", notes = "查询报价系统-收款计划列表")
    @PreAuthorize("@ss.hasPermi('quote:plan:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuotePaymentPlan>> list(@RequestBody PageParams<QuotePaymentPlan> params) {
        startPage(params);
        List<QuotePaymentPlan> list = quotePaymentPlanService.selectQuotePaymentPlanList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出报价系统-收款计划列表
     */
    @ApiOperation(value = "导出报价系统-收款计划列表", notes = "导出报价系统-收款计划列表")
    @PreAuthorize("@ss.hasPermi('quote:plan:export')")
    @Log(title = "报价系统-收款计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuotePaymentPlan quotePaymentPlan) {
        List<QuotePaymentPlan> list = quotePaymentPlanService.selectQuotePaymentPlanList(quotePaymentPlan);
        ExcelUtil<QuotePaymentPlan> util = new ExcelUtil<QuotePaymentPlan>(QuotePaymentPlan.class);
        util.exportExcel(response, list, "报价系统-收款计划数据");
    }

    /**
     * 获取报价系统-收款计划详细信息
     */
    @ApiOperation(value = "获取报价系统-收款计划详细信息", notes = "获取报价系统-收款计划详细信息")
    @PreAuthorize("@ss.hasPermi('quote:plan:query')")
    @GetMapping(value = "/{id}")
    public R<QuotePaymentPlan> getInfo(@PathVariable("id") Long id) {
        return R.ok(quotePaymentPlanService.selectQuotePaymentPlanById(id));
    }

    /**
     * 新增报价系统-收款计划
     */
    @ApiOperation(value = "新增报价系统-收款计划", notes = "新增报价系统-收款计划")
    @PreAuthorize("@ss.hasPermi('quote:plan:add')")
    @Log(title = "报价系统-收款计划", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody QuotePaymentPlan quotePaymentPlan) {
        return R.ok(quotePaymentPlanService.insertQuotePaymentPlan(quotePaymentPlan));
    }

    /**
     * 修改报价系统-收款计划
     */
    @ApiOperation(value = "修改报价系统-收款计划", notes = "修改报价系统-收款计划")
    @PreAuthorize("@ss.hasPermi('quote:plan:edit')")
    @Log(title = "报价系统-收款计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuotePaymentPlan quotePaymentPlan) {
        return R.ok(quotePaymentPlanService.updateQuotePaymentPlan(quotePaymentPlan));
    }

    /**
     * 删除报价系统-收款计划
     */
    @ApiOperation(value = "删除报价系统-收款计划", notes = "删除报价系统-收款计划")
    @PreAuthorize("@ss.hasPermi('quote:plan:remove')")
    @Log(title = "报价系统-收款计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids) {
        return R.ok(quotePaymentPlanService.deleteQuotePaymentPlanByIds(ids));
    }
}
