package com.atguigu.rabbit.two;

import com.atguigu.rabbit.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * <p>项目文档： 生产者，发送大量消息</p>
 *
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月19日 11:39:00
 */
public class Task01 {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    // 发送大量消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        /**
         * 生成一个队列
         * 1. 队列名称
         * 2. 队列里的消息是否持久化（磁盘）。默认情况下消息存储在内存中
         * 3. 该队列是否只供一个消费者进行消费，是否进行消息共享。true只能一个消费者消费，false可以多个消费者消费，
         * 4. 是否自动删除，最后一个消费者断开连接后，该队列是否自动删除，true自动删除，false不自动删除
         * 5. 其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 从控制台中接收消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            // 发送消息的内容
            String message = scanner.next();
            /**
             *  发送消息
             *  1. 发送到哪个交换机
             *  2. 路由的key值 （本次是队列名称）
             *  3. 其他参数信息
             *  4. 发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成，消息内容为：" + message);
        }
    }
}
