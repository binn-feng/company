package com.internal.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 因为 父类WebMvcConfigurationSupport 重写了 extendMessageConverters 方法
 * 覆盖了原方法.导致了 配置文件中的该配置失效  #所有数字返回给前端的时候,都转成字符串 spring.jackson.generator.write-numbers-as-strings=true
 * 所以此处,我们自己重写 extendMessageConverters 方法
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 扩展MVC框架的消息转换器.请求数据返回的结果,在返回给前端之前进行一次转换.
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter myConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器,底层使用Jackson将Java对象转为Json
        //我们自己的转换器 【JacksonObjectMapper】 定义在 common中.....如果是我的话,不会定义在这么远.
        myConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0, myConverter);
    }
}