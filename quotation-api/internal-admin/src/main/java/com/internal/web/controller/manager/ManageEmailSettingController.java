package com.internal.web.controller.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.common.annotation.Log;
import com.internal.common.core.controller.BaseController;
import com.internal.common.core.domain.R;
import com.internal.common.core.page.TableDataInfo;
import com.internal.common.enums.BusinessType;
import com.internal.common.request.PageParams;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.quote.domain.QuoteEmailSetting;
import com.internal.quote.service.IQuoteEmailSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 报价系统-工作流邮箱设置Controller
 *
 * @author internal
 * @date 2024-11-25
 */
@RestController
@AllArgsConstructor
@Api(value = "ManageEmailSettingController", tags = "管理系统-工作流邮箱设置表")
@RequestMapping("/manager/setting")
public class ManageEmailSettingController extends BaseController {

    private final IQuoteEmailSettingService quoteEmailSettingService;

    /**
     * 查询报价系统-工作流邮箱设置列表
     */
    @ApiOperation(value = "查询报价系统-工作流邮箱设置列表", notes = "查询报价系统-工作流邮箱设置列表")
    @PreAuthorize("@ss.hasPermi('manager:setting:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuoteEmailSetting>> list(@RequestBody PageParams<QuoteEmailSetting> params) {
        startPage(params.getCurrent(),params.getSize());
        List<QuoteEmailSetting> list = quoteEmailSettingService.selectQuoteEmailSettingList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出报价系统-工作流邮箱设置列表
     */
    @ApiOperation(value = "导出报价系统-工作流邮箱设置列表", notes = "导出报价系统-工作流邮箱设置列表")
    @PreAuthorize("@ss.hasPermi('manager:setting:export')")
    @Log(title = "报价系统-工作流邮箱设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuoteEmailSetting quoteEmailSetting) {
        List<QuoteEmailSetting> list = quoteEmailSettingService.selectQuoteEmailSettingList(quoteEmailSetting);
        ExcelUtil<QuoteEmailSetting> util = new ExcelUtil<QuoteEmailSetting>(QuoteEmailSetting.class);
        util.exportExcel(response, list, "报价系统-工作流邮箱设置数据");
    }

    /**
     * 获取报价系统-工作流邮箱设置详细信息
     */
    @ApiOperation(value = "获取报价系统-工作流邮箱设置详细信息", notes = "获取报价系统-工作流邮箱设置详细信息")
    @PreAuthorize("@ss.hasPermi('manager:setting:query')")
    @GetMapping(value = "/{id}")
    public R<QuoteEmailSetting> getInfo(@PathVariable("id") Long id) {
        return R.ok(quoteEmailSettingService.selectQuoteEmailSettingById(id));
    }

    /**
     * 新增报价系统-工作流邮箱设置
     */
    @ApiOperation(value = "新增报价系统-工作流邮箱设置", notes = "新增报价系统-工作流邮箱设置")
    @PreAuthorize("@ss.hasPermi('manager:setting:add')")
    @Log(title = "报价系统-工作流邮箱设置", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody QuoteEmailSetting quoteEmailSetting) {
        return R.ok(quoteEmailSettingService.insertQuoteEmailSetting(quoteEmailSetting));
    }

    /**
     * 批量新价系统-工作流邮箱设置
     */
    @ApiOperation(value = "批量新价系统-工作流邮箱设置", notes = "批量新价系统-工作流邮箱设置")
    @PreAuthorize("@ss.hasPermi('manager:setting:add')")
    @Log(title = "报价系统-工作流邮箱设置", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public R<Boolean> addBatch(@RequestBody List<QuoteEmailSetting> quoteEmailSetting) {
        quoteEmailSettingService.remove(new QueryWrapper<QuoteEmailSetting>().isNotNull("id"));
        return R.ok(quoteEmailSettingService.saveBatch(quoteEmailSetting));
    }


    /**
     * 修改报价系统-工作流邮箱设置
     */
    @ApiOperation(value = "修改报价系统-工作流邮箱设置", notes = "修改报价系统-工作流邮箱设置")
    @PreAuthorize("@ss.hasPermi('manager:setting:edit')")
    @Log(title = "报价系统-工作流邮箱设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuoteEmailSetting quoteEmailSetting) {
        return R.ok(quoteEmailSettingService.updateQuoteEmailSetting(quoteEmailSetting));
    }

    /**
     * 删除报价系统-工作流邮箱设置
     */
    @ApiOperation(value = "删除报价系统-工作流邮箱设置", notes = "删除报价系统-工作流邮箱设置")
    @PreAuthorize("@ss.hasPermi('manager:setting:remove')")
    @Log(title = "报价系统-工作流邮箱设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids) {
        return R.ok(quoteEmailSettingService.deleteQuoteEmailSettingByIds(ids));
    }
}
