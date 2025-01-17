package com.internal.common.enums;

/**
 * 待签合同-合同类型
 * 
 * @author every
 */
public enum ContractType
{
    // 合同类型(欣象代理:1,北光直签:2)
    XX("1", "欣象代理"),
    NL("2", "北光直签");

    private final String code;
    private final String info;

    ContractType(String code, String info)
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
