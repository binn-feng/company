package com.internal.common.utils;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author nl
 */
public class VersionUtil {

    private VersionUtil() {
        // 私有化构造方法，防止实例化
    }

    public static String getFirstVersion(){
        return "V1.0.0";
    }

    // 获取当前版本号
    public static String getCurrentVersion(String version) {
        return version;
    }

    // 递增主版本号
    public static String incrementMajor(String version) {
        if(ObjectUtil.isNull(version) || ObjectUtil.isEmpty(version) || "无".equals(version)){
            return getFirstVersion();
        }
        String[] parts = version.replace("V", "").split("\\.");
        int major = Integer.parseInt(parts[0]);
        major++;
        return "V" + major + ".0.0";  // 重置次版本号和修订号
    }

    // 递增次版本号
    public static String incrementMinor(String version) {
        String[] parts = version.replace("V", "").split("\\.");
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        minor++;
        return "V" + major + "." + minor + ".0";  // 重置修订号
    }

    // 递增修订号
    public static String incrementPatch(String version) {
        String[] parts = version.replace("V", "").split("\\.");
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        int patch = Integer.parseInt(parts[2]);
        patch++;
        return "V" + major + "." + minor + "." + patch;
    }
}