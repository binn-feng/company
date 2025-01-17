package com.internal.common.enums;

/**
 * CRM商机状态
 * 
 * @author every
 */
public enum CrmQuoteStatusEnum {
    // CRM商机状态 [失败:00001303,进行中:00001301,暂停:00001304,终止:00001305,成单:00001302,系统终止:00001306]
    FAIL("00001303", "失败"),
    PROCESSING("00001301", "进行中"),
    PAUSE("00001304", "暂停"),
    TERMINATE("00001305", "终止"),
    SUCCESS("00001302", "成单"),
    SYSTEM_TERMINATION("00001306", "系统终止");

    private final String code;
    private final String info;

    CrmQuoteStatusEnum(String code, String info)
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
