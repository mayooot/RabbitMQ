package com.atguigu.rabbit.utils;

/**
 * <p>项目文档： 线程休眠的工具类</p>
 *
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月19日 17:56:00
 */
public class SleepUtils {
    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
