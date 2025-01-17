package com.internal.common.enums;

/**
 * 单位类型
 * 
 * @author every
 */
public enum UnitType
{
    // 单位类型[人天:HUMAN_NATURE,人月:HUMAN_MONTH]
    HUMAN_NATURE("HUMAN_NATURE", "人天"),
    HUMAN_MONTH("HUMAN_MONTH", "人月");

    private final String code;
    private final String info;

    UnitType(String code, String info)
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
