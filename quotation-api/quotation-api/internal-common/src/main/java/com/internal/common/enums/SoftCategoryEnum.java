package com.internal.common.enums;

/**
 * 软著类别
 * 
 * @author every
 */
public enum SoftCategoryEnum {
    // 产品状态[1:可用,2:不可用]
    XX("1", "欣象软著"),
    NL("2", "北光软著");

    private final String code;
    private final String info;

    SoftCategoryEnum(String code, String info)
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
