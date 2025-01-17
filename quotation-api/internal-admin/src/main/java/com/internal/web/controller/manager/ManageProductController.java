package com.internal.web.controller.manager;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import com.internal.common.request.PageParams;
import com.internal.manager.domain.ManageProduct;
import com.internal.manager.dto.ManageProductQueryDTO;
import com.internal.manager.dto.ManageProductQueryDTO;
import com.internal.manager.service.IManageProductService;
import com.internal.manager.vo.ManageProductChildVO;
import com.internal.manager.vo.ManageProductVO;
import com.internal.manager.vo.ManageProductChildVO;
import com.internal.manager.vo.ManageProductVO;
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
 * 管理系统-产品管理Controller
 * 
 * @author internal
 * @date 2024-10-11
 */
@RestController
@AllArgsConstructor
@Api(value = "ManageProductController", tags = "管理系统-产品管理表")
@RequestMapping("/manager/product")
public class ManageProductController extends BaseController
{

    private final IManageProductService manageProductService;

    /**
     * 查询管理系统-产品管理列表(主查询)
     */
    @ApiOperation(value = "查询管理系统-产品管理列表(主查询)", notes = "查询管理系统-产品管理列表(主查询)")
    @PreAuthorize("@ss.hasPermi('manager:product:list')")
    @PostMapping("/page")
    public TableDataInfo<List<ManageProductVO>> list(@RequestBody PageParams<ManageProductQueryDTO> params)
    {
        startPage(params);
        List<ManageProductVO> list = manageProductService.selectManageProductList(params.getModel());
        TableDataInfo<List<ManageProductVO>> result = getDataTable(list);
        Long count = manageProductService.selectManageProductCount(params.getModel());
        result.setTotal(count);
        return result;
    }

    /**
     * 查询未对应的主产品列表
     */
    @ApiOperation(value = "查询未对应的主产品列表", notes = "查询未对应的主产品列表- 需要category和status")
    @PreAuthorize("@ss.hasPermi('manager:product:getOptions')")
    @PostMapping("/getOptions")
    public List<ManageProductVO> getOptions(@RequestBody ManageProductQueryDTO dto)
    {
        List<ManageProductVO> list = manageProductService.getOptions(dto);
        return list;
    }

    /**
     * 查询管理系统-产品管理列表(子产品)
     */
    @ApiOperation(value = "查询管理系统-产品管理列表(子产品)", notes = "查询管理系统-产品管理列表(子产品)")
    @PreAuthorize("@ss.hasPermi('manager:product:list')")
    @PostMapping("/childrenPage")
    public TableDataInfo<List<ManageProductChildVO>> childrenList(@RequestBody PageParams<ManageProductQueryDTO> params)
    {
        params.getModel().setIsAuth(true);
        startPage(params);
        List<ManageProductChildVO> list = manageProductService.selectProductChildrenList(params.getModel());
        TableDataInfo<List<ManageProductChildVO>> result = getDataTable(list);
        Long count = manageProductService.selectProductChildrenCount(params.getModel());
        result.setTotal(count);
        return result;
    }

    /**
     * 导出主产品
     */
    @ApiOperation(value = "导出主产品", notes = "导出主产品")
    @PreAuthorize("@ss.hasPermi('manager:product:export')")
    @Log(title = "管理系统-导出主产品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ManageProductQueryDTO queryDTO)
    {
        List<ManageProductVO> list = manageProductService.selectManageProductList(queryDTO);
        ExcelUtil<ManageProductVO> util = new ExcelUtil<ManageProductVO>(ManageProductVO.class);
        util.exportExcel(response, list, "管理系统-产品管理数据");
    }

    /**
     * 导出子产品(暂时不用，留口)
     */
    @ApiOperation(value = "导出子产品", notes = "导出子产品")
    @PreAuthorize("@ss.hasPermi('manager:product:export')")
    @Log(title = "管理系统-导出子产品", businessType = BusinessType.EXPORT)
    @PostMapping("/exportChildren")
    public void exportChildren(HttpServletResponse response, ManageProductQueryDTO queryDTO)
    {
        List<ManageProductChildVO> list = manageProductService.selectProductChildrenList(queryDTO);
        ExcelUtil<ManageProductChildVO> util = new ExcelUtil<ManageProductChildVO>(ManageProductChildVO.class);
        util.exportExcel(response, list, "管理系统-产品管理数据");
    }

    /**
     * 获取管理系统-产品管理详细信息
     */
    @ApiOperation(value = "获取管理系统-产品管理详细信息", notes = "获取管理系统-产品管理详细信息")
    @PreAuthorize("@ss.hasPermi('manager:product:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(manageProductService.selectManageProductById(id));
    }

    /**
     * 新增管理系统-产品管理
     */
    @ApiOperation(value = "新增管理系统-产品管理", notes = "新增管理系统-产品管理")
    @PreAuthorize("@ss.hasPermi('manager:product:add')")
    @Log(title = "管理系统-产品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ManageProduct manageProduct)
    {
        return toAjax(manageProductService.insertManageProduct(manageProduct));
    }

    /**
     * 修改管理系统-产品管理
     */
    @ApiOperation(value = "修改管理系统-产品管理", notes = "修改管理系统-产品管理")
    @PreAuthorize("@ss.hasPermi('manager:product:edit')")
    @Log(title = "管理系统-产品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ManageProduct manageProduct)
    {
        return toAjax(manageProductService.updateManageProduct(manageProduct));
    }

    /**
     * 删除管理系统-产品管理
     */
    @ApiOperation(value = "删除管理系统-产品管理", notes = "删除管理系统-产品管理")
    @PreAuthorize("@ss.hasPermi('manager:product:remove')")
    @Log(title = "管理系统-产品管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(manageProductService.deleteManageProductByIds(ids));
    }

    /**
     * 产品管理-上架
     */
    @ApiOperation(value = "产品管理-上架", notes = "产品管理-上架(需要id和category)")
    @PreAuthorize("@ss.hasPermi('manager:product:publish')")
    @PostMapping("/publish")
    public AjaxResult publish(@RequestBody ManageProduct product)
    {
        return toAjax(manageProductService.publish(product));
    }
    /**
     * 产品管理-下架
     */
    @ApiOperation(value = "产品管理-下架", notes = "产品管理-下架(需要id和category)")
    @PreAuthorize("@ss.hasPermi('manager:product:unPublish')")
    @PostMapping("/unPublish")
    public AjaxResult unPublish(@RequestBody ManageProduct product)
    {
        return toAjax(manageProductService.unPublish(product));
    }
}
