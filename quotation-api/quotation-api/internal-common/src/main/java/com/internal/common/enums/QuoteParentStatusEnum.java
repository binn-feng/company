package com.internal.common.enums;

/**
 * 商机主表状态
 *
 * @author every
 */
public enum QuoteParentStatusEnum {
    // 商机状态;[丢单:00001303,进行中:00001301,暂停:00001304,终止:00001305,成单:00001302]
    DISCARD("00001303", "丢单"),
    PROCESSING("00001301", "进行中"),
    PAUSE("00001304", "暂停"),
    TERMINATION("00001305", "终止"),

    SUCCESS("00001302", "成单");

    private final String code;
    private final String info;

    QuoteParentStatusEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    // 通过code获取info
    public static String getInfo(String code) {
        for (QuoteParentStatusEnum value : QuoteParentStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getInfo();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
