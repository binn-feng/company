package com.internal.common.utils;

import com.internal.common.annotation.TemplateField;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author zdliu
 */
@Slf4j
public class TemplateUtil {


    /**
     * 替换模板中的占位符
     *
     * @param template 模板字符串
     * @param obj      包含注解字段的对象
     * @return 替换后的字符串
     */
    public static String replaceTemplate(String template, Object obj){
        String result = template;

        try {
            // 获取对象的所有字段
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 检查字段上是否有TemplateField注解
                if (field.isAnnotationPresent(TemplateField.class)) {
                    TemplateField annotation = field.getAnnotation(TemplateField.class);
                    String placeholder = annotation.value(); // 获取占位符名称

                    // 设置字段可访问
                    field.setAccessible(true);
                    String value = (String) field.get(obj); // 获取字段值

                    if (StringUtils.isNotEmpty(value)) {
                        if(placeholder.contains("链接")){
                            value = "<a href='" + value + "' target='_blank'>" + value + "</a>";
                        }
                        // 替换模板中的占位符
                        result = result.replace("{" + placeholder + "}", value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error("替换模板中的占位符时出错：{}", e.getMessage());
        }
        return result;
    }
}