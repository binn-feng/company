package com.internal.common.enums;

/**
 * 用户key
 *
 * @author every
 */
public enum RoleKeyType {
    // 用户key[普通角色:common,售前支持:presale,财务人员:finance,销售支持:saleSupport,管理层:manager,销售管理:saleManager,采购支持:purchasing]
    COMMON("common", "普通角色"),
    PRESALE("presale", "售前支持"),
    FINANCE("finance", "财务人员"),
    SALE_SUPPORT("saleSupport", "销售支持"),
    MANAGER("manager", "管理层"),
    SALE_MANAGER("saleManager", "销售管理"),
    PURCHASING("purchasing", "采购支持");

    private final String code;
    private final String info;

    RoleKeyType(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
