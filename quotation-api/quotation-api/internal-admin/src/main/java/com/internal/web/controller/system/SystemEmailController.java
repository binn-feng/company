package com.internal.web.controller.system;

import com.internal.common.annotation.Log;
import com.internal.common.core.controller.BaseController;
import com.internal.common.core.domain.R;
import com.internal.common.core.page.TableDataInfo;
import com.internal.common.enums.BusinessType;
import com.internal.common.request.PageParams;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.system.domain.SystemEmail;
import com.internal.system.service.ISystemEmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 内置邮箱设置Controller
 *
 * @author internal
 * @date 2024-11-25
 */
@RestController
@AllArgsConstructor
@Api(value = "SystemEmailController", tags = "内置邮箱设置表")
@RequestMapping("/quote/email")
public class SystemEmailController extends BaseController {

    private final ISystemEmailService systemEmailService;

    /**
     * 查询内置邮箱设置列表
     */
    @ApiOperation(value = "查询内置邮箱设置列表", notes = "查询内置邮箱设置列表")
    @PreAuthorize("@ss.hasPermi('quote:email:list')")
    @PostMapping("/page")
    public TableDataInfo<List<SystemEmail>> list(@RequestBody PageParams<SystemEmail> params) {
        startPage(params);
        List<SystemEmail> list = systemEmailService.selectSystemEmailList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出内置邮箱设置列表
     */
    @ApiOperation(value = "导出内置邮箱设置列表", notes = "导出内置邮箱设置列表")
    @PreAuthorize("@ss.hasPermi('quote:email:export')")
    @Log(title = "内置邮箱设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SystemEmail systemEmail) {
        List<SystemEmail> list = systemEmailService.selectSystemEmailList(systemEmail);
        ExcelUtil<SystemEmail> util = new ExcelUtil<SystemEmail>(SystemEmail.class);
        util.exportExcel(response, list, "内置邮箱设置数据");
    }

    /**
     * 获取内置邮箱设置详细信息
     */
    @ApiOperation(value = "获取内置邮箱设置详细信息", notes = "获取内置邮箱设置详细信息")
    @PreAuthorize("@ss.hasPermi('quote:email:query')")
    @GetMapping(value = "/{id}")
    public R<SystemEmail> getInfo(@PathVariable("id") Long id) {
        return R.ok(systemEmailService.selectSystemEmailById(id));
    }

    /**
     * 新增内置邮箱设置
     */
    @ApiOperation(value = "新增内置邮箱设置", notes = "新增内置邮箱设置")
    @PreAuthorize("@ss.hasPermi('quote:email:add')")
    @Log(title = "内置邮箱设置", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody SystemEmail systemEmail) {
        return R.ok(systemEmailService.insertSystemEmail(systemEmail));
    }

    /**
     * 修改内置邮箱设置
     */
    @ApiOperation(value = "修改内置邮箱设置", notes = "修改内置邮箱设置")
    @PreAuthorize("@ss.hasPermi('quote:email:edit')")
    @Log(title = "内置邮箱设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody SystemEmail systemEmail) {
        return R.ok(systemEmailService.updateSystemEmail(systemEmail));
    }

    /**
     * 删除内置邮箱设置
     */
    @ApiOperation(value = "删除内置邮箱设置", notes = "删除内置邮箱设置")
    @PreAuthorize("@ss.hasPermi('quote:email:remove')")
    @Log(title = "内置邮箱设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids) {
        return R.ok(systemEmailService.deleteSystemEmailByIds(ids));
    }
}
