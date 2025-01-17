package com.internal.common.enums;

/**
 * 邮件类型
 * 
 * @author every
 */
public enum EmailTypeEnum
{
    //邮件类型[ 需成本报价: NEED_COST_QUOTATION,
    //完成成本报价:COMPLETE_COST_QUOTATION,
    //报价审批: QUOTATION_APPROVAL,
    //报价审批结果通知: QUOTATION_APPROVAL_RESULT_NOTIFICATION,
    //签约申请: SIGNING_APPLICATION,
    //签约审批结果通知: SIGNING_APPROVAL_RESULT_NOTIFICATION,
    //丢单通知: LOSS_NOTIFICATION
    //]
    NEED_COST_QUOTATION("NEED_COST_QUOTATION", "需成本报价"),
    COMPLETE_COST_QUOTATION("COMPLETE_COST_QUOTATION", "完成成本报价"),
    QUOTATION_APPROVAL("QUOTATION_APPROVAL", "报价审批"),
    QUOTATION_APPROVAL_RESULT_NOTIFICATION("QUOTATION_APPROVAL_RESULT_NOTIFICATION", "报价审批结果通知"),
    SIGNING_APPLICATION("SIGNING_APPLICATION", "签约申请"),
    SIGNING_APPROVAL_RESULT_NOTIFICATION("SIGNING_APPROVAL_RESULT_NOTIFICATION", "签约审批结果通知"),
    LOSS_NOTIFICATION("LOSS_NOTIFICATION", "丢单通知");

    private final String code;
    private final String info;

    EmailTypeEnum(String code, String info)
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
