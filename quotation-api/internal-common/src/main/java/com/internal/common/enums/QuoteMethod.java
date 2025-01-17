package com.internal.common.enums;

/**
 * 单位类型
 * 
 * @author every
 */
public enum QuoteMethod
{
    // 报价方式[0:默认,1:一个人报价,2:多个人报价]
    DEFAULT("0", "无"),
    SINGLE("1", "一个人报价"),
    MULTIPLE("2", "多个人报价");

    private final String code;
    private final String info;

    QuoteMethod(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
