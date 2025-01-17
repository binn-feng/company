package com.internal.web.controller.quote;

import java.util.List;

import com.internal.common.core.domain.BaseEntity;
import com.internal.common.core.domain.R;
import jakarta.servlet.http.HttpServletResponse;

import com.internal.common.core.domain.entity.SysDept;
import com.internal.common.request.PageParams;
import com.internal.quote.dto.BaseDTO;
import com.internal.quote.dto.QuoteRadioDTO;
import com.internal.quote.vo.QuoteRadioVO;
import com.internal.quote.vo.SysDeptVO;
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
import com.internal.quote.domain.QuoteRadio;
import com.internal.quote.service.IQuoteRadioService;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.common.core.page.TableDataInfo;

/**
 * 报价系统-报价部门占比Controller
 * 
 * @author internal
 * @date 2025-01-07
 */
@RestController
@AllArgsConstructor
@Api(value = "QuoteRadioController", tags = "报价系统-报价部门占比表")
@RequestMapping("/quote/radio")
public class QuoteRadioController extends BaseController
{

    private final IQuoteRadioService quoteRadioService;

    /**
     * 查询报价系统-报价部门占比列表
     */
    @ApiOperation(value = "查询报价系统-报价部门占比列表", notes = "查询报价系统-报价部门占比列表")
    @PreAuthorize("@ss.hasPermi('quote:radio:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuoteRadio>> list(@RequestBody PageParams<QuoteRadio> params)
    {
        startPage(params);
        List<QuoteRadio> list = quoteRadioService.selectQuoteRadioList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出报价系统-报价部门占比列表
     */
    @ApiOperation(value = "导出报价系统-报价部门占比列表", notes = "导出报价系统-报价部门占比列表")
    @PreAuthorize("@ss.hasPermi('quote:radio:export')")
    @Log(title = "报价系统-报价部门占比", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuoteRadio quoteRadio)
    {
        List<QuoteRadio> list = quoteRadioService.selectQuoteRadioList(quoteRadio);
        ExcelUtil<QuoteRadio> util = new ExcelUtil<QuoteRadio>(QuoteRadio.class);
        util.exportExcel(response, list, "报价系统-报价部门占比数据");
    }

    /**
     * 获取报价系统-报价部门占比详细信息
     */
    @ApiOperation(value = "获取报价系统-报价部门占比详细信息", notes = "获取报价系统-报价部门占比详细信息")
    @PreAuthorize("@ss.hasPermi('quote:radio:query')")
    @GetMapping(value = "/{id}")
    public R<QuoteRadio> getInfo(@PathVariable("id") Long id)
    {
        return  R.ok(quoteRadioService.selectQuoteRadioById(id));
    }

    /**
     * 新增报价系统-报价部门占比
     */
    @ApiOperation(value = "新增报价系统-报价部门占比", notes = "新增报价系统-报价部门占比")
    @PreAuthorize("@ss.hasPermi('quote:radio:add')")
    @Log(title = "报价系统-报价部门占比", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody QuoteRadio quoteRadio)
    {
        return R.ok(quoteRadioService.insertQuoteRadio(quoteRadio));
    }

    /**
     * 修改报价系统-报价部门占比
     */
    @ApiOperation(value = "修改报价系统-报价部门占比", notes = "修改报价系统-报价部门占比")
    @PreAuthorize("@ss.hasPermi('quote:radio:edit')")
    @Log(title = "报价系统-报价部门占比", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuoteRadio quoteRadio)
    {
        return R.ok(quoteRadioService.updateQuoteRadio(quoteRadio));
    }

    /**
     * 删除报价系统-报价部门占比
     */
    @ApiOperation(value = "删除报价系统-报价部门占比", notes = "删除报价系统-报价部门占比")
    @PreAuthorize("@ss.hasPermi('quote:radio:remove')")
    @Log(title = "报价系统-报价部门占比", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids)
    {
        return R.ok(quoteRadioService.deleteQuoteRadioByIds(ids));
    }

    /**
     * 查询报价的部门
     */
    @ApiOperation(value = "查询报价的部门", notes = "查询报价的部门-需要opportunitiesId")
    @PostMapping("/dept")
    public List<QuoteRadioVO> getQuoteDept(@RequestBody QuoteRadioDTO dto)
    {
        List<QuoteRadioVO> list = quoteRadioService.getQuoteDept(dto);
        return list;
    }
}
