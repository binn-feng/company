package com.internal.web.controller.manager;

import java.util.List;
import com.internal.common.core.domain.R;
import jakarta.servlet.http.HttpServletResponse;
import com.internal.common.request.PageParams;
import com.internal.manager.domain.ManageHardwareExt;
import com.internal.manager.domain.ManageHardwareSelf;
import com.internal.manager.service.IManageHardwareSelfService;
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
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.common.core.page.TableDataInfo;

/**
 * 管理系统-硬件自研Controller
 * 
 * @author internal
 * @date 2024-12-19
 */
@RestController
@AllArgsConstructor
@Api(value = "ManageHardwareSelfController", tags = "管理系统-硬件自研表")
@RequestMapping("/manager/self")
public class ManageHardwareSelfController extends BaseController
{

    private final IManageHardwareSelfService manageHardwareSelfService;

    /**
     * 查询管理系统-硬件自研列表
     */
    @ApiOperation(value = "查询管理系统-硬件自研列表", notes = "查询管理系统-硬件自研列表")
    @PreAuthorize("@ss.hasPermi('quote:self:list')")
    @PostMapping("/page")
    public TableDataInfo<List<ManageHardwareSelf>> list(@RequestBody PageParams<ManageHardwareSelf> params)
    {
        startPage(params);
        List<ManageHardwareSelf> list = manageHardwareSelfService.selectManageHardwareSelfList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出管理系统-硬件自研列表
     */
    @ApiOperation(value = "导出管理系统-硬件自研列表", notes = "导出管理系统-硬件自研列表")
    @PreAuthorize("@ss.hasPermi('quote:self:export')")
    @Log(title = "管理系统-硬件自研", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ManageHardwareSelf manageHardwareSelf)
    {
        List<ManageHardwareSelf> list = manageHardwareSelfService.selectManageHardwareSelfList(manageHardwareSelf);
        ExcelUtil<ManageHardwareSelf> util = new ExcelUtil<ManageHardwareSelf>(ManageHardwareSelf.class);
        util.exportExcel(response, list, "管理系统-硬件自研数据");
    }

    /**
     * 获取管理系统-硬件自研详细信息
     */
    @ApiOperation(value = "获取管理系统-硬件自研详细信息", notes = "获取管理系统-硬件自研详细信息")
    @PreAuthorize("@ss.hasPermi('quote:self:query')")
    @GetMapping(value = "/{id}")
    public R<ManageHardwareSelf> getInfo(@PathVariable("id") Long id)
    {
        return  R.ok(manageHardwareSelfService.selectManageHardwareSelfById(id));
    }

    /**
     * 新增管理系统-硬件自研
     */
    @ApiOperation(value = "新增管理系统-硬件自研", notes = "新增管理系统-硬件自研")
    @PreAuthorize("@ss.hasPermi('quote:self:add')")
    @Log(title = "管理系统-硬件自研", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody ManageHardwareSelf manageHardwareSelf)
    {
        return R.ok(manageHardwareSelfService.insertManageHardwareSelf(manageHardwareSelf));
    }

    /**
     * 修改管理系统-硬件自研
     */
    @ApiOperation(value = "修改管理系统-硬件自研", notes = "修改管理系统-硬件自研")
    @PreAuthorize("@ss.hasPermi('quote:self:edit')")
    @Log(title = "管理系统-硬件自研", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody ManageHardwareSelf manageHardwareSelf)
    {
        return R.ok(manageHardwareSelfService.updateManageHardwareSelf(manageHardwareSelf));
    }

    /**
     * 删除管理系统-硬件自研
     */
    @ApiOperation(value = "删除管理系统-硬件自研", notes = "删除管理系统-硬件自研")
    @PreAuthorize("@ss.hasPermi('quote:self:remove')")
    @Log(title = "管理系统-硬件自研", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids)
    {
        return R.ok(manageHardwareSelfService.deleteManageHardwareSelfByIds(ids));
    }

    /**
     * 上架
     */
    @ApiOperation(value = "上架", notes = "上架(需要id")
    @PreAuthorize("@ss.hasPermi('manager:self:publish')")
    @PostMapping("/publish")
    public AjaxResult publish(@RequestBody ManageHardwareSelf self)
    {
        return toAjax(manageHardwareSelfService.publish(self));
    }
    /**
     * 下架
     */
    @ApiOperation(value = "下架", notes = "下架(需要id)")
    @PreAuthorize("@ss.hasPermi('manager:self:unPublish')")
    @PostMapping("/unPublish")
    public AjaxResult unPublish(@RequestBody ManageHardwareSelf self)
    {
        return toAjax(manageHardwareSelfService.unPublish(self));
    }
}
