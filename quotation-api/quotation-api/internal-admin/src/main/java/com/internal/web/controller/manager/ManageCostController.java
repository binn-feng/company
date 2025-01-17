package com.internal.web.controller.manager;

import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.common.annotation.Anonymous;
import com.internal.common.annotation.Log;
import com.internal.common.core.controller.BaseController;
import com.internal.common.core.domain.AjaxResult;
import com.internal.common.core.domain.R;
import com.internal.common.core.page.TableDataInfo;
import com.internal.common.enums.BusinessType;
import com.internal.common.enums.CostType;
import com.internal.common.request.PageParams;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.manager.domain.ManageCost;
import com.internal.manager.dto.ManageCostSaveDTO;
import com.internal.manager.service.IManageCostService;
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
 * 管理系统-成本设置Controller
 *
 * @author internal
 * @date 2024-10-09
 */
@RestController
@AllArgsConstructor
@Api(value = "ManageCostController", tags = "管理系统-成本设置表")
@RequestMapping("/manager/cost")
public class ManageCostController extends BaseController
{

    private final IManageCostService manageCostService;

    /**
     * 查询管理系统-成本设置列表
     */
    @ApiOperation(value = "查询管理系统-成本设置列表", notes = "查询管理系统-成本设置列表")
    @PreAuthorize("@ss.hasPermi('manager:cost:list')")
    @PostMapping("/page")
    public TableDataInfo<List<ManageCost>> list(@RequestBody PageParams<ManageCost> params)
    {
         startPage(params);
        List<ManageCost> list = manageCostService.selectManageCostList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 查询管理系统-成本设置map
     */
    @ApiOperation(value = "查询管理系统-成本设置map", notes = "查询管理系统-成本设置map")
    //@PreAuthorize("@ss.hasPermi('manager:cost:map')")
    @PostMapping("/getCostMap")
    public R<Map<CostType,ManageCost>> getCostMap()
    {
        Map<CostType,ManageCost> map = manageCostService.getCostMap();
        return R.ok(map);
    }

    /**
     * 导出管理系统-成本设置列表
     */
    @ApiOperation(value = "导出管理系统-成本设置列表", notes = "导出管理系统-成本设置列表")
    @PreAuthorize("@ss.hasPermi('manager:cost:export')")
    @Log(title = "管理系统-成本设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ManageCost manageCost)
    {
        List<ManageCost> list = manageCostService.selectManageCostList(manageCost);
        ExcelUtil<ManageCost> util = new ExcelUtil<ManageCost>(ManageCost.class);
        util.exportExcel(response, list, "管理系统-成本设置数据");
    }

    /**
     * 获取管理系统-成本设置详细信息
     */
    @ApiOperation(value = "获取管理系统-成本设置详细信息", notes = "获取管理系统-成本设置详细信息")
    @PreAuthorize("@ss.hasPermi('manager:cost:query')")
    @GetMapping(value = "/{id}")
    public R<ManageCost> getInfo(@PathVariable("id") Long id)
    {
        return R.ok(manageCostService.selectManageCostById(id));
    }

    /**
     * 新增管理系统-成本设置
     */
    @ApiOperation(value = "新增管理系统-成本设置", notes = "新增管理系统-成本设置")
    @PreAuthorize("@ss.hasPermi('manager:cost:add')")
    @Log(title = "管理系统-成本设置", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody ManageCost manageCost)
    {
        manageCostService.remove(new QueryWrapper<ManageCost>().isNotNull("id"));
        return R.ok(manageCostService.insertManageCost(manageCost));
    }

    /**
     * 新增管理系统-成本设置
     */
    @ApiOperation(value = "批量新增管理系统-成本设置", notes = "批量新增管理系统-成本设置")
    @PreAuthorize("@ss.hasPermi('manager:cost:add')")
    @Log(title = "管理系统-成本设置", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public R<Boolean> batchAdd(@RequestBody ManageCostSaveDTO manageCostSaveDTO)
    {
        manageCostService.remove(new QueryWrapper<ManageCost>().isNotNull("id"));
        manageCostSaveDTO.getManageCostList().forEach(item -> item.setId(null));
        return R.ok(manageCostService.saveBatch(manageCostSaveDTO.getManageCostList(),50));
    }

    /**
     * 修改管理系统-成本设置
     */
    @ApiOperation(value = "修改管理系统-成本设置", notes = "修改管理系统-成本设置")
    @PreAuthorize("@ss.hasPermi('manager:cost:edit')")
    @Log(title = "管理系统-成本设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody ManageCost manageCost)
    {
        return R.ok(manageCostService.updateManageCost(manageCost));
    }

    /**
     * 删除管理系统-成本设置
     */
    @ApiOperation(value = "删除管理系统-成本设置", notes = "删除管理系统-成本设置")
    @PreAuthorize("@ss.hasPermi('manager:cost:remove')")
    @Log(title = "管理系统-成本设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids)
    {
        return R.ok(manageCostService.deleteManageCostByIds(ids));
    }
}
