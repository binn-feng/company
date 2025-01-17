package com.internal.common.enums;

/**
 * 版本状态
 * 
 * @author every
 */
public enum ProductStatusEnum {
    // 产品状态[1:可用,2:不可用]
    ENABLE("1", "可用"),
    DISABLE("2", "不可用");

    private final String code;
    private final String info;

    ProductStatusEnum(String code, String info)
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
