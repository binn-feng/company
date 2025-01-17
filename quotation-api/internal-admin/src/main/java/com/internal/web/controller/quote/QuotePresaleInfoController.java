package com.internal.web.controller.quote;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import com.internal.common.core.domain.R;

import javax.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

import com.internal.common.core.domain.entity.SysUser;
import com.internal.common.enums.ContractType;
import com.internal.common.request.PageParams;
import com.internal.common.utils.DecimalUtil;
import com.internal.quote.dto.BaseDTO;
import com.internal.quote.dto.QuotePresaleInfoQuery;
import com.internal.quote.dto.QuotePresaleInfoSaveDTO;
import com.internal.common.email.SystemConfig;
import com.internal.common.utils.MailUtil;
import com.internal.quote.vo.ApprovalAndSignInfoVO;
import com.internal.quote.vo.QuotePreSaleDetailInfoVO;
import com.internal.quote.vo.QuotePresaleInfoVO;
import com.internal.quote.vo.SignDetailInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.internal.common.annotation.Log;
import com.internal.common.core.controller.BaseController;
import com.internal.common.enums.BusinessType;
import com.internal.quote.domain.QuotePresaleInfo;
import com.internal.quote.service.IQuotePresaleInfoService;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.common.core.page.TableDataInfo;

/**
 * 销售报价信息Controller
 * 
 * @author internal
 * @date 2024-10-30
 */
@RestController
@AllArgsConstructor
@Api(value = "QuotePresaleInfoController", tags = "销售报价信息")
@RequestMapping("/quote/info")
public class QuotePresaleInfoController extends BaseController
{

    private final IQuotePresaleInfoService quotePresaleInfoService;
    private final SystemConfig systemConfig;
    /**
     * 查询报价系统-商机售前报价信息列表
     */
    @ApiOperation(value = "查询报价系统-商机售前报价信息列表", notes = "查询报价系统-商机售前报价信息列表")
    @PreAuthorize("@ss.hasPermi('quote:info:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuotePresaleInfo>> list(@RequestBody PageParams<QuotePresaleInfo> params)
    {
        startPage(params);
        List<QuotePresaleInfo> list = quotePresaleInfoService.selectQuotePresaleInfoList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出报价系统-商机售前报价信息列表
     */
    @ApiOperation(value = "导出报价系统-商机售前报价信息列表", notes = "导出报价系统-商机售前报价信息列表")
    @PreAuthorize("@ss.hasPermi('quote:info:export')")
    @Log(title = "报价系统-商机售前报价信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuotePresaleInfo quotePresaleInfo)
    {
        List<QuotePresaleInfo> list = quotePresaleInfoService.selectQuotePresaleInfoList(quotePresaleInfo);
        ExcelUtil<QuotePresaleInfo> util = new ExcelUtil<QuotePresaleInfo>(QuotePresaleInfo.class);
        util.exportExcel(response, list, "报价系统-商机售前报价信息数据");
    }

    /**
     * 获取报价系统-商机售前报价信息详细信息
     */
    @ApiOperation(value = "获取报价系统-商机售前报价信息详细信息", notes = "获取报价系统-商机售前报价信息详细信息")
    @PreAuthorize("@ss.hasPermi('quote:info:query')")
    @GetMapping(value = "/{id}")
    public R<QuotePresaleInfo> getInfo(@PathVariable("id") Long id)
    {
        return  R.ok(quotePresaleInfoService.selectQuotePresaleInfoById(id));
    }

    /**
     * 新增报价系统-商机售前报价信息
     */
    @ApiOperation(value = "新增报价系统-商机售前报价信息", notes = "新增报价系统-商机售前报价信息")
    @PreAuthorize("@ss.hasPermi('quote:info:add')")
    @Log(title = "报价系统-商机售前报价信息", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody QuotePresaleInfo quotePresaleInfo)
    {
        return R.ok(quotePresaleInfoService.insertQuotePresaleInfo(quotePresaleInfo));
    }

    /**
     * 修改报价系统-商机售前报价信息
     */
    @ApiOperation(value = "修改报价系统-商机售前报价信息", notes = "修改报价系统-商机售前报价信息")
    @PreAuthorize("@ss.hasPermi('quote:info:edit')")
    @Log(title = "报价系统-商机售前报价信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuotePresaleInfo quotePresaleInfo)
    {
        return R.ok(quotePresaleInfoService.updateQuotePresaleInfo(quotePresaleInfo));
    }

    /**
     * 销售报价-待销售报价-粗略报价详情
     */
    @ApiOperation(value = "销售报价-待销售报价-粗略报价详情", notes = "销售报价-待销售报价-粗略报价详情")
    @PostMapping("/getSalesRoughInfo")
    public R<QuotePreSaleDetailInfoVO> getSalesRoughInfo(@RequestBody QuotePresaleInfoQuery quotePresaleInfoQuery)
    {
        QuotePreSaleDetailInfoVO result = quotePresaleInfoService.getSalesRoughInfo(quotePresaleInfoQuery);
        return R.ok(result);
    }

    /**
     * 销售报价-待销售报价-详细报价详情
     */
    @ApiOperation(value = "销售报价-待销售报价-详细报价详情", notes = "销售报价-待销售报价-详细报价详情")
    @PostMapping("/getSalesDetailInfo")
    public R<QuotePreSaleDetailInfoVO> getSalesDetailInfo(@RequestBody QuotePresaleInfoQuery quotePresaleInfoQuery)
    {
        if(ObjectUtil.isEmpty(quotePresaleInfoQuery.getQuoteType())){
            quotePresaleInfoQuery.setQuoteType(ContractType.XX.getCode());
        }
        QuotePreSaleDetailInfoVO result = quotePresaleInfoService.getSalesDetailInfo(quotePresaleInfoQuery);
        return R.ok(result);
    }

    /**
     * 销售报价-待销售报价-详细报价详情
     */
    @ApiOperation(value = "销售报价-报价详情", notes = "销售报价-报价详情")
    @PostMapping("/getDetailInfoById")
    public R<QuotePresaleInfoVO> getDetailInfoById(@RequestBody QuotePresaleInfoQuery query)
    {
        QuotePresaleInfoVO result = quotePresaleInfoService.getDetailInfoById(query);
        return R.ok(result);
    }

    /**
     * 销售报价-重新销售报价
     */
    @ApiOperation(value = "销售报价-重新销售报价", notes = "销售报价-重新销售报价(需要opportunitiesId)")
    @PostMapping("/reSales")
    public R<QuotePreSaleDetailInfoVO> reSales(@RequestBody QuotePresaleInfoQuery query)
    {
        QuotePreSaleDetailInfoVO result = quotePresaleInfoService.reSales(query);
        return R.ok(result);
    }


    /**
     * 待销售报价-确认销售报价
     */
    @ApiOperation(value = "待销售报价-确认销售报价", notes = "待销售报价-确认销售报价")
    @Log(title = "待销售报价-确认销售报价", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('quote:sale:create','quote:rejected:update','quote:order:update')")
    @PostMapping("/addSalesQuotesVersion")
    public R<Integer> addSalesQuotesVersion(@RequestBody QuotePresaleInfoSaveDTO saveDTO)
    {
        return R.ok(quotePresaleInfoService.addSalesQuotesVersion(saveDTO));
    }

    /**
     * 销售报价-签约申请页
     */
    @ApiOperation(value = "销售报价-签约申请页", notes = "销售报价-签约申请页")
    @PostMapping("/approvalAndSignInfo")
    public R<SignDetailInfoVO> approvalAndSignInfo(@RequestBody BaseDTO dto)
    {
        SignDetailInfoVO result = quotePresaleInfoService.approvalAndSignInfo(dto.getOpportunitiesId(),true);
        return R.ok(result);
    }

    /**
     * 销售报价-待销售报价审批页
     */
    @ApiOperation(value = "销售报价-待销售报价审批页", notes = "销售报价-待销售报价审批页")
    @PostMapping("/approvalInfo")
    public R<SignDetailInfoVO> approvalInfo(@RequestBody BaseDTO dto)
    {
        SignDetailInfoVO result = quotePresaleInfoService.approvalAndSignInfo(dto.getOpportunitiesId(),false);
        return R.ok(result);
    }
    /**
     * 存在成本信息-需要opportunitiesId
     */
    @ApiOperation(value = "存在成本信息-需要opportunitiesId", notes = "存在成本信息-需要opportunitiesId")
    @PostMapping("/existCostInfo")
    public R<Boolean> existCostInfo(@RequestBody BaseDTO dto)
    {
        Boolean result = quotePresaleInfoService.existCostInfo(dto.getOpportunitiesId());
        return R.ok(result);
    }



}
