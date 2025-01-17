package com.internal.common.exception;

/**
 * 自定义异常
 * 
 * @author every
 */
public class CustomizedException extends RuntimeException
{
    public CustomizedException()
    {
    }

    public CustomizedException(String message) {
        super(message);
    }
}
