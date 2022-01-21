package com.atguigu.rabbit.one;

import com.rabbitmq.client.*;

/**
 * <p>项目文档： 消费者</p>
 * 消费者 接收消息
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月18日 21:12:00
 */
public class Consumer {
    // 队列名称
    public static final String QUEUE_NAME = "hello";

    /**
     * 接收信息
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("182.92.176.32");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        CancelCallback cancelCallback = consumerTag -> System.out.println("消费消息被中断！");
        /**
         * 消费者消费消息
         * 1. 消息哪个队列
         * 2. 消费成功之后是否要自动应答， true代表自动应答，false代表手动应答
         * 3. 消费者成功消费后的回调
         * 4. 消费者中断消费后的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
