package com.internal.web.controller.manager;

import com.internal.common.annotation.Log;
import com.internal.common.core.controller.BaseController;
import com.internal.common.core.domain.R;
import com.internal.common.core.page.TableDataInfo;
import com.internal.common.enums.BusinessType;
import com.internal.common.request.PageParams;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.manager.domain.ManagerBuGoals;
import com.internal.manager.dto.ManagerBuGoalsQueryDTO;
import com.internal.manager.service.IManagerBuGoalsService;
import com.internal.manager.vo.ManagerBuGoalsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 管理系统-目标管理子Controller
 *
 * @author internal
 * @date 2024-12-30
 */
@RestController
@AllArgsConstructor
@Api(value = "ManagerBuGoalsController", tags = "管理系统-目标管理子表")
@RequestMapping("/manager/goals")
public class ManagerBuGoalsController extends BaseController {

    private final IManagerBuGoalsService managerBuGoalsService;

    /**
     * 查询管理系统-目标管理子列表
     */
    @ApiOperation(value = "查询管理系统-目标管理子列表", notes = "查询管理系统-目标管理子列表")
    @PreAuthorize("@ss.hasPermi('manager:goals:list')")
    @PostMapping("/page")
    public TableDataInfo<List<ManagerBuGoalsVO>> list(@RequestBody PageParams<ManagerBuGoalsQueryDTO> params) {
        startPage(params);
        List<ManagerBuGoalsVO> list = managerBuGoalsService.selectManagerBuGoalsList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出管理系统-目标管理子列表
     */
    @ApiOperation(value = "导出管理系统-目标管理子列表", notes = "导出管理系统-目标管理子列表")
    @PreAuthorize("@ss.hasPermi('manager:goals:export')")
    @Log(title = "管理系统-目标管理子", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ManagerBuGoalsQueryDTO managerBuGoals) {
        List<ManagerBuGoalsVO> list = managerBuGoalsService.selectManagerBuGoalsList(managerBuGoals);
        ExcelUtil<ManagerBuGoalsVO> util = new ExcelUtil<ManagerBuGoalsVO>(ManagerBuGoalsVO.class);
        util.exportExcel(response, list, "管理系统-目标管理子数据");
    }

    /**
     * 获取管理系统-目标管理子详细信息
     */
    @ApiOperation(value = "获取管理系统-目标管理子详细信息", notes = "获取管理系统-目标管理子详细信息")
    @PreAuthorize("@ss.hasPermi('manager:goals:query')")
    @GetMapping(value = "/{id}")
    public R<ManagerBuGoals> getInfo(@PathVariable("id") Long id) {
        return R.ok(managerBuGoalsService.selectManagerBuGoalsById(id));
    }

    /**
     * 新增管理系统-目标管理子
     */
    @ApiOperation(value = "新增管理系统-目标管理子", notes = "新增管理系统-目标管理子")
    @PreAuthorize("@ss.hasPermi('manager:goals:add')")
    @Log(title = "管理系统-目标管理子", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody ManagerBuGoals managerBuGoals) {
        return R.ok(managerBuGoalsService.insertManagerBuGoals(managerBuGoals));
    }

    /**
     * 修改管理系统-目标管理子
     */
    @ApiOperation(value = "修改管理系统-目标管理子", notes = "修改管理系统-目标管理子")
    @PreAuthorize("@ss.hasPermi('manager:goals:edit')")
    @Log(title = "管理系统-目标管理子", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody ManagerBuGoals managerBuGoals) {
        return R.ok(managerBuGoalsService.updateManagerBuGoals(managerBuGoals));
    }

    /**
     * 删除管理系统-目标管理子
     */
    @ApiOperation(value = "删除管理系统-目标管理子", notes = "删除管理系统-目标管理子")
    @PreAuthorize("@ss.hasPermi('manager:goals:remove')")
    @Log(title = "管理系统-目标管理子", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids) {
        return R.ok(managerBuGoalsService.deleteManagerBuGoalsByIds(ids));
    }
}
