package com.internal.web.controller.quote;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import com.internal.common.core.domain.R;
import com.internal.common.request.PageParams;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
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
import com.internal.quote.domain.QuoteOpportunitiesImplement;
import com.internal.quote.service.IQuoteOpportunitiesImplementService;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.common.core.page.TableDataInfo;

/**
 * 详细报价表-商机实施小记Controller
 * 
 * @author internal
 * @date 2024-10-15
 */
@RestController
@AllArgsConstructor
@Api(value = "QuoteOpportunitiesImplementController", tags = "详细报价表-商机实施小记表")
@RequestMapping("/quote/implement")
public class QuoteOpportunitiesImplementController extends BaseController
{

    private final IQuoteOpportunitiesImplementService quoteOpportunitiesImplementService;

    /**
     * 查询报价系统-商机实施小记列表
     */
    @ApiOperation(value = "查询报价系统-商机实施小记列表", notes = "查询报价系统-商机实施小记列表")
    @PreAuthorize("@ss.hasPermi('quote:implement:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuoteOpportunitiesImplement>> list(@RequestBody PageParams<QuoteOpportunitiesImplement> params)
    {
        startPage(params);
        List<QuoteOpportunitiesImplement> list = quoteOpportunitiesImplementService.selectQuoteOpportunitiesImplementList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出报价系统-商机实施小记列表
     */
    @ApiOperation(value = "导出报价系统-商机实施小记列表", notes = "导出报价系统-商机实施小记列表")
    @PreAuthorize("@ss.hasPermi('quote:implement:export')")
    @Log(title = "报价系统-商机实施小记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuoteOpportunitiesImplement quoteOpportunitiesImplement)
    {
        List<QuoteOpportunitiesImplement> list = quoteOpportunitiesImplementService.selectQuoteOpportunitiesImplementList(quoteOpportunitiesImplement);
        ExcelUtil<QuoteOpportunitiesImplement> util = new ExcelUtil<QuoteOpportunitiesImplement>(QuoteOpportunitiesImplement.class);
        util.exportExcel(response, list, "报价系统-商机实施小记数据");
    }

    /**
     * 获取报价系统-商机实施小记详细信息
     */
    @ApiOperation(value = "获取报价系统-商机实施小记详细信息", notes = "获取报价系统-商机实施小记详细信息")
    @PreAuthorize("@ss.hasPermi('quote:implement:query')")
    @PostMapping(value = "/getImplementInfo")
    public R<QuoteOpportunitiesImplement> getInfo(@RequestBody QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery)
    {
        return  R.ok(quoteOpportunitiesImplementService.selectQuoteOpportunitiesImplementById(quoteOpportunitiesDetailQuery.getOpportunitiesDetailId()));
    }

    /**
     * 新增报价系统-商机实施小记
     */
    @ApiOperation(value = "新增报价系统-商机实施小记", notes = "新增报价系统-商机实施小记")
    @PreAuthorize("@ss.hasPermi('quote:implement:add')")
    @Log(title = "报价系统-商机实施小记", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody QuoteOpportunitiesImplement quoteOpportunitiesImplement)
    {
        return R.ok(quoteOpportunitiesImplementService.insertQuoteOpportunitiesImplement(quoteOpportunitiesImplement));
    }

    /**
     * 修改报价系统-商机实施小记
     */
    @ApiOperation(value = "修改报价系统-商机实施小记", notes = "修改报价系统-商机实施小记")
    @PreAuthorize("@ss.hasPermi('quote:implement:edit')")
    @Log(title = "报价系统-商机实施小记", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuoteOpportunitiesImplement quoteOpportunitiesImplement)
    {
        return R.ok(quoteOpportunitiesImplementService.updateQuoteOpportunitiesImplement(quoteOpportunitiesImplement));
    }

    /**
     * 删除报价系统-商机实施小记
     */
    @ApiOperation(value = "删除报价系统-商机实施小记", notes = "删除报价系统-商机实施小记")
    @PreAuthorize("@ss.hasPermi('quote:implement:remove')")
    @Log(title = "报价系统-商机实施小记", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids)
    {
        return R.ok(quoteOpportunitiesImplementService.deleteQuoteOpportunitiesImplementByIds(ids));
    }
}
