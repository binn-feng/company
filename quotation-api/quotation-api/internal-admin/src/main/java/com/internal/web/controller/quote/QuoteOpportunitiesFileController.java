package com.internal.web.controller.quote;

import java.io.File;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import com.internal.common.config.InternalConfig;
import com.internal.common.core.domain.R;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.internal.common.request.PageParams;
import com.internal.common.utils.file.FileUploadUtils;
import com.internal.common.utils.file.FileUtils;
import com.internal.common.utils.file.ZipUtils;
import com.internal.framework.config.ServerConfig;
import com.internal.quote.domain.QuoteOpportunities;
import com.internal.quote.service.IQuoteOpportunitiesParentService;
import com.internal.quote.service.IQuoteOpportunitiesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.internal.common.annotation.Log;
import com.internal.common.core.controller.BaseController;
import com.internal.common.enums.BusinessType;
import com.internal.quote.domain.QuoteOpportunitiesFile;
import com.internal.quote.service.IQuoteOpportunitiesFileService;
import com.internal.common.utils.poi.ExcelUtil;
import com.internal.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 商机附件Controller
 * 
 * @author internal
 * @date 2024-10-28
 */
@RestController
@AllArgsConstructor
@Api(value = "QuoteOpportunitiesFileController", tags = "商机附件表")
@RequestMapping("/quote/file")
public class QuoteOpportunitiesFileController extends BaseController
{

    private final IQuoteOpportunitiesFileService quoteOpportunitiesFileService;
    private final IQuoteOpportunitiesService quoteOpportunitiesService;
    private final IQuoteOpportunitiesParentService quoteChildrenService;
    private static final String FILE_DELIMETER = ",";
    @Autowired
    private ServerConfig serverConfig;
    /**
     * 查询商机附件列表
     */
    @ApiOperation(value = "查询商机附件列表", notes = "查询商机附件列表")
    @PreAuthorize("@ss.hasPermi('quote:file:list')")
    @PostMapping("/page")
    public TableDataInfo<List<QuoteOpportunitiesFile>> list(@RequestBody PageParams<QuoteOpportunitiesFile> params)
    {
        startPage(params);
        List<QuoteOpportunitiesFile> list = quoteOpportunitiesFileService.selectQuoteOpportunitiesFileList(params.getModel());
        return getDataTable(list);
    }

    /**
     * 导出商机附件列表
     */
    @ApiOperation(value = "导出商机附件列表", notes = "导出商机附件列表")
    @PreAuthorize("@ss.hasPermi('quote:file:export')")
    @Log(title = "商机附件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuoteOpportunitiesFile quoteOpportunitiesFile)
    {
        List<QuoteOpportunitiesFile> list = quoteOpportunitiesFileService.selectQuoteOpportunitiesFileList(quoteOpportunitiesFile);
        ExcelUtil<QuoteOpportunitiesFile> util = new ExcelUtil<QuoteOpportunitiesFile>(QuoteOpportunitiesFile.class);
        util.exportExcel(response, list, "商机附件数据");
    }

    /**
     * 获取商机附件详细信息
     */
    @ApiOperation(value = "获取商机附件详细信息", notes = "获取商机附件详细信息")
    @PreAuthorize("@ss.hasPermi('quote:file:query')")
    @GetMapping(value = "/{id}")
    public R<QuoteOpportunitiesFile> getInfo(@PathVariable("id") Long id)
    {
        return  R.ok(quoteOpportunitiesFileService.selectQuoteOpportunitiesFileById(id));
    }


    /**
     * 删除商机附件
     */
    @ApiOperation(value = "删除商机附件", notes = "删除商机附件")
    @PreAuthorize("@ss.hasPermi('quote:file:remove')")
    @Log(title = "商机附件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids)
    {
        return R.ok(quoteOpportunitiesFileService.deleteQuoteOpportunitiesFileByIds(ids));
    }

    /**
     * 上传附件(多个)
     */
    @ApiOperation(value = "上传附件(多个)", notes = "上传附件(多个)")
    @Log(title = "上传附件(多个)", businessType = BusinessType.INSERT)
    @PostMapping("/upLoadFiles")
    public R<Boolean> upLoadFiles(@RequestParam("opportunitiesId") Long opportunitiesId,
                                  @RequestParam("fileList") List<MultipartFile> fileList) throws Exception
    {
        List<QuoteOpportunitiesFile> quoteOpportunitiesFileList = new LinkedList<>();
        try
        {
            // 上传文件路径
            String filePath = InternalConfig.getUploadPath();
            //把多个文件都弄到一个文件夹里
            Path quoteFilePath = Paths.get(filePath,opportunitiesId.toString());

            File quoteFilePathDir = quoteFilePath.toFile();
            if (!quoteFilePathDir.exists()) {
                boolean result = quoteFilePathDir.mkdirs();
            }
            for (MultipartFile file : fileList)
            {
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(quoteFilePath.toString(), file);
                String url = serverConfig.getUrl() + fileName;

                QuoteOpportunitiesFile opportunitiesFile = new QuoteOpportunitiesFile();
                opportunitiesFile.setOpportunitiesId(opportunitiesId);
                //opportunitiesFile.setOpportunitiesChildrenId(opportunitiesChildrenId);
                opportunitiesFile.setUrl(url);
                opportunitiesFile.setPath(fileName);
                opportunitiesFile.setFileType(FileUtils.getFileExtension(fileName));
                opportunitiesFile.setSuffix(FileUtils.getFileExtension(fileName));
                opportunitiesFile.setUniqueFileName(FileUtils.getName(fileName));
                opportunitiesFile.setOriginalFileName(file.getOriginalFilename());
                quoteOpportunitiesFileList.add(opportunitiesFile);
            }
        }
        catch (Exception e)
        {
            return R.fail(false);
        }

        return R.ok(quoteOpportunitiesFileService.upLoadFiles(quoteOpportunitiesFileList));
    }

    /**
     * 下载附件
     */
    @ApiOperation(value = "下载附件", notes = "下载附件")
    @Log(title = "下载附件", businessType = BusinessType.OTHER)
    @GetMapping("/downLoadFiles")
    public void downLoadFiles(Long opportunitiesId, HttpServletResponse response, HttpServletRequest request) throws Exception
    {
        QuoteOpportunities entity = quoteOpportunitiesService.getById(opportunitiesId);
        if(ObjectUtil.isNotNull(entity)){
            // 上传文件路径
            String filePath = InternalConfig.getUploadPath();
            //把多个文件都弄到一个文件夹里
            String folderPath = Paths.get(filePath,opportunitiesId.toString()).toString();
            //String zipFileName = "商机-" + entity.getName() + ".zip";
            String zipFileName = "internal.zip";
            byte[] data = ZipUtils.zipFolder(folderPath);

            response.reset();
            response.addHeader("access-control-allow-credentials","true");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment; filename=\"internal.zip\"");
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream; charset=UTF-8");
            IOUtils.write(data, response.getOutputStream());
        }
    }
}
