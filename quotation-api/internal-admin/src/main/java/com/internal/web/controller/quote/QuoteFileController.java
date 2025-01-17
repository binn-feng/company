package com.internal.web.controller.quote;

import com.internal.common.annotation.Log;
import com.internal.common.core.controller.BaseController;
import com.internal.common.enums.BusinessType;
import com.internal.common.exception.CustomizedException;
import com.internal.common.utils.ExceptionUtil;
import com.internal.common.utils.file.FileUtils;
import com.internal.common.utils.poi.EasyExcelUtil;
import com.internal.quote.service.IQuoteOpportunitiesService;
import com.internal.quote.vo.QuoteOpportunitiesCustomizableImportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

/**
 * 报价系统-文件相关Controller
 * 
 * @author internal
 * @date 2025-01-07
 */
@RestController
@AllArgsConstructor
@Api(value = "QuoteFileController", tags = "报价系统-文件相关")
@RequestMapping("/quote/file")
public class QuoteFileController extends BaseController
{
    private final IQuoteOpportunitiesService quoteOpportunitiesService;

    /**
     * 下载功能清单模版
     */
    @ApiOperation(value = "下载功能清单模版", notes = "下载功能清单模版")
    @Log(title = "下载功能清单模版", businessType = BusinessType.EXPORT)
    @PostMapping("/customizableTemplate")
    public void downloadCustomizableTemplate(HttpServletResponse response) throws Exception {
        EasyExcelUtil<Object> util = new EasyExcelUtil<>(Object.class);
        String templatePath = "template/Cost_Customizable_template.xlsx";
        EasyExcelUtil.initResponse(response, "功能清单模版");

        util.exportEasyExcel(response, "sheet1", templatePath, null);
    }

    /**
     * 上传功能清单
     */
    @ApiOperation(value = "上传功能清单", notes = "上传功能清单")
    @Log(title = "上传功能清单")
    @PostMapping("/uploadCustomizable")
    public List<QuoteOpportunitiesCustomizableImportVO> uploadCustomizableExcel(@RequestParam("file") MultipartFile file) throws Exception
    {
        String ext = FileUtils.getFileExtension(file.getOriginalFilename());
        if(!"xlsx".equals(ext)){
            throw new CustomizedException("暂不支持改格式，请下载模版后填写重试");
        }
        EasyExcelUtil<QuoteOpportunitiesCustomizableImportVO> util = new EasyExcelUtil<>(QuoteOpportunitiesCustomizableImportVO.class);
        List<QuoteOpportunitiesCustomizableImportVO> result = new LinkedList<>();
        try {
            Boolean v = util.verifyProperties(file.getInputStream());
            if(!v){
                throw new CustomizedException("暂不支持改格式，请下载模版后填写重试");
            }
            result = util.importEasyExcel(file.getInputStream());
        } catch (Exception e) {
            ExceptionUtil.customizedThrow(e,"上传失败");
        }
        return result;
    }


}
