package com.internal.common.enums;

/**
 * 成本报价流程类别
 * 
 * @author every
 */
public enum QuoteProcessCategory
{
    // 流程类别[1:正常流程,2:需更新成本报价]
    NORMAL("1", "正常流程"),
    UPDATE_REQUIRED("2", "需更新成本报价"),
    UPDATED("3", "成本报价已更新");

    private final String code;
    private final String info;

    QuoteProcessCategory(String code, String info)
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
