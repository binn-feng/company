package com.internal.web.controller.manager;

import java.util.List;
import com.internal.common.core.domain.R;
import jakarta.servlet.http.HttpServletResponse;
import com.internal.common.request.PageParams;
import com.internal.manager.dto.ManageSoftOptionsDTO;
import com.internal.manager.dto.ManageSoftQueryDTO;
import com.internal.manager.dto.ManageSoftSaveDTO;
import com.internal.manager.vo.ManageProductVO;
import com.internal.manager.vo.ManageSoftVO;
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
import com.internal.manager.domain.ManageSoft;
import com.internal.manager.service.IManageSoftService;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.common.core.page.TableDataInfo;

/**
 * 管理系统-软著管理Controller
 * 
 * @author internal
 * @date 2024-12-11
 */
@RestController
@AllArgsConstructor
@Api(value = "ManageSoftController", tags = "管理系统-软著管理表")
@RequestMapping("/manager/soft")
public class ManageSoftController extends BaseController
{

    private final IManageSoftService manageSoftService;

    /**
     * 查询管理系统-软著管理列表
     */
    @ApiOperation(value = "查询管理系统-软著管理列表", notes = "查询管理系统-软著管理列表")
    @PreAuthorize("@ss.hasPermi('manager:soft:list')")
    @PostMapping("/page")
    public TableDataInfo<List<ManageSoftVO>> list(@RequestBody PageParams<ManageSoftQueryDTO> params)
    {
        startPage(params);
        List<ManageSoftVO> list = manageSoftService.selectManageSoftList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 查询未对应的软著列表
     */
    @ApiOperation(value = "查询未对应的软著列表", notes = "查询未对应的软著列表-需要category和from")
    @PreAuthorize("@ss.hasPermi('manager:soft:getOptions')")
    @PostMapping("/getOptions")
    public List<ManageSoftVO> getOptions(@RequestBody ManageSoftOptionsDTO dto)
    {
        List<ManageSoftVO> list = manageSoftService.getOptions(dto);
        return list;
    }

    /**
     * 导出管理系统-软著管理列表
     */
    @ApiOperation(value = "导出管理系统-软著管理列表", notes = "导出管理系统-软著管理列表")
    @PreAuthorize("@ss.hasPermi('manager:soft:export')")
    @Log(title = "管理系统-软著管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ManageSoftQueryDTO manageSoft)
    {
        List<ManageSoftVO> list = manageSoftService.selectManageSoftList(manageSoft);
        ExcelUtil<ManageSoftVO> util = new ExcelUtil<ManageSoftVO>(ManageSoftVO.class);
        util.exportExcel(response, list, "管理系统-软著管理数据");
    }

    /**
     * 获取管理系统-软著管理详细信息
     */
    @ApiOperation(value = "获取管理系统-软著管理详细信息", notes = "获取管理系统-软著管理详细信息")
    @PreAuthorize("@ss.hasPermi('manager:soft:query')")
    @GetMapping(value = "/{id}")
    public R<ManageSoft> getInfo(@PathVariable("id") Long id)
    {
        return  R.ok(manageSoftService.selectManageSoftById(id));
    }

    /**
     * 新增管理系统-软著管理
     */
    @ApiOperation(value = "新增管理系统-软著管理", notes = "新增管理系统-软著管理")
    @PreAuthorize("@ss.hasPermi('manager:soft:add')")
    @Log(title = "管理系统-软著管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@RequestBody ManageSoftSaveDTO manageSoft)
    {
        return R.ok(manageSoftService.insertManageSoft(manageSoft));
    }

    /**
     * 修改管理系统-软著管理
     */
    @ApiOperation(value = "修改管理系统-软著管理", notes = "修改管理系统-软著管理")
    @PreAuthorize("@ss.hasPermi('manager:soft:edit')")
    @Log(title = "管理系统-软著管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Boolean> edit(@RequestBody ManageSoft manageSoft)
    {
        return R.ok(manageSoftService.updateManageSoft(manageSoft));
    }

    /**
     * 删除管理系统-软著管理
     */
    @ApiOperation(value = "删除管理系统-软著管理", notes = "删除管理系统-软著管理")
    @PreAuthorize("@ss.hasPermi('manager:soft:remove')")
    @Log(title = "管理系统-软著管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids)
    {
        return R.ok(manageSoftService.deleteManageSoftByIds(ids));
    }
}
