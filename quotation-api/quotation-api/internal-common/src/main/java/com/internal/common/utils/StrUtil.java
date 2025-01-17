package com.internal.common.utils;

import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StrUtil {
    private StrUtil(){

    }

    public static List<String> splitList(String str,String symbol){
        if(ObjectUtil.isEmpty(str)){
            return new LinkedList<>();
        }
        return Arrays.stream(str.split(symbol)).collect(Collectors.toList());
    }

}
