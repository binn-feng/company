package com.internal.common.utils;

import cn.hutool.core.util.ObjectUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class MapUtil {
    private MapUtil(){

    }

    /**
     * 根据key获取map，包含判空，有任意一参数为空时返回null
     * @param map
     * @param key
     * @return
     * @param <T1>
     * @param <T2>
     */
    public static <T1,T2> T2 getMapValue(Map<T1,T2> map,T1 key)
    {
        if (mapContainsKey(map,key)){
            return map.get(key);
        }
        else {
            return null;
        }
    }

    public static <T1,T2> boolean mapContainsKey(Map<T1,T2> map,T1 key){
        return map != null && map.size() > 0 && key!= null && map.containsKey(key) && ObjectUtil.isNotEmpty(map.get(key));
    }

}
