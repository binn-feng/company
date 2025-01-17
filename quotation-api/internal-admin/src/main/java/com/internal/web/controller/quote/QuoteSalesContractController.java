package com.internal.web.controller.quote;

import com.internal.common.annotation.Log;
import com.internal.common.core.controller.BaseController;
import com.internal.common.core.domain.R;
import com.internal.common.core.page.TableDataInfo;
import com.internal.common.enums.BusinessType;
import com.internal.common.request.PageParams;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.quote.domain.QuoteSalesContract;
import com.internal.quote.service.IQuoteSalesContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 报价系统-销售合同Controller
 *
 * @author internal
 * @date 2024-12-19
 */
@RestController
@AllArgsConstructor
@Api(value = "QuoteSalesContractController", tags = "报价系统-销售合同表")
@RequestMapping("/quote/contract")
public class QuoteSalesContractController extends BaseController {

    private final IQuoteSalesContractService quoteSalesContractService;

    /**
     * 查询报价系统-销售合同列表
     */
    @ApiOperation(value = "查询报价系统-销售合同列表", notes = "查询报价系统-销售合同列表")
    @PreAuthorize("@ss.hasPermi('quote:contract:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuoteSalesContract>> list(@RequestBody PageParams<QuoteSalesContract> params) {
        startPage(params);
        List<QuoteSalesContract> list = quoteSalesContractService.selectQuoteSalesContractList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出报价系统-销售合同列表
     */
    @ApiOperation(value = "导出报价系统-销售合同列表", notes = "导出报价系统-销售合同列表")
    @PreAuthorize("@ss.hasPermi('quote:contract:export')")
    @Log(title = "报价系统-销售合同", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuoteSalesContract quoteSalesContract) {
        List<QuoteSalesContract> list = quoteSalesContractService.selectQuoteSalesContractList(quoteSalesContract);
        ExcelUtil<QuoteSalesContract> util = new ExcelUtil<QuoteSalesContract>(QuoteSalesContract.class);
        util.exportExcel(response, list, "报价系统-销售合同数据");
    }

    /**
     * 获取报价系统-销售合同详细信息
     */
    @ApiOperation(value = "获取报价系统-销售合同详细信息", notes = "获取报价系统-销售合同详细信息")
    @PreAuthorize("@ss.hasPermi('quote:contract:query')")
    @GetMapping(value = "/{id}")
    public R<QuoteSalesContract> getInfo(@PathVariable("id") Long id) {
        return R.ok(quoteSalesContractService.selectQuoteSalesContractById(id));
    }

    /**
     * 新增报价系统-销售合同
     */
    @ApiOperation(value = "新增报价系统-销售合同", notes = "新增报价系统-销售合同")
    @PreAuthorize("@ss.hasPermi('quote:contract:add')")
    @Log(title = "报价系统-销售合同", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody QuoteSalesContract quoteSalesContract) {
        return R.ok(quoteSalesContractService.insertQuoteSalesContract(quoteSalesContract));
    }

    /**
     * 修改报价系统-销售合同
     */
    @ApiOperation(value = "修改报价系统-销售合同", notes = "修改报价系统-销售合同")
    @PreAuthorize("@ss.hasPermi('quote:contract:edit')")
    @Log(title = "报价系统-销售合同", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuoteSalesContract quoteSalesContract) {
        return R.ok(quoteSalesContractService.updateQuoteSalesContract(quoteSalesContract));
    }

    /**
     * 删除报价系统-销售合同
     */
    @ApiOperation(value = "删除报价系统-销售合同", notes = "删除报价系统-销售合同")
    @PreAuthorize("@ss.hasPermi('quote:contract:remove')")
    @Log(title = "报价系统-销售合同", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids) {
        return R.ok(quoteSalesContractService.deleteQuoteSalesContractByIds(ids));
    }
}
