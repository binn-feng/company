package com.internal.web.controller.quote;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import com.internal.common.core.domain.R;
import com.internal.common.request.PageParams;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.service.IQuoteOpportunitiesSupportService;
import com.internal.quote.vo.QuoteOpportunitiesSupportVO;
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
import com.internal.common.enums.BusinessType;
import com.internal.quote.domain.QuoteOpportunitiesSupport;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.common.core.page.TableDataInfo;

/**
 * 详细报价表-商机售前支持详情Controller
 * 
 * @author internal
 * @date 2024-10-15
 */
@RestController
@AllArgsConstructor
@Api(value = "QuoteOpportunitiesSupportController", tags = "详细报价表-商机售前支持详情表")
@RequestMapping("/quote/support/detail")
public class QuoteOpportunitiesSupportController extends BaseController
{

    private final IQuoteOpportunitiesSupportService quoteOpportunitiesSupportService;

    /**
     * 查询报价系统-商机售前支持详情列表
     */
    @ApiOperation(value = "查询报价系统-商机售前支持详情列表", notes = "查询报价系统-商机售前支持详情列表")
    @PreAuthorize("@ss.hasPermi('quote:detail:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuoteOpportunitiesSupport>> list(@RequestBody PageParams<QuoteOpportunitiesSupport> params)
    {
        startPage(params);
        List<QuoteOpportunitiesSupport> list = quoteOpportunitiesSupportService.selectQuoteOpportunitiesSupportList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出报价系统-商机售前支持详情列表
     */
    @ApiOperation(value = "导出报价系统-商机售前支持详情列表", notes = "导出报价系统-商机售前支持详情列表")
    @PreAuthorize("@ss.hasPermi('quote:detail:export')")
    @Log(title = "报价系统-商机售前支持详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuoteOpportunitiesSupport quoteOpportunitiesSupportDetail)
    {
        List<QuoteOpportunitiesSupport> list = quoteOpportunitiesSupportService.selectQuoteOpportunitiesSupportList(quoteOpportunitiesSupportDetail);
        ExcelUtil<QuoteOpportunitiesSupport> util = new ExcelUtil<QuoteOpportunitiesSupport>(QuoteOpportunitiesSupport.class);
        util.exportExcel(response, list, "报价系统-商机售前支持详情数据");
    }

    /**
     * 获取报价系统-商机售前支持详情详细信息
     */
    @ApiOperation(value = "获取报价系统-商机售前支持详情详细信息", notes = "获取报价系统-商机售前支持详情详细信息")
    @PreAuthorize("@ss.hasPermi('quote:detail:query')")
    @PostMapping(value = "/getSupportInfo")
    public R<QuoteOpportunitiesSupportVO> getSupportInfo(@RequestBody QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery)
    {
        return  R.ok(quoteOpportunitiesSupportService.getSupportInfo(quoteOpportunitiesDetailQuery));
    }

    /**
     * 新增报价系统-商机售前支持详情
     */
    @ApiOperation(value = "新增报价系统-商机售前支持详情", notes = "新增报价系统-商机售前支持详情")
    @PreAuthorize("@ss.hasPermi('quote:detail:add')")
    @Log(title = "报价系统-商机售前支持详情", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody QuoteOpportunitiesSupport quoteOpportunitiesSupportDetail)
    {
        return R.ok(quoteOpportunitiesSupportService.insertQuoteOpportunitiesSupport(quoteOpportunitiesSupportDetail));
    }

    /**
     * 修改报价系统-商机售前支持详情
     */
    @ApiOperation(value = "修改报价系统-商机售前支持详情", notes = "修改报价系统-商机售前支持详情")
    @PreAuthorize("@ss.hasPermi('quote:detail:edit')")
    @Log(title = "报价系统-商机售前支持详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuoteOpportunitiesSupport quoteOpportunitiesSupportDetail)
    {
        return R.ok(quoteOpportunitiesSupportService.updateQuoteOpportunitiesSupport(quoteOpportunitiesSupportDetail));
    }

    /**
     * 删除报价系统-商机售前支持详情
     */
    @ApiOperation(value = "删除报价系统-商机售前支持详情", notes = "删除报价系统-商机售前支持详情")
    @PreAuthorize("@ss.hasPermi('quote:detail:remove')")
    @Log(title = "报价系统-商机售前支持详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids)
    {
        return R.ok(quoteOpportunitiesSupportService.deleteQuoteOpportunitiesSupportByIds(ids));
    }
}
