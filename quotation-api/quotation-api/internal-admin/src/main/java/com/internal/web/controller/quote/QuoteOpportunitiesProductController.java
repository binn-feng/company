package com.internal.web.controller.quote;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import cn.hutool.core.util.ObjUtil;
import com.internal.common.core.domain.R;
import com.internal.common.request.PageParams;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.vo.QuoteOpportunitiesProductVO;
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
import com.internal.quote.domain.QuoteOpportunitiesProduct;
import com.internal.quote.service.IQuoteOpportunitiesProductService;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.common.core.page.TableDataInfo;

/**
 * 详细报价表-商机产品平台小记Controller
 * 
 * @author internal
 * @date 2024-10-15
 */
@RestController
@AllArgsConstructor
@Api(value = "QuoteOpportunitiesProductController", tags = "详细报价表-商机产品平台小记表")
@RequestMapping("/quote/product")
public class QuoteOpportunitiesProductController extends BaseController
{

    private final IQuoteOpportunitiesProductService quoteOpportunitiesProductService;

    /**
     * 查询报价系统-商机产品平台小记列表
     */
    @ApiOperation(value = "查询报价系统-商机产品平台小记列表", notes = "查询报价系统-商机产品平台小记列表")
    @PreAuthorize("@ss.hasPermi('quote:product:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuoteOpportunitiesProduct>> list(@RequestBody PageParams<QuoteOpportunitiesProduct> params)
    {
        startPage(params);
        List<QuoteOpportunitiesProduct> list = quoteOpportunitiesProductService.selectQuoteOpportunitiesProductList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出报价系统-商机产品平台小记列表
     */
    @ApiOperation(value = "导出报价系统-商机产品平台小记列表", notes = "导出报价系统-商机产品平台小记列表")
    @PreAuthorize("@ss.hasPermi('quote:product:export')")
    @Log(title = "报价系统-商机产品平台小记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuoteOpportunitiesProduct quoteOpportunitiesProduct)
    {
        List<QuoteOpportunitiesProduct> list = quoteOpportunitiesProductService.selectQuoteOpportunitiesProductList(quoteOpportunitiesProduct);
        ExcelUtil<QuoteOpportunitiesProduct> util = new ExcelUtil<QuoteOpportunitiesProduct>(QuoteOpportunitiesProduct.class);
        util.exportExcel(response, list, "报价系统-商机产品平台小记数据");
    }

    /**
     * 获取报价系统-商机产品平台小记详细信息
     */
    @ApiOperation(value = "获取报价系统-商机产品平台小记详细信息", notes = "获取报价系统-商机产品平台小记详细信息")
    @PreAuthorize("@ss.hasPermi('quote:product:query')")
    @PostMapping(value = "/getProductInfo")
    public R<QuoteOpportunitiesProductVO> getProductInfo(@RequestBody QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery)
    {
        return  R.ok(quoteOpportunitiesProductService.getProductInfo(quoteOpportunitiesDetailQuery.getOpportunitiesDetailId()));
    }

    /**
     * 新增报价系统-商机产品平台小记
     */
    @ApiOperation(value = "新增报价系统-商机产品平台小记", notes = "新增报价系统-商机产品平台小记")
    @PreAuthorize("@ss.hasPermi('quote:product:add')")
    @Log(title = "报价系统-商机产品平台小记", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody QuoteOpportunitiesProduct quoteOpportunitiesProduct)
    {
        return R.ok(quoteOpportunitiesProductService.insertQuoteOpportunitiesProduct(quoteOpportunitiesProduct));
    }

    /**
     * 修改报价系统-商机产品平台小记
     */
    @ApiOperation(value = "修改报价系统-商机产品平台小记", notes = "修改报价系统-商机产品平台小记")
    @PreAuthorize("@ss.hasPermi('quote:product:edit')")
    @Log(title = "报价系统-商机产品平台小记", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuoteOpportunitiesProduct quoteOpportunitiesProduct)
    {
        return R.ok(quoteOpportunitiesProductService.updateQuoteOpportunitiesProduct(quoteOpportunitiesProduct));
    }

    /**
     * 删除报价系统-商机产品平台小记
     */
    @ApiOperation(value = "删除报价系统-商机产品平台小记", notes = "删除报价系统-商机产品平台小记")
    @PreAuthorize("@ss.hasPermi('quote:product:remove')")
    @Log(title = "报价系统-商机产品平台小记", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids)
    {
        return R.ok(quoteOpportunitiesProductService.deleteQuoteOpportunitiesProductByIds(ids));
    }
}
