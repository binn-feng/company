package com.internal.web.controller.manager;
import cn.hutool.core.util.ObjectUtil;
import com.internal.common.core.controller.BaseController;
import com.internal.common.core.domain.R;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.core.page.TableDataInfo;
import com.internal.common.request.PageParams;
import com.internal.common.utils.SecurityUtils;
import com.internal.quote.dto.*;
import com.internal.quote.service.*;
import com.internal.quote.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理系统-报表Controller
 *
 * @author internal
 * @date 2024-10-09
 */
@RestController
@AllArgsConstructor
@Api(value = "ManageReportController", tags = "管理系统-报表")
@RequestMapping("/manager/report")
public class ManageReportController extends BaseController
{
    private final IQuoteOpportunitiesService quoteOpportunitiesService;
    private final IQuoteOpportunitiesDetailService quoteOpportunitiesDetailService;
    private final IQuoteOpportunitiesRoughService quoteOpportunitiesRoughService;
    private final IQuoteOpportunitiesUnableService quoteOpportunitiesUnableService;
    private final IQuotePresaleInfoService quotePresaleInfoService;
    private final IQuoteSignInfoService quoteSignInfoService;

    /**
     * 待成本报价列表
     */
    @ApiOperation(value = "待成本报价列表", notes = "待成本报价列表")
    @PreAuthorize("@ss.hasPermi('manage:await:page')")
    @PostMapping("/pageWithCost")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithCost(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        //筛选当前售前
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if(ObjectUtil.isEmpty(currentUser)){
            throw new RuntimeException("无效的用户");
        }
        // 处理权限相关信息
        quoteOpportunitiesService.fillAuthInfo(currentUser,params);
        return pageInfo(params);
    }

    /**
     * 待销售报价列表
     */
    @ApiOperation(value = "待销售报价列表", notes = "待销售报价列表")
    @PreAuthorize("@ss.hasPermi('manage:await:sale:page')")
    @PostMapping("/pageWithSales")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithSales(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfoAuth(params);
    }

    /**
     * 待报价审批列表
     */
    @ApiOperation(value = "待报价审批列表", notes = "待报价审批列表")
    @PreAuthorize("@ss.hasPermi('manage:approval:page')")
    @PostMapping("/pageWithApproval")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithApproval(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfoAuth(params);
    }

    /**
     * 待签约审批列表
     */
    @ApiOperation(value = "待签约审批列表", notes = "待签约审批列表")
    @PreAuthorize("@ss.hasPermi('manage:contract:approval:page')")
    @PostMapping("/pageWithSign")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithSign(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfoAuth(params);
    }

    /**
     * 待签合同列表
     */
    @ApiOperation(value = "待签合同列表", notes = "待签合同列表")
    @PreAuthorize("@ss.hasPermi('manage:contract:pending:page')")
    @PostMapping("/pageWithContract")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithContract(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfoAuth(params);
    }

    /**
     * 已签约
     */
    @ApiOperation(value = "已签约", notes = "已签约")
    @PreAuthorize("@ss.hasPermi('manage:signed:page')")
    @PostMapping("/listWithSigned")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithSigned(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfoAuth(params);
    }

    /**
     * 丢单(全部)
     */
    @ApiOperation(value = "丢单(全部)", notes = "丢单(全部)")
    @PreAuthorize("@ss.hasPermi('manage:lose:page')")
    @PostMapping("/listWithLose")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithLose(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfoAuth(params);
    }

    /**
     * 丢单(销售审批未通过)
     */
    @ApiOperation(value = "丢单(销售审批未通过)", notes = "丢单(销售审批未通过)")
    @PreAuthorize("@ss.hasPermi('manage:lose:sale:page')")
    @PostMapping("/listWithLoseSale")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithLoseSale(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        params.getModel().setSaleAuditLog("拒绝");
        return pageInfoAuth(params);
    }

    /**
     * 丢单(签约审批未通过)
     */
    @ApiOperation(value = "丢单(签约审批未通过)", notes = "丢单(签约审批未通过)")
    @PreAuthorize("@ss.hasPermi('manage:lose:sign:page')")
    @PostMapping("/listWithLoseSign")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithLoseSign(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        params.getModel().setSignAuditLog("拒绝");
        return pageInfoAuth(params);
    }


    /**
     * 组装分页信息
     *
     * @param params params
     * @return TableDataInfo<List < QuoteOpportunitiesVO>>
     */
    private TableDataInfo<List<QuoteOpportunitiesVO>> pageInfo(PageParams<QuoteOpportunitiesQuery> params) {
        startPage(params);
        List<QuoteOpportunitiesVO> list = quoteOpportunitiesService.getQuoteOpportunitiesList(params.getModel(),true);
        TableDataInfo<List<QuoteOpportunitiesVO>> tableDataInfo = getDataTable(list);
        return tableDataInfo;
    }

    /**
     * 组装分页信息
     *
     * @param params params
     * @return TableDataInfo<List < QuoteOpportunitiesVO>>
     */
    private TableDataInfo<List<QuoteOpportunitiesVO>> pageInfoAuth(PageParams<QuoteOpportunitiesQuery> params) {
        params.getModel().setAuth(Boolean.TRUE);
        return pageInfo(params);
    }

    /**
     * 获取所有成本报价版本
     */
    @ApiOperation(value = "获取所有成本报价版本", notes = "获取所有成本报价版本")
    @PostMapping("/version")
    public R<List<OpportunitiesDetailVersionVO>> getVersionList(@RequestBody QuoteOpportunitiesRoughQuery quoteOpportunitiesRoughQuery) {
        List<OpportunitiesDetailVersionVO> resultList = quoteOpportunitiesService.getVersionList(quoteOpportunitiesRoughQuery);
        return R.ok(resultList);
    }

    /**
     * 获取所有销售报价版本
     */
    @ApiOperation(value = "获取所有销售报价版本", notes = "获取所有销售报价版本")
    @PostMapping("/salesVersion")
    public R<List<OpportunitiesSalesVersionVO>> getSalesVersionList(@RequestBody QuoteOpportunitiesRoughQuery quoteOpportunitiesRoughQuery) {
        List<OpportunitiesSalesVersionVO> resultList = quoteOpportunitiesService.getSalesVersionList(quoteOpportunitiesRoughQuery);
        return R.ok(resultList);
    }

    /**
     * 获取所有签约申请版本
     */
    @ApiOperation(value = "获取所有签约申请版本", notes = "获取所有签约申请版本")
    @PostMapping("/signVersion")
    public R<List<OpportunitiesSignVersionVO>> getSignVersionList(@RequestBody QuoteOpportunitiesRoughQuery quoteOpportunitiesRoughQuery) {
        List<OpportunitiesSignVersionVO> resultList = quoteOpportunitiesService.getSignVersionList(quoteOpportunitiesRoughQuery);
        return R.ok(resultList);
    }

    /**
     * 详细报价详情
     */
    @ApiOperation(value = "详细报价详情", notes = "详细报价详情")
    @PostMapping(value = "/detailInfo")
    public R<QuoteOpportunitiesDetailVO> getDetailInfo(@RequestBody QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery)
    {
        return R.ok(quoteOpportunitiesDetailService.getDetailInfo(quoteOpportunitiesDetailQuery));
    }

    /**
     * 粗略报价详情
     */
    @ApiOperation(value = "粗略报价详情", notes = "粗略报价详情")
    /*@PreAuthorize("@ss.hasPermi('quote:rough:query')")*/
    @PostMapping(value = "/roughInfo")
    public R<QuoteOpportunitiesRoughVO> getRoughInfo(@RequestBody QuoteOpportunitiesDetailQuery quoteOpportunitiesDetailQuery)
    {
        return  R.ok(quoteOpportunitiesRoughService.getRoughInfo(quoteOpportunitiesDetailQuery));
    }

    /**
     * 无法报价详情
     */
    @ApiOperation(value = "无法报价详情", notes = "无法报价详情")
    /*@PreAuthorize("@ss.hasPermi('quote:unable:query')")*/
    @GetMapping(value = "/unable/{id}")
    public R<QuoteOpportunitiesUnableVO> getInfo(@PathVariable("id") Long id)
    {
        return  R.ok(quoteOpportunitiesUnableService.selectQuoteOpportunitiesUnableById(id));
    }

    /**
     * 销售报价详情
     */
    @ApiOperation(value = "销售报价详情", notes = "销售报价详情")
    @PostMapping("/presaleInfo")
    public R<QuotePresaleInfoVO> getPresaleInfo(@RequestBody QuotePresaleInfoQuery query)
    {
        QuotePresaleInfoVO result = quotePresaleInfoService.getDetailInfoById(query);
        return R.ok(result);
    }

    /**
     * 签约详情页(需要id)
     */
    @ApiOperation(value = "签约详情页(需要id)", notes = "签约详情页(需要id)")
    @PostMapping("/signInfo")
    public R<SignDetailInfoVO> getSignInfoById(@RequestBody BaseDTO dto)
    {
        SignDetailInfoVO result = quoteSignInfoService.getSignInfoById(dto);
        return R.ok(result);
    }
}
