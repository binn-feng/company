package com.internal.common.enums;

/**
 * 状态
 * 
 * @author every
 */
public enum QuoteStatusEnum {
    // 状态（报价申请:0,待成本报价:1,待销售报价:2,销售报价待审批:3,销售已报价:4,待签约:5,已签约:6,丢单:7,销售报价审批未通过:8,签约审批未通过:9,待签约审批:10,废弃:11,关闭:12）
    /**
     * 报价申请
     */
    QUOTE_APPLY("0", "报价申请"),
    /**
     * 待成本报价
     */
    WAIT_COST_QUOTE("1", "待成本报价"),
    /**
     * 待销售报价
     */
    WAIT_SALE_QUOTE("2", "待销售报价"),
    /**
     * 销售报价待审批
     */
    SALE_PENDING_APPROVAL("3","销售报价待审批"),
    /**
     * 销售已报价
     */
    SALE_QUOTE("4", "销售已报价"),
    /**
     * 待签约
     */
    WAIT_SIGN("5", "待签约"),
    /**
     * 已签约
     */
    SIGNED("6", "已签约"),
    /**
     * 丢单
     */
    LOST("7", "丢单"),
    /**
     * 销售报价审批未通过
     */
    SALE_REJECTED("8","销售报价审批未通过"),
    /**
     * 签约审批未通过
     */
    SIGN_REJECTED("9","签约审批未通过"),
    /**
     * 待签约审批
     */
    SIGN_PENDING_APPROVAL("10","待签约审批"),
    /**
     * 废弃
     */
    ABANDON("11","废弃"),
    /**
     * 关闭
     */
    CLOSE("12","关闭"),
    /**
     * 待更新成本报价
     */
    UPDATE_PENDING("13","待更新成本报价"),
    /**
     * 待确认丢单
     */
    LOSE_PENDING_APPROVAL("14","待确认丢单"),
    /**
     * 丢单审批未通过
     */
    LOSE_REJECTED("15","丢单审批未通过"),
    ;

    private final String code;
    private final String info;

    QuoteStatusEnum(String code, String info)
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
