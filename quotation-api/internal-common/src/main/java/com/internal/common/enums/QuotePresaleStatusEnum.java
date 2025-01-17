package com.internal.common.enums;

/**
 * 商机售前报价信息-状态
 * 
 * @author every
 */
public enum QuotePresaleStatusEnum {
    // 状态（启用:1,生效中:2）
    ENABLED("1", "启用"),
    DISABLED("2", "未启用");

    private final String code;
    private final String info;

    QuotePresaleStatusEnum(String code, String info)
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
