package com.internal.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.internal.common.exception.CustomizedException;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 错误信息处理类。
 *
 * @author every
 */
public class ExceptionUtil
{
    /**
     * 获取异常的详细错误信息（仅用于内部调试或日志记录，不要返回给用户）。
     *
     * @param e 异常对象
     * @return 异常的堆栈信息字符串
     */
    public static String getExceptionMessage(Throwable e)
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }

    /**
     * 获取异常的根错误信息（仅用于内部调试或日志记录，不要返回给用户）。
     *
     * @param e 异常对象
     * @return 异常的根错误信息
     */
    public static String getRootErrorMessage(Exception e) {
        Throwable root = ExceptionUtils.getRootCause(e);
        root = (root == null ? e : root);
        if (root == null)
        {
            return "";
        }
        String msg = root.getMessage();
        if (msg == null)
        {
            return "null";
        }
        return StringUtils.defaultString(msg);
    }

    /**
     * 获取用户友好的错误信息（可以返回给用户）。
     *
     * @param e 异常对象
     * @return 用户友好的错误信息
     */
    public static String getUserFriendlyMessage(Throwable e) {
        // 返回异常的基本信息，而不是完整的堆栈信息
        return "操作失败，请稍后重试。如果问题持续，请联系管理员。";
    }

    /**
     * 如果是项目里throw的错误，就直接throw出去，不然就throw自定义的信息
     * @param e
     * @param message
     * @throws Exception
     */
    public static void customizedThrow(Exception e, String message) throws Exception
    {
        if(e instanceof CustomizedException){
            throw e;
        }else {
            throw new CustomizedException(message);
        }
    }
}

