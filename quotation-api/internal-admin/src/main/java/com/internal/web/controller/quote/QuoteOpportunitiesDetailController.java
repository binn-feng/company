package com.internal.web.controller.quote;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.internal.common.core.domain.R;
import com.internal.common.enums.VersionStatus;
import com.internal.common.request.PageParams;
import com.internal.manager.domain.ManageHardwareExt;
import com.internal.manager.domain.ManageHardwareSelf;
import com.internal.manager.dto.ManageProductQueryDTO;
import com.internal.manager.service.IManageHardwareExtService;
import com.internal.manager.service.IManageHardwareSelfService;
import com.internal.manager.service.IManageProductService;
import com.internal.manager.vo.ManageProductChildVO;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesDetailSaveDTO;
import com.internal.quote.vo.OpportunitiesDetailVersionVO;
import com.internal.quote.vo.QuoteOpportunitiesDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import com.internal.quote.domain.QuoteOpportunitiesDetail;
import com.internal.quote.service.IQuoteOpportunitiesDetailService;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.common.core.page.TableDataInfo;

/**
 * 商机详细报价表Controller
 * 
 * @author internal
 * @date 2024-10-15
 */
@RestController
@AllArgsConstructor
@Api(value = "QuoteOpportunitiesDetailController", tags = "商机详细报价表")
@RequestMapping("/quote/opportunities/detail")
public class QuoteOpportunitiesDetailController extends BaseController
{

    private final IQuoteOpportunitiesDetailService quoteOpportunitiesDetailService;
    private final IManageProductService manageProductService;
    private final IManageHardwareExtService hardwareExtService;
    private final IManageHardwareSelfService hardwareSelfService;


    /**
     * 查询报价系统-商机详细报价列表
     */
    @ApiOperation(value = "查询报价系统-商机详细报价列表", notes = "查询报价系统-商机详细报价列表")
    @PreAuthorize("@ss.hasPermi('quote:detail:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuoteOpportunitiesDetail>> list(@RequestBody PageParams<QuoteOpportunitiesDetail> params)
    {
        startPage(params);
        List<QuoteOpportunitiesDetail> list = quoteOpportunitiesDetailService.selectQuoteOpportunitiesDetailList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出报价系统-商机详细报价列表
     */
    @ApiOperation(value = "导出报价系统-商机详细报价列表", notes = "导出报价系统-商机详细报价列表")
    @PreAuthorize("@ss.hasPermi('quote:detail:export')")
    @Log(title = "报价系统-商机详细报价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuoteOpportunitiesDetail quoteOpportunitiesDetail)
    {
        List<QuoteOpportunitiesDetail> list = quoteOpportunitiesDetailService.selectQuoteOpportunitiesDetailList(quoteOpportunitiesDetail);
        ExcelUtil<QuoteOpportunitiesDetail> util = new ExcelUtil<QuoteOpportunitiesDetail>(QuoteOpportunitiesDetail.class);
        util.exportExcel(response, list, "报价系统-商机详细报价数据");
    }

    /**
     * 获取报价系统-商机详细报价详细信息
     */
    @ApiOperation(value = "获取报价系统-商机详细报价详细信息", notes = "获取报价系统-商机详细报价详细信息")
    /*@PreAuthorize("@ss.hasPermi('quote:await:detail')")*/
    @PostMapping(value = "/getDetailInfo")
    public R<QuoteOpportunitiesDetailVO> getDetailInfo(@RequestBody QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery)
    {
        return R.ok(quoteOpportunitiesDetailService.getDetailInfo(quoteOpportunitiesDetailQuery));
    }

    /**
     * 新增报价系统-商机详细报价
     */
    @ApiOperation(value = "新增报价系统-商机详细报价", notes = "新增报价系统-商机详细报价")
    @PreAuthorize("@ss.hasPermi('quote:await:detail:quote','quote:finish:update','quote:cannot:update','quote:await:update:update')")
    @Log(title = "报价系统-商机详细报价", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody @Validated QuoteOpportunitiesDetailSaveDTO quoteOpportunitiesDetailSaveDTO)
    {
        return R.ok(quoteOpportunitiesDetailService.insertQuoteOpportunitiesDetail(quoteOpportunitiesDetailSaveDTO));
    }

    /**
     * 修改报价系统-商机详细报价
     */
    @ApiOperation(value = "修改报价系统-商机详细报价", notes = "修改报价系统-商机详细报价")
    @PreAuthorize("@ss.hasPermi('quote:detail:edit')")
    @Log(title = "报价系统-商机详细报价", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuoteOpportunitiesDetail quoteOpportunitiesDetail)
    {
        return R.ok(quoteOpportunitiesDetailService.updateQuoteOpportunitiesDetail(quoteOpportunitiesDetail));
    }

    /**
     * 删除报价系统-商机详细报价
     */
    @ApiOperation(value = "删除报价系统-商机详细报价", notes = "删除报价系统-商机详细报价")
    @PreAuthorize("@ss.hasPermi('quote:detail:remove')")
    @Log(title = "报价系统-商机详细报价", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids)
    {
        return R.ok(quoteOpportunitiesDetailService.deleteQuoteOpportunitiesDetailByIds(ids));
    }

    /**
     * 获取版本号列表
     */
    @ApiOperation(value = "获取版本号列表", notes = "获取版本号列表")
    @PostMapping("/version")
    public R<List<OpportunitiesDetailVersionVO>> getVersionList(@RequestBody QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery)
    {
        if(ObjectUtil.isNotEmpty(quoteOpportunitiesDetailQuery.getLatest()) && quoteOpportunitiesDetailQuery.getLatest()){
            // 获取最新记录的version
            QuoteOpportunitiesDetail opportunitiesDetail = quoteOpportunitiesDetailService.list(Wrappers.<QuoteOpportunitiesDetail>lambdaQuery()
                            .eq(QuoteOpportunitiesDetail::getOpportunitiesId, quoteOpportunitiesDetailQuery.getOpportunitiesId())
                            .eq(QuoteOpportunitiesDetail::getStatus, VersionStatus.INACTIVE.getCode()))
                    .stream().max(Comparator.comparing(QuoteOpportunitiesDetail::getCreateTime)).orElse(null);
            if(ObjectUtil.isNotEmpty(opportunitiesDetail)){
                return R.ok(Lists.newArrayList(OpportunitiesDetailVersionVO.builder()
                        .id(opportunitiesDetail.getId())
                        .opportunitiesId(opportunitiesDetail.getOpportunitiesId())
                        .valuationVersion(opportunitiesDetail.getValuationVersion())
                        .build()));
            }
            return R.ok(null);
        }

        List<QuoteOpportunitiesDetail> quoteOpportunitiesDetailList = quoteOpportunitiesDetailService
                .list(Wrappers.<QuoteOpportunitiesDetail>lambdaQuery().eq(QuoteOpportunitiesDetail::getOpportunitiesId, quoteOpportunitiesDetailQuery.getOpportunitiesId()));
        if(CollUtil.isNotEmpty(quoteOpportunitiesDetailList)){
            return R.ok(quoteOpportunitiesDetailList.stream()
                    .map(item -> OpportunitiesDetailVersionVO.builder()
                            .id(item.getId())
                            .opportunitiesId(item.getOpportunitiesId())
                            .valuationVersion(item.getValuationVersion())
                            .build())
                    .collect(Collectors.toList()));
        }
        return R.ok(null);
    }

    /**
     * 查询子产品列表
     */
    @ApiOperation(value = "查询子产品列表", notes = "查询子产品列表")
    //@PreAuthorize("@ss.hasPermi('quote:detail:products')")
    @PostMapping("/selectProduct")
    public TableDataInfo<List<ManageProductChildVO>> selectProduct(@RequestBody ManageProductQueryDTO dto)
    {
        dto.setIsAuth(Boolean.FALSE);
        List<ManageProductChildVO> list = manageProductService.selectProductChildrenList(dto);
        TableDataInfo<List<ManageProductChildVO>> result = getDataTable(list);
        return result;
    }

    /**
     * 查询硬件自研列表
     */
    @ApiOperation(value = "查询硬件自研列表", notes = "查询硬件自研列表")
    //@PreAuthorize("@ss.hasPermi('quote:detail:products')")
    @PostMapping("/selectHardwareSelf")
    public List<ManageHardwareSelf> selectHardwareSelf(@RequestBody ManageHardwareSelf dto)
    {
        List<ManageHardwareSelf> list = hardwareSelfService.selectManageHardwareSelfList(dto);
        return list;
    }

    /**
     * 查询硬件外采列表
     */
    @ApiOperation(value = "查询硬件外采列表", notes = "查询硬件外采列表")
    //@PreAuthorize("@ss.hasPermi('quote:detail:products')")
    @PostMapping("/selectHardwareExt")
    public List<ManageHardwareExt> selectHardwareExt(@RequestBody ManageHardwareExt dto)
    {
        List<ManageHardwareExt> list = hardwareExtService.selectManageHardwareExtList(dto);
        return list;
    }


    /**
     * 查询管理系统-硬件外采列表
     */
    @ApiOperation(value = "查询管理系统-硬件外采列表", notes = "查询管理系统-硬件外采列表")
    @PostMapping("/hardware/ext/page")
    public TableDataInfo<List<ManageHardwareExt>> hardwareExtPage(@RequestBody PageParams<ManageHardwareExt> params)
    {
        startPage(params);
        List<ManageHardwareExt> list = hardwareExtService.selectManageHardwareExtList(params.getModel());
        return getDataTable(list);
    }
}
