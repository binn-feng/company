package com.internal.common.enums;

/**
 * 丢单类型
 * 
 * @author every
 */
public enum QuoteLoseType
{
    //丢单类型[1:待销售报价(未销售报价),2:待销售报价(利润率低于15%),3:销售报价审批未通过,4:报价单,5:待签约审批,6:签约审批未通过,7:CRM丢单]
    /**
     * 待销售报价(未销售报价)
     */
    WAIT_SALE_QUOTE_1("1", "待销售报价(未销售报价)"),
    /**
     * 待销售报价(利润率低于15%)
     */
    WAIT_SALE_QUOTE_2("2", "待销售报价(利润率低于15%)"),
    /**
     * 销售报价审批未通过
     */
    SALE_REJECTED("3","销售报价审批未通过"),
    /**
     * 报价单
     */
    SALE_QUOTE("4", "报价单"),
    /**
     * 待签约审批
     */
    SIGN_PENDING_APPROVAL("5","待签约审批"),
    /**
     * 签约审批未通过
     */
    SIGN_REJECTED("6","签约审批未通过"),
    /**
     * CRM丢单
     */
    FROM_CRM("7","CRM丢单"),
    ;



    private final String code;
    private final String info;

    QuoteLoseType(String code, String info)
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
