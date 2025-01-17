package com.internal.common.utils.poi;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author:         ww
 * Datetime:       2020\8\12 0012
 * Description:    excel读取监听器
 */
@Slf4j
public class DefaultExcelListener<T> extends AnalysisEventListener<T> {

    private final List<T> rows = new ArrayList();

    /**
     * 读取excel数据前操作(只有不读取表头数据时才会触发此方法)
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("======================================================");
        log.info("解析第一行数据:{}"+ JSON.toJSONString(headMap));
        log.info("======================================================");
    }

    /**
     * 读取excel数据操作
     * @param object
     * @param context
     */
    @Override
    public void invoke(T object, AnalysisContext context) {

        rows.add(object);
        log.info("list容量"+rows.size()+"---"+object);
        /** 数据量不是特别大，可以不需要打开
         // 实际数据量比较大时，rows里的数据可以存到一定量之后进行批量处理（比如存到数据库），
         // 然后清空列表，以防止内存占用过多造成OOM
        if(rows.size() >= 500){
            log.info("存入数据库ing");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rows.clear();
        }
        */
    }

    /**
     * 读取完excel数据后的操作
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("成功读取【"+ rows.size()+"】条数据");
    }

    /**
     * 在读取excel异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
        }
    }

    /**
     * @return 返回读取excel总数据
     */
    public List<T> getRows() {
        return rows;
    }
}