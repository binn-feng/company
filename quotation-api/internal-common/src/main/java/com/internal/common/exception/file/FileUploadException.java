package com.internal.common.exception.file;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 文件上传异常类
 * 
 * @author every
 */
public class FileUploadException extends Exception
{

    private static final long serialVersionUID = 1L;

    private final Throwable cause;

    public FileUploadException()
    {
        this(null, null);
    }

    public FileUploadException(final String msg)
    {
        this(msg, null);
    }

    public FileUploadException(String msg, Throwable cause)
    {
        super(msg);
        this.cause = cause;
    }

    @Override
    public void printStackTrace(PrintStream stream) {
        // 不再直接输出堆栈信息
        stream.println("FileUploadException: " + getMessage());
        if (cause != null) {
            stream.println("Caused by: " + cause.getMessage());
        }
    }

    @Override
    public void printStackTrace(PrintWriter writer) {
        // 不再直接输出堆栈信息
        writer.println("FileUploadException: " + getMessage());
        if (cause != null) {
            writer.println("Caused by: " + cause.getMessage());
        }
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    /**
     * 重写 toString 方法，返回用户友好的错误信息
     */
    @Override
    public String toString() {
        if (cause == null) {
            return "FileUploadException: " + getMessage();
        } else {
            return "FileUploadException: " + getMessage() + ", Caused by: " + cause.getMessage();
        }
    }
}
