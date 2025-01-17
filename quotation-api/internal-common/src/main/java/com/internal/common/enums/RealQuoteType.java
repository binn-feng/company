package com.internal.common.enums;

/**
 * 类型
 *
 * @author every
 */
public enum RealQuoteType {
    // 类型:[销售支持:SALES_SUPPORT,产品:PRODUCT,软件开发:SOFTWARE_DEVELOPMENT,采购-自研硬件:SELF_DEVELOPED,采购-外采硬件:EXTERNAL_PROCUREMENT,实施:IMPLEMENT,其他:OTHER ]
    /**
     * 销售支持
     */
    SALES_SUPPORT("SALES_SUPPORT", "销售支持"),
    /**
     * 产品
     */
    PRODUCT("PRODUCT", "产品"),
    /**
     * 软件开发
     */
    SOFTWARE_DEVELOPMENT("SOFTWARE_DEVELOPMENT", "软件开发"),
    /**
     * 采购-自研硬件
     */
    SELF_DEVELOPED("SELF_DEVELOPED", "采购-自研硬件"),
    /**
     * 采购-外采硬件
     */
    EXTERNAL_PROCUREMENT("EXTERNAL_PROCUREMENT", "采购-外采硬件"),
    /**
     * 实施
     */
    IMPLEMENT("IMPLEMENT", "实施"),
    /**
     * 其他
     */
    OTHER("OTHER", "其他");

    private final String code;
    private final String info;

    RealQuoteType(String code, String info) {
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
