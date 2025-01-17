package com.internal.common.enums;

/**
 * 版本状态
 * 
 * @author every
 */
public enum VersionStatus
{
    // "版本状态[0暂存;未生效;2生效中]"
    TEMPORARY("0", "暂存"),
    INACTIVE("1", "未生效"),
    ACTIVE("2", "生效中");

    private final String code;
    private final String info;

    VersionStatus(String code, String info)
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
