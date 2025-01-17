package com.internal.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程工具类
 *
 * @author zdliu
 */
@Slf4j
public class ThreadUtil {

    // 创建一个固定大小的线程池
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static int activeTasks = 0; // 活动任务计数
    private static final ReentrantLock lock = new ReentrantLock(); // 使用重入锁

    /**
     * 提交一个任务到线程池，并返回 Future 对象
     *
     * @param task 要执行的任务
     * @return Future 对象
     */
    public static Future<?> runInThread(Runnable task) {
        lock.lock();  // 获取锁
        try {
            activeTasks++;
            return executorService.submit(() -> {
                try {
                    task.run();
                } catch (Exception e) {
                    // 使用日志记录异常信息，而不是直接打印堆栈信息
                    log.error("Task encountered an exception: {}", e.getMessage(), e);
                } finally {
                    taskCompleted();
                }
            });
        } finally {
            lock.unlock(); // 确保释放锁
        }
    }

    /**
     * 处理任务完成的逻辑
     */
    private static void taskCompleted() {
        lock.lock(); // 获取锁
        try {
            activeTasks--;
            // 如果需要关闭，请使用 shutdown() 方法
        } finally {
            lock.unlock(); // 确保释放锁
        }
    }

    /**
     * 关闭线程池的方法
     */
    public static void shutdown() {
        lock.lock(); // 获取锁
        try {
            if (!executorService.isShutdown()) {
                executorService.shutdown(); // 关闭线程池，不再接受新任务
                try {
                    if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                        executorService.shutdownNow(); // 如果没有在指定时间内完成，强制关闭
                    }
                } catch (InterruptedException e) {
                    executorService.shutdownNow(); // 如果当前线程被中断，强制关闭
                    Thread.currentThread().interrupt(); // 重新设置中断状态
                }
            }
        } finally {
            lock.unlock(); // 确保释放锁
        }
    }
}