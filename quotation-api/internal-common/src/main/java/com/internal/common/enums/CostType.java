package com.internal.common.enums;

/**
 * 管理系统-成本管理-成本类型
 * 
 * @author every
 */
public enum CostType
{
    // 成本类型;[需求分析:RA,UI设计:UI,开发:DEV,测试:TEST,项目管理:PM,售前支持:PRE,项目实施:IMP]
    RA("RA", "需求分析"),
    UI("UI", "UI设计"),
    DEV("DEV", "开发"),
    TEST("TEST", "测试"),
    PM("PM","项目管理"),
    PRE("PRE","售前支持"),
    IMP("IMP","项目实施");

    private final String code;
    private final String info;

    CostType(String code, String info)
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
