package com.internal.common.utils.poi;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Author:         ww
 * Datetime:       2020\8\12 0012
 * Description:    excel读取监听器
 */
@Slf4j
public class ExcelHeaderListener extends AnalysisEventListener<Map<Integer, String>> {

    private List<String> headers = new LinkedList<>();

    /**
     * 读取excel数据前操作(只有不读取表头数据时才会触发此方法)
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("======================================================");
        log.info("解析第一行数据:{}"+ JSON.toJSONString(headMap));
        log.info("======================================================");
       headMap.forEach((key,value) ->{
           headers.add(value);
       });
    }

    /**
     * 读取excel数据操作
     * @param context
     */
    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
    }

    /**
     * 读取完excel数据后的操作
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    /**
     * 在读取excel异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {

    }

    /**
     * @return 返回读取excel总数据
     */
    public List<String> getHeaders() {
        return headers;
    }
}