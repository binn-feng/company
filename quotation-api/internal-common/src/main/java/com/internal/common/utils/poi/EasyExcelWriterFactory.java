package com.internal.common.utils.poi;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ClassPathResource;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * Author:         ww
 * Datetime:       2020\8\12 0012
 * Description:    链式添加sheet表
 */
public class EasyExcelWriterFactory {

    private int sheetNo = 0;
    private ExcelWriter excelWriter = null;
    private InputStream templateInputStream = null; // 存储模板的 InputStream
    private File templateFile = null; // 存储模板的 File（用于文件导出）

    private OutputStream outputStream;

    public EasyExcelWriterFactory(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public EasyExcelWriterFactory(File file) {
        excelWriter = EasyExcel.write(file).build();
    }

    public EasyExcelWriterFactory(String filePath) {
        excelWriter = EasyExcel.write(filePath).build();
    }

    /**
     * 链式模板表头写入
     * @param headClazz 表头格式
     * @param data 数据 List<ExcelModel> 或者List<List<Object>>
     * @return
     */
    public EasyExcelWriterFactory writeModel(Class headClazz, List data, String sheetName){
        excelWriter.write(data, EasyExcel.writerSheet(this.sheetNo++, sheetName).head(headClazz).build());
        return this;
    }

    /**
     * 链式自定义表头写入
     * @param head
     * @param data 数据 List<ExcelModel> 或者List<List<Object>>
     * @param sheetName
     * @return
     */
    public EasyExcelWriterFactory write(List<List<String>> head, List data, String sheetName){
        excelWriter.write(data, EasyExcel.writerSheet(this.sheetNo++, sheetName).head(head).build());
        return this;
    }
    /**
     * 统一设置模板文件
     * @param templatePath 模板路径
     * @throws Exception
     */
    public EasyExcelWriterFactory setTemplate(String templatePath) throws Exception {
        InputStream templateInputStream = new ClassPathResource(templatePath).getInputStream();
        this.excelWriter = EasyExcel.write(outputStream)
                .withTemplate(templateInputStream)
                .registerWriteHandler(settingStyle())
                .inMemory(Boolean.TRUE)
                .build();
        return this;
    }


    /**
     * 导出Excel文件并填充数据到模板（适用于HTTP响应）
     * @param sheetName sheet名称
     * @param object 填充的数据（Map<String, Object> 或其他）
     * @return 当前对象实例，以支持链式调用
     * @throws Exception
     */
    public EasyExcelWriterFactory exportToResponse(String sheetName, Object object) throws Exception {
        if (templateFile == null && templateInputStream == null) {
            throw new IllegalArgumentException("模板文件未设置，请先调用 setTemplate 方法设置模板");
        }
        WriteSheet writeSheet = EasyExcel.writerSheet(this.sheetNo++, sheetName).sheetName(sheetName).build();
        // 填充数据
        this.excelWriter.fill(object, writeSheet);
        // 强制计算公式
        Workbook workbook = excelWriter.writeContext().writeWorkbookHolder().getWorkbook();
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

        return this;
    }

    /**
     * 设置统一样式
     * @return
     */
    private  HorizontalCellStyleStrategy settingStyle() {
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
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }
    
	/**
     * 使用此类结束后，一定要关闭流
     */
    public void finish() {
        excelWriter.finish();
    }
}