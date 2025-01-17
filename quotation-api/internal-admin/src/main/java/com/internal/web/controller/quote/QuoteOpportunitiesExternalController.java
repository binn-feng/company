package com.internal.web.controller.quote;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import com.internal.common.core.domain.R;
import com.internal.common.request.PageParams;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.vo.QuoteOpportunitiesExternalVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.internal.common.annotation.Log;
import com.internal.common.core.controller.BaseController;
import com.internal.common.core.domain.AjaxResult;
import com.internal.common.enums.BusinessType;
import com.internal.quote.domain.QuoteOpportunitiesExternal;
import com.internal.quote.service.IQuoteOpportunitiesExternalService;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.common.core.page.TableDataInfo;

/**
 * 详细报价表-商机外采硬件小记Controller
 * 
 * @author internal
 * @date 2024-10-15
 */
@RestController
@AllArgsConstructor
@Api(value = "QuoteOpportunitiesExternalController", tags = "详细报价表-商机外采硬件小记表")
@RequestMapping("/quote/external")
public class QuoteOpportunitiesExternalController extends BaseController
{

    private final IQuoteOpportunitiesExternalService quoteOpportunitiesExternalService;

    /**
     * 查询报价系统-商机外采硬件小记列表
     */
    @ApiOperation(value = "查询报价系统-商机外采硬件小记列表", notes = "查询报价系统-商机外采硬件小记列表")
    @PreAuthorize("@ss.hasPermi('quote:external:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuoteOpportunitiesExternal>> list(@RequestBody PageParams<QuoteOpportunitiesExternal> params)
    {
        startPage(params);
        List<QuoteOpportunitiesExternal> list = quoteOpportunitiesExternalService.selectQuoteOpportunitiesExternalList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出报价系统-商机外采硬件小记列表
     */
    @ApiOperation(value = "导出报价系统-商机外采硬件小记列表", notes = "导出报价系统-商机外采硬件小记列表")
    @PreAuthorize("@ss.hasPermi('quote:external:export')")
    @Log(title = "报价系统-商机外采硬件小记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuoteOpportunitiesExternal quoteOpportunitiesExternal)
    {
        List<QuoteOpportunitiesExternal> list = quoteOpportunitiesExternalService.selectQuoteOpportunitiesExternalList(quoteOpportunitiesExternal);
        ExcelUtil<QuoteOpportunitiesExternal> util = new ExcelUtil<QuoteOpportunitiesExternal>(QuoteOpportunitiesExternal.class);
        util.exportExcel(response, list, "报价系统-商机外采硬件小记数据");
    }

    /**
     * 获取报价系统-商机外采硬件小记详细信息
     */
    @ApiOperation(value = "获取报价系统-商机外采硬件小记详细信息", notes = "获取报价系统-商机外采硬件小记详细信息")
    @PreAuthorize("@ss.hasPermi('quote:external:query')")
    @PostMapping(value = "/getExternalInfo")
    public R<QuoteOpportunitiesExternalVO> getExternalInfo(@RequestBody QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery)
    {
        return  R.ok(quoteOpportunitiesExternalService.getExternalInfo(quoteOpportunitiesDetailQuery));
    }

    /**
     * 新增报价系统-商机外采硬件小记
     */
    @ApiOperation(value = "新增报价系统-商机外采硬件小记", notes = "新增报价系统-商机外采硬件小记")
    @PreAuthorize("@ss.hasPermi('quote:external:add')")
    @Log(title = "报价系统-商机外采硬件小记", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody QuoteOpportunitiesExternal quoteOpportunitiesExternal)
    {
        return R.ok(quoteOpportunitiesExternalService.insertQuoteOpportunitiesExternal(quoteOpportunitiesExternal));
    }

    /**
     * 修改报价系统-商机外采硬件小记
     */
    @ApiOperation(value = "修改报价系统-商机外采硬件小记", notes = "修改报价系统-商机外采硬件小记")
    @PreAuthorize("@ss.hasPermi('quote:external:edit')")
    @Log(title = "报价系统-商机外采硬件小记", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuoteOpportunitiesExternal quoteOpportunitiesExternal)
    {
        return R.ok(quoteOpportunitiesExternalService.updateQuoteOpportunitiesExternal(quoteOpportunitiesExternal));
    }

    /**
     * 删除报价系统-商机外采硬件小记
     */
    @ApiOperation(value = "删除报价系统-商机外采硬件小记", notes = "删除报价系统-商机外采硬件小记")
    @PreAuthorize("@ss.hasPermi('quote:external:remove')")
    @Log(title = "报价系统-商机外采硬件小记", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids)
    {
        return R.ok(quoteOpportunitiesExternalService.deleteQuoteOpportunitiesExternalByIds(ids));
    }
}
