package com.atguigu.rabbit.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * <p>项目文档： 生产者</p>
 * 发送消息
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月18日 20:39:00
 */
public class Producer {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    /**
     * 发送消息
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 工厂ip 连接RabbitMQ的队列
        factory.setHost("182.92.176.32");
        // 用户名
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 创建连接
        Connection connection = factory.newConnection();
        // 获取信道
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列
         * 1. 队列名称
         * 2. 队列里的消息是否持久化（磁盘）。默认情况下消息存储在内存中
         * 3. 该队列是否只供一个消费者进行消费，是否进行消息共享。true只能一个消费者消费，false可以多个消费者消费，
         * 4. 是否自动删除，最后一个消费者断开连接后，该队列是否自动删除，true自动删除，false不自动删除
         * 5. 其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, true, false, null);
        // 消息内容
        String message  = "hello world cjb";

        /**
         *  发送消息
         *  1. 发送到哪个交换机
         *  2. 路由的key值 （本次是队列名称）
         *  3. 其他参数信息
         *  4. 发送消息的消息体
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕捏");
    }
}
