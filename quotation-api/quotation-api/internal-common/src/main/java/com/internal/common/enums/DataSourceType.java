package com.internal.common.enums;

/**
 * 数据源
 * 
 * @author every
 */
public enum DataSourceType
{
    /**
     * 主库
     */
    MASTER("master"),

    /**
     * 从库
     */
    SLAVE("slave"),

    /**
     * 日报
     */
    NLPM("nlpm"),

    /**
     * CRM数据库
     */
    CRM_APP("crm_app");

    private final String value;

    DataSourceType(String value) {
        this.value = value;
    }

    /**
     * 获取描述信息
     * @return 描述信息
     */
    public String getValue() {
        return this.value;
    }
}
