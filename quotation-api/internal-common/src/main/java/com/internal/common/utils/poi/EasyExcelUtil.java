package com.internal.common.utils.poi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel相关处理
 *
 * @author every
 */
public class EasyExcelUtil<T> {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 实体对象
     */
    public Class<T> clazz;


    public EasyExcelUtil(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    /**
     * 对excel表单默认第一个索引名转换成list（EasyExcel）
     *
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importEasyExcel(InputStream is) throws Exception
    {
        return EasyExcel.read(is).head(clazz).sheet().doReadSync();
    }

    /**
     * 验证clazz里，包含ExcelProperty的属性和excel里的表头是否对的上
     * @param is
     * @return
     */
    public Boolean verifyProperties(InputStream is){
        Field[] fields = clazz.getDeclaredFields();
        List<String> properties = new LinkedList<>();
        for (Field f : fields) {
            if(f.isAnnotationPresent(ExcelProperty.class)){
                properties.addAll(Arrays.asList(f.getAnnotation(ExcelProperty.class).value()));
            }
        }
        properties = properties.stream().filter(x -> ObjectUtil.isNotEmpty(x)).collect(Collectors.toList());
        ExcelHeaderListener headerListener = new ExcelHeaderListener();
        EasyExcel.read(is,headerListener).sheet().doRead();
        List<String> headList = headerListener.getHeaders();

        return CollUtil.isNotEmpty(properties) && properties.containsAll(headList) && headList.containsAll(properties);
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单（EasyExcel）
     *
     * @param list 导出数据集合
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public void exportEasyExcel(HttpServletResponse response, List<T> list, String sheetName)
    {
        try
        {
            EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(list);
        }
        catch (IOException e)
        {
            log.error("导出EasyExcel异常{}", e.getMessage());
        }
    }

    /**
     * 根据模版导出
     * @param response
     * @param sheetName
     * @param templatePath
     * @param object
     * @throws Exception
     */
    public void exportEasyExcel(HttpServletResponse response, String sheetName, String templatePath, Object object) throws Exception {
        // 头的策略
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = settingStyle();
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream())
                .withTemplate(new ClassPathResource(templatePath).getInputStream())
                .registerWriteHandler(horizontalCellStyleStrategy)
                .inMemory(Boolean.TRUE)
                .build();
        WriteSheet writeSheet = EasyExcelFactory.writerSheet(sheetName).build();
        excelWriter.fill(object, writeSheet);
        Workbook workbook = excelWriter.writeContext().writeWorkbookHolder().getWorkbook();
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        excelWriter.finish();
    }

    /**
     * 根据模版导出
     * @param response
     * @param sheetName
     * @param templatePath
     * @param map
     * @throws Exception
     */
    public void exportEasyExcel(HttpServletResponse response, String sheetName, String templatePath, Map<String, Object> map) throws Exception {
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), clazz)
                .withTemplate(new ClassPathResource(templatePath).getInputStream())
                .registerWriteHandler(settingStyle())
                .inMemory(Boolean.TRUE)
                .build();
        WriteSheet writeSheet = EasyExcelFactory.writerSheet(sheetName).build();
        excelWriter.fill(map, writeSheet);
        Workbook workbook = excelWriter.writeContext().writeWorkbookHolder().getWorkbook();
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        excelWriter.finish();
    }

    /**
     * 设置统一样式
     * @return
     */
    private static HorizontalCellStyleStrategy settingStyle() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontName("微软雅黑");
        contentWriteFont.setFontHeightInPoints((short)12);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return horizontalCellStyleStrategy;
    }

    /**
     * 根据模版导出
     * @param response
     * @param sheetName
     * @param templateInputStream
     * @param map
     * @throws Exception
     */
    public void exportEasyExcel(HttpServletResponse response, String sheetName, InputStream templateInputStream, Map<String, Object> map) throws Exception {
        EasyExcel.write(response.getOutputStream(), clazz).withTemplate(templateInputStream)
                .registerWriteHandler(settingStyle()).sheet(sheetName).doFill(map);
    }

    /**
     * 根据excel模板文件写入文件
     * @param response
     * @param templateFileName
     * @param data
     */
    public void writeTemplate(HttpServletResponse response, String templateFileName, List data) throws IOException {
        EasyExcel.write(response.getOutputStream(), clazz).withTemplate(templateFileName).registerWriteHandler(settingStyle()).sheet().doWrite(data);
    }

    /**
     * 按模板写文件
     * @param filePath
     * @param headClazz 表头模板
     * @param data 数据
     * @param sheetNo sheet页号，从0开始
     * @param sheetName sheet名称
     */
    public  void write(String filePath, Class headClazz, List data, Integer sheetNo, String sheetName){
        EasyExcel.write(filePath, headClazz).sheet(sheetNo, sheetName).doWrite(data);
    }

    /**
     * 按模板写文件
     * @param filePath
     * @param headClazz 表头模板
     * @param data 数据
     * @param writeHandler 自定义的处理器，比如设置table样式，设置超链接、单元格下拉框等等功能都可以通过这个实现（需要注册多个则自己通过链式去调用）
     * @param sheetNo sheet页号，从0开始
     * @param sheetName sheet名称
     */
    public  void write(String filePath, Class headClazz, List data, WriteHandler writeHandler, Integer sheetNo, String sheetName){
        EasyExcel.write(filePath, headClazz).registerWriteHandler(writeHandler).sheet(sheetNo, sheetName).doWrite(data);
    }

    /**
     * 按模板写文件（包含某些字段）
     * @param filePath
     * @param headClazz 表头模板
     * @param data 数据
     * @param includeCols 包含字段集合，根据字段名称显示
     * @param sheetNo sheet页号，从0开始
     * @param sheetName sheet名称
     */
    public  void writeInclude(String filePath, Class headClazz, List data, Set<String> includeCols, Integer sheetNo, String sheetName){
        EasyExcel.write(filePath, headClazz).includeColumnFieldNames(includeCols).sheet(sheetNo, sheetName).doWrite(data);
    }


    /**
     * 按模板写文件（排除某些字段）
     * @param filePath
     * @param headClazz 表头模板
     * @param data 数据
     * @param excludeCols 过滤排除的字段，根据字段名称过滤
     * @param sheetNo sheet页号，从0开始
     * @param sheetName sheet名称
     */
    public void writeExclude(String filePath, Class headClazz, List data, Set<String> excludeCols, Integer sheetNo, String sheetName){
        EasyExcel.write(filePath, headClazz).excludeColumnFieldNames(excludeCols).registerWriteHandler(settingStyle()).sheet(sheetNo, sheetName).doWrite(data);
    }

    /**
     * 多个sheet页的数据链式写入
     * ExcelUtil.writeWithSheets(outputStream)
     *                 .writeModel(ExcelModel.class, excelModelList, "sheetName1")
     *                 .write(headData, data,"sheetName2")
     *                 .finish();
     * @param outputStream
     */
    public static EasyExcelWriterFactory writeWithSheets(OutputStream outputStream){
        return new EasyExcelWriterFactory(outputStream);
    }

    /**
     * 多个sheet页的数据链式写入
     * ExcelUtil.writeWithSheets(file)
     *                 .writeModel(ExcelModel.class, excelModelList, "sheetName1")
     *                 .write(headData, data,"sheetName2")
     *                 .finish();
     * @param file
     */
    public static EasyExcelWriterFactory writeWithSheets(File file){
        return new EasyExcelWriterFactory(file);
    }

    /**
     * 多个sheet页的数据链式写入
     * ExcelUtil.writeWithSheets(filePath)
     *                 .writeModel(ExcelModel.class, excelModelList, "sheetName1")
     *                 .write(headData, data,"sheetName2")
     *                 .finish();
     * @param filePath
     */
    public static EasyExcelWriterFactory writeWithSheets(String filePath){
        return new EasyExcelWriterFactory(filePath);
    }

    /**
     * 多个sheet页的数据链式写入（失败了会返回一个有部分数据的Excel）
     * ExcelUtil.writeWithSheets(response, exportFileName)
     *                 .writeModel(ExcelModel.class, excelModelList, "sheetName1")
     *                 .write(headData, data,"sheetName2")
     *                 .finish();
     * @param response
     * @param exportFileName 导出的文件名称
     */
    public EasyExcelWriterFactory writeWithSheetsWeb(HttpServletResponse response, String exportFileName) throws IOException{
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode(exportFileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        return new EasyExcelWriterFactory(response.getOutputStream());
    }

    /**
     * 初始化响应体
     * @param response 请求头
     * @param fileName 导出名称
     */
    public static void initResponse(HttpServletResponse response, String fileName) {
        // 最终文件名：文件名
        String finalFileName = fileName ;
        // 设置content—type 响应类型
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            // 这里URLEncoder.encode可以防止中文乱码
            finalFileName = URLEncoder.encode(finalFileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", "attachment;filename=" + finalFileName + ".xlsx");
    }


}
