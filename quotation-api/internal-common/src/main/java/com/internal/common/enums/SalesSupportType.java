package com.internal.common.enums;

/**
 * 售前支持类型
 * 
 * @author every
 */
public enum SalesSupportType
{
    // 售前支持类型;[已投入:INVESTED,预计后期投入:LATE_INPUTS]
    INVESTED("INVESTED", "已投入"),
    LATE_INPUTS("LATE_INPUTS", "预计后期投入");

    private final String code;
    private final String info;

    SalesSupportType(String code, String info)
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
