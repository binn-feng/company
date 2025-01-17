package com.internal.web.controller.quote;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.internal.common.core.domain.R;
import jakarta.servlet.http.HttpServletResponse;

import com.internal.common.enums.VersionStatus;
import com.internal.common.request.PageParams;
import com.internal.quote.dto.QuoteOpportunitiesDetailQuery;
import com.internal.quote.dto.QuoteOpportunitiesRoughQuery;
import com.internal.quote.dto.QuoteOpportunitiesRoughSaveDTO;
import com.internal.quote.vo.OpportunitiesDetailVersionVO;
import com.internal.quote.vo.QuoteOpportunitiesRoughVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.internal.quote.domain.QuoteOpportunitiesRough;
import com.internal.quote.service.IQuoteOpportunitiesRoughService;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.common.core.page.TableDataInfo;

/**
 * 商机粗略报价Controller
 * 
 * @author internal
 * @date 2024-10-17
 */
@RestController
@AllArgsConstructor
@Api(value = "QuoteOpportunitiesRoughController", tags = "商机粗略报价表")
@RequestMapping("/quote/rough")
public class QuoteOpportunitiesRoughController extends BaseController
{

    private final IQuoteOpportunitiesRoughService quoteOpportunitiesRoughService;

    /**
     * 查询报价系统-商机粗略报价列表
     */
    @ApiOperation(value = "查询报价系统-商机粗略报价列表", notes = "查询报价系统-商机粗略报价列表")
    @PreAuthorize("@ss.hasPermi('quote:rough:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuoteOpportunitiesRough>> list(@RequestBody PageParams<QuoteOpportunitiesRough> params)
    {
        startPage(params);
        List<QuoteOpportunitiesRough> list = quoteOpportunitiesRoughService.selectQuoteOpportunitiesRoughList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出报价系统-商机粗略报价列表
     */
    @ApiOperation(value = "导出报价系统-商机粗略报价列表", notes = "导出报价系统-商机粗略报价列表")
    @PreAuthorize("@ss.hasPermi('quote:rough:export')")
    @Log(title = "报价系统-商机粗略报价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuoteOpportunitiesRough quoteOpportunitiesRough)
    {
        List<QuoteOpportunitiesRough> list = quoteOpportunitiesRoughService.selectQuoteOpportunitiesRoughList(quoteOpportunitiesRough);
        ExcelUtil<QuoteOpportunitiesRough> util = new ExcelUtil<QuoteOpportunitiesRough>(QuoteOpportunitiesRough.class);
        util.exportExcel(response, list, "报价系统-商机粗略报价数据");
    }

    /**
     * 获取报价系统-商机粗略报价详细信息
     */
    @ApiOperation(value = "获取报价系统-商机粗略报价详细信息", notes = "获取报价系统-商机粗略报价详细信息")
    /*@PreAuthorize("@ss.hasPermi('quote:rough:query')")*/
    @PostMapping(value = "/getRoughInfo")
    public R<QuoteOpportunitiesRoughVO> getRoughInfo(@RequestBody QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery)
    {
        return  R.ok(quoteOpportunitiesRoughService.getRoughInfo(quoteOpportunitiesDetailQuery));
    }

    /**
     * 新增报价系统-商机粗略报价
     */
    @ApiOperation(value = "新增报价系统-商机粗略报价", notes = "新增报价系统-商机粗略报价")
    @PreAuthorize("@ss.hasPermi('quote:await:rough:quote','quote:finish:update','quote:cannot:update','quote:await:update:update')")
    @Log(title = "报价系统-商机粗略报价", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody QuoteOpportunitiesRoughSaveDTO quoteOpportunitiesRough)
    {
        return R.ok(quoteOpportunitiesRoughService.insertQuoteOpportunitiesRough(quoteOpportunitiesRough));
    }

    /**
     * 修改报价系统-商机粗略报价
     */
    @ApiOperation(value = "修改报价系统-商机粗略报价", notes = "修改报价系统-商机粗略报价")
    @PreAuthorize("@ss.hasPermi('quote:rough:edit')")
    @Log(title = "报价系统-商机粗略报价", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuoteOpportunitiesRough quoteOpportunitiesRough)
    {
        return R.ok(quoteOpportunitiesRoughService.updateQuoteOpportunitiesRough(quoteOpportunitiesRough));
    }

    /**
     * 删除报价系统-商机粗略报价
     */
    @ApiOperation(value = "删除报价系统-商机粗略报价", notes = "删除报价系统-商机粗略报价")
    @PreAuthorize("@ss.hasPermi('quote:rough:remove')")
    @Log(title = "报价系统-商机粗略报价", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids)
    {
        return R.ok(quoteOpportunitiesRoughService.deleteQuoteOpportunitiesRoughByIds(ids));
    }

    /**
     * 获取版本号列表
     */
    @ApiOperation(value = "获取版本号列表", notes = "获取版本号列表")
    @PostMapping("/version")
    public R<List<OpportunitiesDetailVersionVO>> getVersionList(@RequestBody QuoteOpportunitiesRoughQuery quoteOpportunitiesRoughQuery)
    {
        if(ObjectUtil.isNotEmpty(quoteOpportunitiesRoughQuery.getLatest()) && quoteOpportunitiesRoughQuery.getLatest()){
            // 获取最新记录的version
            QuoteOpportunitiesRough opportunitiesRough = quoteOpportunitiesRoughService.list(Wrappers.<QuoteOpportunitiesRough>lambdaQuery()
                            .eq(QuoteOpportunitiesRough::getOpportunitiesId, quoteOpportunitiesRoughQuery.getOpportunitiesId())
                            .eq(QuoteOpportunitiesRough::getStatus, VersionStatus.INACTIVE.getCode()))
                    .stream().max(Comparator.comparing(QuoteOpportunitiesRough::getCreateTime)).orElse(null);
            if(ObjectUtil.isNotEmpty(opportunitiesRough)){
                return R.ok(Lists.newArrayList(OpportunitiesDetailVersionVO.builder()
                        .id(opportunitiesRough.getId())
                        .amount(opportunitiesRough.getAmount())
                        .date(new SimpleDateFormat("yyyy-MM-dd").format(opportunitiesRough.getCreateTime()))
                        .opportunitiesId(opportunitiesRough.getOpportunitiesId())
                        .valuationVersion(opportunitiesRough.getValuationVersion())
                        .build()));
            }
            return R.ok(null);
        }

        List<QuoteOpportunitiesRough> quoteOpportunitiesDetailList = quoteOpportunitiesRoughService
                .list(Wrappers.<QuoteOpportunitiesRough>lambdaQuery().eq(QuoteOpportunitiesRough::getOpportunitiesId, quoteOpportunitiesRoughQuery.getOpportunitiesId()));
        if(CollUtil.isNotEmpty(quoteOpportunitiesDetailList)){
            return R.ok(quoteOpportunitiesDetailList.stream()
                    .map(item -> OpportunitiesDetailVersionVO.builder()
                            .id(item.getId())
                            .amount(item.getAmount())
                            .date(new SimpleDateFormat("yyyy-MM-dd").format(item.getCreateTime()))
                            .opportunitiesId(item.getOpportunitiesId())
                            .valuationVersion(item.getValuationVersion())
                            .build())
                    .collect(Collectors.toList()));
        }
        return R.ok(null);
    }
}
