package com.internal.common.utils;


import com.internal.common.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zdliu
 */
public class EmailUtils {
    public static Boolean emailVerify(String email) {
        if(StringUtils.isEmpty(email) || email.length() < 8){
            throw new ServiceException("请检查email格式是否正确");
        }
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((CharSequence) email.trim());
        return matcher.matches();
    }
}
