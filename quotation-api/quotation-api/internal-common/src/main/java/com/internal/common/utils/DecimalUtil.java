package com.internal.common.utils;

import cn.hutool.core.util.ObjectUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class DecimalUtil {
    /**
     * 计算精度
     */
    public static final int defaultScale = 8;
    /**
     * 前端显示精度
     */
    public static final int displayScale = 2;
    /**
     * 前端显示精度
     */
    public static final int displayRateScale = 4;

    /**
     * 设置对象里所有BigDecimal属性的小数位数
     * @param obj 对象
     * @param scale 小数位数
     */
    public static void setObjectScale(Object obj,int scale){
        if(obj != null){
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.getType().equals(BigDecimal.class)){
                    // 设置属性可访问（即使它是私有的）
                    field.setAccessible(true);
                    try {
                        BigDecimal value = (BigDecimal) field.get(obj);
                        if(value != null){
                            if(value.scale() != scale){
                                BigDecimal newValue = value.setScale(scale, RoundingMode.HALF_UP);
                                field.set(obj,newValue);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }
    }

    /**
     * 设置list里所有对象的所有BigDecimal属性的小数位数
     * @param list 列表
     * @param scale 小数位数
     */
    public static <T> void setListScale(List<T> list, int scale){
        if(list != null && list.size() > 0)
        {
            for (T obj:list) {
                if(obj == null){
                    continue;
                }
                Class<?> clazz = obj.getClass();
                Field[] fields = clazz.getDeclaredFields();

                for (Field field : fields) {
                    if (field.getType().equals(BigDecimal.class)){
                        // 设置属性可访问（即使它是私有的）
                        field.setAccessible(true);
                        try {
                            BigDecimal value = (BigDecimal) field.get(obj);
                            if(value != null && value.scale() != scale){
                                BigDecimal newValue = value.setScale(scale, RoundingMode.HALF_UP);
                                field.set(obj,newValue);
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    /**
     * 计算list里数据的总和(包含判空)
     * @param list
     * @return
     */
    public static BigDecimal addAll(BigDecimal... list){
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal item: list) {
            result = result.add(getValue(item));
        }
        return result;
    }

    /**
     * 获取字符串,支持判空，支持设置小数位数，百分比格式
     * @param b
     * @param scale
     * @param rate
     * @return
     */
    public static String getStr(BigDecimal b,int scale,boolean rate){
        String result = "无";
        if(b != null){
            if(rate){
                result = b.multiply(BigDecimal.valueOf(100)).setScale(scale, RoundingMode.HALF_UP).toString() + "%";
            }else {
                result = b.setScale(scale, RoundingMode.HALF_UP).toString();
            }
        }
        return result;
    }

    /**
     * getStr的重载，默认不是百分比格式
     * @param b
     * @param scale
     * @return
     */
    public static String getStr(BigDecimal b,int scale){
        return getStr(b,scale,false);
    }



    public static BigDecimal getValue(BigDecimal b){
        return b == null ? BigDecimal.ZERO : b;
    }
}
