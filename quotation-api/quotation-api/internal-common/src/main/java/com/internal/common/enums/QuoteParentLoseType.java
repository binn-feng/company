package com.internal.common.enums;

/**
 * 父表丢单类型
 * 
 * @author every
 */
public enum QuoteParentLoseType
{
    //父表丢单类型[1:报价系统丢单,2:CRM丢单]
    /**
     * 报价系统丢单
     */
    QUOTE("1", "报价系统丢单"),
    /**
     * CRM丢单
     */
    CRM("2","CRM丢单"),
    ;



    private final String code;
    private final String info;

    QuoteParentLoseType(String code, String info)
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
