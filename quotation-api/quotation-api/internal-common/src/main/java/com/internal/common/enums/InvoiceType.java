package com.internal.common.enums;

/**
 * 发票类型
 * 
 * @author every
 */
public enum InvoiceType
{
    // 发票类型;[普通发票:ORDINARY,专用发票:CATEGORICAL,不确定:INCONCLUSIVE]
    ORDINARY("ORDINARY", "普通发票"),
    CATEGORICAL("CATEGORICAL", "专用发票"),
    INCONCLUSIVE("INCONCLUSIVE", "不确定");

    private final String code;
    private final String info;

    InvoiceType(String code, String info)
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
