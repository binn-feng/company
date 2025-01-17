package com.internal.common.enums;

/**
 * 版本状态
 * 
 * @author every
 */
public enum ApprovalStatusEnum {
    // 审批状态（无审批:0,待报价审批:1,报价审批通过:2,报价审批未通过:3）
    UNAPPROVED("0", "无审批"),
    SALES_PENDING_APPROVAL("1", "待报价审批"),
    SALES_APPROVED("2", "报价审批通过"),
    SALES_REJECTED("3", "报价审批未通过");

    private final String code;
    private final String info;

    ApprovalStatusEnum(String code, String info)
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
