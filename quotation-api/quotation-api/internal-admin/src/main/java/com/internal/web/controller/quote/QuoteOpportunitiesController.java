package com.internal.web.controller.quote;

import cn.hutool.core.util.ObjectUtil;
import com.internal.common.annotation.Log;
import com.internal.common.core.controller.BaseController;
import com.internal.common.core.domain.R;
import com.internal.common.core.domain.entity.SysRole;
import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.core.page.TableDataInfo;
import com.internal.common.enums.BusinessType;
import com.internal.common.enums.RoleKeyType;
import com.internal.common.request.PageParams;
import com.internal.common.utils.DateUtils;
import com.internal.common.utils.SecurityUtils;
import com.internal.common.utils.poi.EasyExcelUtil;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.quartz.task.CrmSynOpportunitiesStatusTask;
import com.internal.quartz.task.CrmSynOpportunitiesTask;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.dto.*;
import com.internal.quote.service.IQuoteOpportunitiesFileService;
import com.internal.quote.service.IQuoteOpportunitiesService;
import com.internal.quote.vo.*;
import com.internal.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 报价系统-主表-商机报价信息Controller
 *
 * @author internal
 * @date 2024-10-14
 */
@RestController
@AllArgsConstructor
@Api(value = "QuoteOpportunitiesController", tags = "报价系统-主表-商机报价信息表")
@RequestMapping("/quote/opportunities")
public class QuoteOpportunitiesController extends BaseController {

    private final IQuoteOpportunitiesService quoteOpportunitiesService;
    private final IQuoteOpportunitiesFileService fileService;
    private final CrmSynOpportunitiesTask crmSynOpportunitiesTask;
    private final CrmSynOpportunitiesStatusTask cmSynOpportunitiesStatusTask;
    private final ISysUserService iSysUserService;


    /**
     * 商机报价信息列表
     */
    @ApiOperation(value = "商机报价信息列表", notes = "商机报价信息列表")
    @PreAuthorize("@ss.hasPermi('quote:opportunities:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuoteOpportunitiesVO>> list(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfo(params);
    }

    /**
     * 待成本报价列表
     */
    @ApiOperation(value = "待成本报价列表", notes = "待成本报价列表")
    @PreAuthorize("@ss.hasPermi('quote:await:page')")
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
     * 待更新成本报价列表
     */
    @ApiOperation(value = "待更新成本报价列表", notes = "待更新成本报价列表")
    @PreAuthorize("@ss.hasPermi('quote:await:update:page')")
    @PostMapping("/pageAwaitUpdate")
    public TableDataInfo<List<QuoteOpportunitiesVO>> pageAwaitUpdate(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        //筛选当前售前
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if(ObjectUtil.isEmpty(currentUser)){
            throw new RuntimeException("未能获取到当前用户信息，请重新登陆!");
        }
        // 处理权限相关信息
        quoteOpportunitiesService.fillAuthInfo(currentUser,params);
        return pageInfo(params);
    }

    /**
     * 已成本报价列表
     */
    @ApiOperation(value = "已成本报价列表", notes = "已成本报价列表")
    @PreAuthorize("@ss.hasPermi('quote:finish:page')")
    @PostMapping("/pageWithCosted")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithCosted(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        //筛选当前售前
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if(ObjectUtil.isEmpty(currentUser)){
            throw new RuntimeException("未能获取到当前用户信息，请重新登陆!");
        }
        // 处理权限相关信息
        quoteOpportunitiesService.fillAuthInfo(currentUser,params);
        return pageInfo(params);
    }

    /**
     * 无法报价报价列表
     */
    @ApiOperation(value = "无法报价报价列表", notes = "无法报价报价列表")
    @PreAuthorize("@ss.hasPermi('quote:cannot:page')")
    @PostMapping("/pageWithIncapable")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithIncapable(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
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
    @PreAuthorize("@ss.hasPermi('quote:await:sale:page')")
    @PostMapping("/pageWithSales")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithSales(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfoAuth(params);
    }

    /**
     * 待报价审批列表
     */
    @ApiOperation(value = "待报价审批列表", notes = "待报价审批列表")
    @PreAuthorize("@ss.hasPermi('quote:approval:page')")
    @PostMapping("/pageWithApproval")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithApproval(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfoAuth(params);
    }

    /**
     * 报价审批未通过列表
     */
    @ApiOperation(value = "报价审批未通过列表", notes = "报价审批未通过列表")
    @PreAuthorize("@ss.hasPermi('quote:rejected:page')")
    @PostMapping("/pageWithApprovalFail")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithApprovalFail(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfoAuth(params);
    }

    /**
     * 报价单列表
     */
    @ApiOperation(value = "报价单列表", notes = "报价单列表")
    @PreAuthorize("@ss.hasPermi('quote:order:page')")
    @PostMapping("/pageWithQuote")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithQuote(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfoAuth(params);
    }

    /**
     * 待签约审批列表
     */
    @ApiOperation(value = "待签约审批列表", notes = "待签约审批列表")
    @PreAuthorize("@ss.hasPermi('quote:contract:approval:page')")
    @PostMapping("/pageWithSign")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithSign(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfoAuth(params);
    }

    /**
     * 签约审批未通过列表
     */
    @ApiOperation(value = "签约审批未通过列表", notes = "签约审批未通过列表")
    @PreAuthorize("@ss.hasPermi('quote:contract:rejected:page')")
    @PostMapping("/pageWithSignFail")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithSignFail(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
        return pageInfoAuth(params);
    }

    /**
     * 待签合同列表
     */
    @ApiOperation(value = "待签合同列表", notes = "待签合同列表")
    @PreAuthorize("@ss.hasPermi('quote:contract:pending:page')")
    @PostMapping("/pageWithContract")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithContract(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
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
     * 汇总相关列表
     */
    @ApiOperation(value = "汇总相关列表", notes = "汇总相关列表")
    @PreAuthorize("@ss.hasPermi('quote:summary:deal','quote:summary:lose','quote:summary:pause','quote:summary:discontinue')")
    @PostMapping("/pageWithSummary")
    public TableDataInfo<List<QuoteOpportunitiesVO>> listWithSummary(@RequestBody PageParams<QuoteOpportunitiesQuery> params) {
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
     * 导出报价系统-商机报价信息列表
     */
    @ApiOperation(value = "导出报价系统-商机报价信息列表", notes = "导出报价系统-商机报价信息列表")
    @PreAuthorize("@ss.hasPermi('quote:opportunities:export')")
    @Log(title = "报价系统-商机报价信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuoteOpportunitiesQuery quoteOpportunities) {
        List<QuoteOpportunitiesVO> list = quoteOpportunitiesService.selectQuoteOpportunitiesList(quoteOpportunities);
        ExcelUtil<QuoteOpportunitiesVO> util = new ExcelUtil<QuoteOpportunitiesVO>(QuoteOpportunitiesVO.class);
        util.exportExcel(response, list, "报价系统-商机报价信息数据");
    }

    /**
     * 获取报价系统-商机报价信息详细信息
     */
    @ApiOperation(value = "获取报价系统-商机报价信息详细信息", notes = "获取报价系统-商机报价信息详细信息")
    @PreAuthorize("@ss.hasPermi('quote:opportunities:query')")
    @GetMapping(value = "/{id}")
    public R<QuoteOpportunities> getInfo(@PathVariable("id") Long id) {
        return R.ok(quoteOpportunitiesService.selectQuoteOpportunitiesById(id));
    }

    /**
     * 新增报价系统-商机报价信息
     */
    @ApiOperation(value = "新增报价系统-商机报价信息", notes = "新增报价系统-商机报价信息")
    @PreAuthorize("@ss.hasPermi('quote:opportunities:add')")
    @Log(title = "报价系统-商机报价信息", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody QuoteOpportunities quoteOpportunities) {
        return R.ok(quoteOpportunitiesService.insertQuoteOpportunities(quoteOpportunities));
    }

    /**
     * 修改报价系统-商机报价信息
     */
    @ApiOperation(value = "修改报价系统-商机报价信息", notes = "修改报价系统-商机报价信息")
    @PreAuthorize("@ss.hasPermi('quote:opportunities:edit')")
    @Log(title = "报价系统-商机报价信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuoteOpportunities quoteOpportunities) {
        return R.ok(quoteOpportunitiesService.updateQuoteOpportunities(quoteOpportunities));
    }

    /**
     * 删除报价系统-商机报价信息
     */
    @ApiOperation(value = "删除报价系统-商机报价信息", notes = "删除报价系统-商机报价信息")
    @PreAuthorize("@ss.hasPermi('quote:opportunities:remove')")
    @Log(title = "报价系统-商机报价信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids) {
        return R.ok(quoteOpportunitiesService.deleteQuoteOpportunitiesByIds(ids));
    }

    /**
     * 无法报价
     */
    @ApiOperation(value = "无法报价", notes = "无法报价")
    @PreAuthorize("@ss.hasPermi('quote:await:unable:quote','quote:finish:update','quote:cannot:update')")
    @Log(title = "报价系统-商机报价信息", businessType = BusinessType.UPDATE)
    @PutMapping("/incapable")
    public R<Boolean> incapable(@RequestBody QuoteOpportunitiesUpdateDTO quoteOpportunitiesUpdateDTO) {
        return R.ok(quoteOpportunitiesService.incapable(quoteOpportunitiesUpdateDTO));
    }

    /**
     * 获取所有版本号
     */
    @ApiOperation(value = "获取所有版本号", notes = "获取所有版本号-需要opportunitiesId")
    @PostMapping("/version")
    public R<List<OpportunitiesDetailVersionVO>> getVersionList(@RequestBody QuoteOpportunitiesRoughQuery quoteOpportunitiesRoughQuery) {
        quoteOpportunitiesRoughQuery.setIsolation(true);
        List<OpportunitiesDetailVersionVO> resultList = quoteOpportunitiesService.getVersionList(quoteOpportunitiesRoughQuery);
        //DecimalUtil.setListScale(resultList,DecimalUtil.defaultScale);
        return R.ok(resultList);
    }

    /**
     * 获取所有销售报价版本号
     */
    @ApiOperation(value = "获取所有销售报价版本号", notes = "获取所有销售报价版本号")
    @PostMapping("/salesVersion")
    public R<List<OpportunitiesSalesVersionVO>> getSalesVersionList(@RequestBody QuoteOpportunitiesRoughQuery quoteOpportunitiesRoughQuery) {
        quoteOpportunitiesRoughQuery.setIsolation(true);
        List<OpportunitiesSalesVersionVO> resultList = quoteOpportunitiesService.getSalesVersionList(quoteOpportunitiesRoughQuery);
        //DecimalUtil.setListScale(resultList,DecimalUtil.defaultScale);
        return R.ok(resultList);
    }

    /**
     * 获取所有签约申请版本
     */
    @ApiOperation(value = "获取所有签约申请版本", notes = "获取所有签约申请版本")
    @PostMapping("/signVersion")
    public R<List<OpportunitiesSignVersionVO>> getSignVersionList(@RequestBody QuoteOpportunitiesRoughQuery quoteOpportunitiesRoughQuery) {
        quoteOpportunitiesRoughQuery.setIsolation(true);
        List<OpportunitiesSignVersionVO> resultList = quoteOpportunitiesService.getSignVersionList(quoteOpportunitiesRoughQuery);
        //DecimalUtil.setListScale(resultList,DecimalUtil.defaultScale);
        return R.ok(resultList);
    }

    /**
     * 重新申请报价
     */
    @ApiOperation(value = "重新申请报价", notes = "重新申请报价")
    @PreAuthorize("@ss.hasPermi('quote:sale:reapply','quote:rejected:reapply','quote:rejected:update'" +
            ",'quote:order:reapply','quote:order:update','quote:contract:rejected:replay')")
    @Log(title = "重新申请报价", businessType = BusinessType.UPDATE)
    @PutMapping("/apply")
    public R<Boolean> quoteApply(@RequestBody ReQuoteDTO dto) {
        return R.ok(quoteOpportunitiesService.quoteApply(dto));
    }

    /**
     * 需要更新成本报价
     */
    @ApiOperation(value = "需要更新成本报价", notes = "需要更新成本报价")
    @Log(title = "需要更新成本报价", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('quote:order:contractRequest')")
    @PutMapping("/updateRequired")
    public R<Boolean> updateRequired(@RequestBody ReQuoteDTO dto) {
        return R.ok(quoteOpportunitiesService.updateRequired(dto));
    }

    /**
     * 丢单处理
     */
    @ApiOperation(value = "丢单处理", notes = "丢单处理")
    @PreAuthorize("@ss.hasPermi('quote:sale:handleLost','quote:rejected:handleLost','quote:rejected:update'" +
            ",'quote:order:handleLost','quote:order:contractRequest','quote:order:update','quote:contract:approval:handleLost','quote:contract:rejected:handleLost')")
    @Log(title = "商机丢单处理", businessType = BusinessType.UPDATE)
    @PostMapping("/lose")
    public R<Boolean> lose(@RequestBody QuoteOpportunitiesUpdateDTO dto) {
        Boolean result = false;
        result = quoteOpportunitiesService.lose(dto);

        return R.ok(result);
    }

    /**
     * 丢单审批-通过
     */
    @ApiOperation(value = "丢单审批-通过", notes = "丢单审批-通过")
    @Log(title = "丢单审批-通过", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('quote:lose:approval:approve')")
    @PostMapping("/loseApproval")
    public R<Boolean> loseApproval(@RequestBody ApprovalDTO dto) {
        return R.ok(quoteOpportunitiesService.loseApproval(dto));
    }

    /**
     * 丢单审批-驳回
     */
    @ApiOperation(value = "丢单审批-驳回", notes = "丢单审批-驳回")
    @Log(title = "丢单审批-驳回", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('quote:lose:approval:approve')")
    @PostMapping("/loseReject")
    public R<Boolean> loseReject(@RequestBody ApprovalDTO dto) {
        return R.ok(quoteOpportunitiesService.loseReject(dto));
    }

    /**
     * 报价审批-通过
     */
    @ApiOperation(value = "报价审批-通过", notes = "报价审批-通过")
    @Log(title = "报价审批-通过", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('quote:approval:approve')")
    @PostMapping("/salesApproval")
    public R<Boolean> salesApproval(@RequestBody ApprovalDTO dto) {
        return R.ok(quoteOpportunitiesService.salesApproval(dto));
    }

    /**
     * 报价审批-驳回
     */
    @ApiOperation(value = "报价审批-驳回", notes = "报价审批-驳回")
    @Log(title = "报价审批-驳回", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('quote:approval:approve')")
    @PostMapping("/salesReject")
    public R<Boolean> salesReject(@RequestBody ApprovalDTO dto) {
        return R.ok(quoteOpportunitiesService.salesReject(dto));
    }

    /**
     * 签约审批-通过
     */
    @ApiOperation(value = "签约审批-通过", notes = "签约审批-通过")
    @Log(title = "签约审批-通过", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('quote:contract:approval:approve')")
    @PostMapping("/signApproval")
    public R<Boolean> signApproval(@RequestBody ApprovalDTO dto) {
        return R.ok(quoteOpportunitiesService.signApproval(dto));
    }

    /**
     * 签约审批-驳回
     */
    @ApiOperation(value = "签约审批-驳回", notes = "签约审批-驳回")
    @Log(title = "签约审批-驳回", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('quote:contract:approval:approve')")
    @PostMapping("/signReject")
    public R<Boolean> signReject(@RequestBody ApprovalDTO dto) {
        return R.ok(quoteOpportunitiesService.signReject(dto));
    }


    /**
     * 商机同步
     */
    @ApiOperation(value = "商机同步", notes = "商机同步")
    @PreAuthorize("@ss.hasPermi('quote:await:sync')")
    @GetMapping("/syncOpportunities")
    public R<Boolean> syncOpportunities() throws MessagingException, GeneralSecurityException {
        crmSynOpportunitiesTask.sync();
        cmSynOpportunitiesStatusTask.sync();
        return R.ok(Boolean.TRUE);
    }

    /**
     * 商机状态同步
     */
    @ApiOperation(value = "商机状态同步", notes = "商机状态同步")
    @GetMapping("/syncOpportunitiesStatus")
    public R<Boolean> syncOpportunitiesStatus() throws MessagingException, GeneralSecurityException {
        cmSynOpportunitiesStatusTask.sync();
        return R.ok(Boolean.TRUE);
    }

    /**
     * 待成本报价-由自己报价
     */
    @ApiOperation(value = "待成本报价-由自己报价", notes = "待成本报价-由自己报价-需要opportunitiesId")
    @PostMapping("/quoteBySelf")
    public R<Boolean> quoteBySelf(@RequestBody QuoteOpportunitiesUpdateDTO dto) {
        return R.ok(quoteOpportunitiesService.quoteBySelf(dto));
    }

    /**
     * 待成本报价-由其他人报价
     */
    @ApiOperation(value = "待成本报价-由其他人报价", notes = "待成本报价-由其他人报价-需要opportunitiesId")
    @PostMapping("/quoteByOthers")
    public R<Boolean> quoteByOthers(@RequestBody QuoteOpportunitiesUpdateDTO dto) {
        return R.ok(quoteOpportunitiesService.quoteByOthers(dto));
    }

    /**
     * 报价单导出
     */
    @ApiOperation(value = "报价单导出", notes = "报价单导出")
    @Log(title = "报价单导出", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('quote:order:download')")
    @PostMapping("/quotation/export")
    public void export(HttpServletResponse response, @RequestBody QuotationExportDTO quotationExportDTO) throws Exception {
        EasyExcelUtil<QuotationExportDTO> util = new EasyExcelUtil<>(QuotationExportDTO.class);
        String templatePath = quotationExportDTO.getIsBeiGuangQuote() ? "template/BeiGuang_template.xlsx" : "template/XinXiang_template.xlsx";
        EasyExcelUtil.initResponse(response, "报价单");
        quotationExportDTO.setDate(DateUtils.parseDateToStr("yyyy-MM-dd", new Date()));
        // 获取手机号
        quotationExportDTO.setSalesPhone(quoteOpportunitiesService.selectQuoteSalePhone(quotationExportDTO.getOpportunitiesId()));
        util.exportEasyExcel(response, "sheet1", templatePath, quotationExportDTO);

    }

    /**
     * 主页信息(不配权限，因为权限在查询内部)
     */
    @ApiOperation(value = "主页信息", notes = "主页信息")
    @PostMapping("/indexPageInfo")
    public QuoteIndexVO getIndexPageInfo() {
        QuoteIndexVO result = quoteOpportunitiesService.getIndexPageInfo();
        return result;
    }


}
