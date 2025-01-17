package com.internal.common.enums;

/**
 * CRM商机阶段
 * 
 * @author every
 */
public enum CrmQuoteStateEnum {
    // CRM商机阶段 [
    // 立项:0199b1b1122542d5a1e1c5b0c96fdf5c,
    // 成交:69a0d6025770488db4f21155c96f906e,
    // 意向:84a3452395ba491e999964f47b4f76cb,
    // 发货:88bd1d65e74a45169a9c230a6462a487,
    // 潜在:bd12921998df42599551a979ebac5c7a,
    // 谈判:e176d59243e941d9b0c2d3a3074c2427
    // 认可:fa609aa01a7e4e478c4db177975d2932
    // ]
    PROJECT_INITIATION("0199b1b1122542d5a1e1c5b0c96fdf5c", "立项"),
    DEAL("69a0d6025770488db4f21155c96f906e", "成交"),
    INTENTION("84a3452395ba491e999964f47b4f76cb", "意向"),
    SHIPPING("88bd1d65e74a45169a9c230a6462a487", "发货"),
    POTENTIAL("bd12921998df42599551a979ebac5c7a", "潜在"),
    NEGOTIATION("e176d59243e941d9b0c2d3a3074c2427", "谈判"),
    RECOGNITION("fa609aa01a7e4e478c4db177975d2932", "认可");

    private final String code;
    private final String info;

    CrmQuoteStateEnum(String code, String info)
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
