package com.internal.web.controller.manager;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.common.annotation.Anonymous;
import com.internal.common.core.controller.BaseController;
import com.internal.common.core.domain.AjaxResult;
import com.internal.common.core.domain.R;
import com.internal.common.core.page.TableDataInfo;
import com.internal.common.enums.BusinessType;
import com.internal.common.request.PageParams;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.manager.domain.ManageQuote;
import com.internal.manager.domain.ManageWarning;
import com.internal.manager.service.IManageWarningService;
import com.internal.common.annotation.Log;
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

/**
 * 管理系统-预警设置Controller
 *
 * @author internal
 * @date 2024-10-09
 */
@RestController
@AllArgsConstructor
@Api(value = "ManageWarningController", tags = "管理系统-预警设置表")
@RequestMapping("/manager/warning")
public class ManageWarningController extends BaseController
{

    private final IManageWarningService manageWarningService;

    /**
     * 查询管理系统-预警设置列表
     */
    @ApiOperation(value = "查询管理系统-预警设置列表", notes = "查询管理系统-预警设置列表")
    @PreAuthorize("@ss.hasPermi('manager:warning:list')")
    @PostMapping("/page")
    public TableDataInfo<List<ManageWarning>> list(@RequestBody PageParams<ManageWarning> params)
    {
        startPage(params);
        List<ManageWarning> list = manageWarningService.selectManageWarningList(params.getModel());
        return getDataTable(list);
    }
    /**
     * 查询管理系统-查询利润率(小数格式)
     */
    @ApiOperation(value = "查询管理系统-查询利润率(小数格式)", notes = "查询管理系统-查询利润率(小数格式)")
    @GetMapping("/getProfit")
    public R<ManageWarning> getProfit()
    {
        ManageWarning result = manageWarningService.getProfit();
        return R.ok(result);
    }

    /**
     * 导出管理系统-预警设置列表
     */
    @ApiOperation(value = "导出管理系统-预警设置列表", notes = "导出管理系统-预警设置列表")
    @PreAuthorize("@ss.hasPermi('manager:warning:export')")
    @Log(title = "管理系统-预警设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ManageWarning manageWarning)
    {
        List<ManageWarning> list = manageWarningService.selectManageWarningList(manageWarning);
        ExcelUtil<ManageWarning> util = new ExcelUtil<ManageWarning>(ManageWarning.class);
        util.exportExcel(response, list, "管理系统-预警设置数据");
    }

    /**
     * 获取管理系统-预警设置详细信息
     */
    @ApiOperation(value = "获取管理系统-预警设置详细信息", notes = "获取管理系统-预警设置详细信息")
    @PreAuthorize("@ss.hasPermi('manager:warning:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(manageWarningService.selectManageWarningById(id));
    }

    /**
     * 新增管理系统-预警设置
     */
    @ApiOperation(value = "新增管理系统-预警设置", notes = "新增管理系统-预警设置")
    @PreAuthorize("@ss.hasPermi('manager:warning:add')")
    @Log(title = "管理系统-预警设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ManageWarning manageWarning)
    {
        manageWarningService.remove(new QueryWrapper<ManageWarning>().isNotNull("id"));
        return toAjax(manageWarningService.insertManageWarning(manageWarning));
    }

    /**
     * 修改管理系统-预警设置
     */
    @ApiOperation(value = "修改管理系统-预警设置", notes = "修改管理系统-预警设置")
    @PreAuthorize("@ss.hasPermi('manager:warning:edit')")
    @Log(title = "管理系统-预警设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ManageWarning manageWarning)
    {
        return toAjax(manageWarningService.updateManageWarning(manageWarning));
    }

    /**
     * 删除管理系统-预警设置
     */
    @ApiOperation(value = "删除管理系统-预警设置", notes = "删除管理系统-预警设置")
    @PreAuthorize("@ss.hasPermi('manager:warning:remove')")
    @Log(title = "管理系统-预警设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(manageWarningService.deleteManageWarningByIds(ids));
    }
}
