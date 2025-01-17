package com.internal.web.controller.quote;

import java.util.List;
import com.internal.common.core.domain.R;
import jakarta.servlet.http.HttpServletResponse;
import com.internal.common.request.PageParams;
import com.internal.quote.dto.BaseDTO;
import com.internal.quote.dto.SignApplicationSaveDTO;
import com.internal.quote.dto.SignInfoUpdateDTO;
import com.internal.quote.vo.QuoteSignInfoVO;
import com.internal.quote.vo.SignDetailInfoVO;
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
import com.internal.quote.domain.QuoteSignInfo;
import com.internal.quote.service.IQuoteSignInfoService;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.common.core.page.TableDataInfo;

/**
 * 报价系统-待签合同Controller
 * 
 * @author internal
 * @date 2024-11-14
 */
@RestController
@AllArgsConstructor
@Api(value = "QuoteSignInfoController", tags = "报价系统-待签合同表")
@RequestMapping("/quote/sign/info")
public class QuoteSignInfoController extends BaseController
{

    private final IQuoteSignInfoService quoteSignInfoService;

    /**
     * 查询报价系统-待签合同列表
     */
    @ApiOperation(value = "查询报价系统-待签合同列表", notes = "查询报价系统-待签合同列表")
    @PreAuthorize("@ss.hasPermi('quote:info:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuoteSignInfo>> list(@RequestBody PageParams<QuoteSignInfo> params)
    {
        startPage(params);
        List<QuoteSignInfo> list = quoteSignInfoService.selectQuoteSignInfoList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出报价系统-待签合同列表
     */
    @ApiOperation(value = "导出报价系统-待签合同列表", notes = "导出报价系统-待签合同列表")
    @PreAuthorize("@ss.hasPermi('quote:info:export')")
    @Log(title = "报价系统-待签合同", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuoteSignInfo quoteSignInfo)
    {
        List<QuoteSignInfo> list = quoteSignInfoService.selectQuoteSignInfoList(quoteSignInfo);
        ExcelUtil<QuoteSignInfo> util = new ExcelUtil<QuoteSignInfo>(QuoteSignInfo.class);
        util.exportExcel(response, list, "报价系统-待签合同数据");
    }

    /**
     * 获取报价系统-待签合同详细信息
     */
    @ApiOperation(value = "获取报价系统-待签合同详细信息", notes = "获取报价系统-待签合同详细信息")
    @PreAuthorize("@ss.hasPermi('quote:info:query')")
    @GetMapping(value = "/{id}")
    public R<QuoteSignInfo> getInfo(@PathVariable("id") Long id)
    {
        return  R.ok(quoteSignInfoService.selectQuoteSignInfoById(id));
    }

    /**
     * 新增报价系统-待签合同
     */
    @ApiOperation(value = "新增报价系统-待签合同", notes = "新增报价系统-待签合同")
    @PreAuthorize("@ss.hasPermi('quote:info:add')")
    @Log(title = "报价系统-待签合同", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> add(@RequestBody QuoteSignInfo quoteSignInfo)
    {
        return R.ok(quoteSignInfoService.insertQuoteSignInfo(quoteSignInfo));
    }

    /**
     * 修改报价系统-待签合同
     */
    @ApiOperation(value = "修改报价系统-待签合同", notes = "修改报价系统-待签合同")
    @PreAuthorize("@ss.hasPermi('quote:info:edit')")
    @Log(title = "报价系统-待签合同", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@RequestBody QuoteSignInfo quoteSignInfo)
    {
        return R.ok(quoteSignInfoService.updateQuoteSignInfo(quoteSignInfo));
    }

    /**
     * 删除报价系统-待签合同
     */
    @ApiOperation(value = "删除报价系统-待签合同", notes = "删除报价系统-待签合同")
    @PreAuthorize("@ss.hasPermi('quote:info:remove')")
    @Log(title = "报价系统-待签合同", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids)
    {
        return R.ok(quoteSignInfoService.deleteQuoteSignInfoByIds(ids));
    }
    /**
     * 签约申请
     */
    @ApiOperation(value = "签约申请", notes = "签约申请")
    @Log(title = "签约申请", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('quote:order:contractRequest','quote:contract:approval:update','quote:contract:rejected:update')")
    @PostMapping("/signApplication")
    public R<Boolean> signApplication(@RequestBody SignApplicationSaveDTO vo)
    {
        return R.ok(quoteSignInfoService.signApplication(vo));
    }

    /**
     * 签约详情页(需要id)
     */
    @ApiOperation(value = "签约详情页(需要id)", notes = "签约详情页(需要id)")
    @PostMapping("/signDetailInfo")
    public R<SignDetailInfoVO> getSignInfoById(@RequestBody BaseDTO dto)
    {
        SignDetailInfoVO result = quoteSignInfoService.getSignInfoById(dto);
        return R.ok(result);
    }
    /**
     * 签约审批页(需要opportunitiesId)
     */
    @ApiOperation(value = "签约审批页(需要opportunitiesId)", notes = "签约审批页(需要opportunitiesId)")
    @PostMapping("/signApprovalPage")
    public R<SignDetailInfoVO> getSignInfoByOpportunitiesId(@RequestBody BaseDTO dto)
    {
        SignDetailInfoVO result = quoteSignInfoService.getSignInfoByOpportunitiesId(dto);
        return R.ok(result);
    }
    /**
     * 重新签约审批页(需要opportunitiesId)
     */
    @ApiOperation(value = "重新签约审批页(需要opportunitiesId)", notes = "重新签约审批页(需要opportunitiesId)")
    @PostMapping("/reSignInfo")
    public R<SignDetailInfoVO> getReSignInfo(@RequestBody BaseDTO dto)
    {
        SignDetailInfoVO result = quoteSignInfoService.getReSignInfo(dto);
        return R.ok(result);
    }

    /**
     * 签约状态更新
     */
    @ApiOperation(value = "签约状态更新", notes = "签约状态更新")
    @PostMapping("/updateStatus")
    public R<Boolean> updateStatus(@RequestBody SignInfoUpdateDTO dto)
    {
        Boolean result = quoteSignInfoService.updateStatus(dto);
        return R.ok(result);
    }

    /**
     * 签约状态更新
     */
    @ApiOperation(value = "签约状态更新页(需要opportunitiesId)", notes = "签约状态更新页(需要opportunitiesId)")
    @PostMapping("/statusUpdatePage")
    public R<QuoteSignInfoVO> getStatusUpdatePageInfo(@RequestBody BaseDTO dto)
    {
        QuoteSignInfoVO result = quoteSignInfoService.getStatusUpdatePageInfo(dto);
        return R.ok(result);
    }
}
