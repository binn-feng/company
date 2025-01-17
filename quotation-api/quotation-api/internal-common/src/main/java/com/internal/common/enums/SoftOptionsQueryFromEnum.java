package com.internal.common.enums;

/**
 * 单位类型
 * 
 * @author every
 */
public enum SoftOptionsQueryFromEnum
{
    // 下拉列表来源[1:软著管理页,2:产品管理页]
    SOFT("1", "软著管理页"),
    PRODUCT("2", "产品管理页");


    private final String code;
    private final String info;

    SoftOptionsQueryFromEnum(String code, String info)
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
