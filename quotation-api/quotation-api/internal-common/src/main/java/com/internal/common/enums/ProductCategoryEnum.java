package com.internal.common.enums;

/**
 * 版本状态
 * 
 * @author every
 */
public enum ProductCategoryEnum {
    // 产品状态[1:可用,2:不可用]
    FIRST("1", "父产品"),
    SECOND("2", "子产品");

    private final String code;
    private final String info;

    ProductCategoryEnum(String code, String info)
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
