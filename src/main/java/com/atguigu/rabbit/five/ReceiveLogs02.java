package com.atguigu.rabbit.five;

import com.atguigu.rabbit.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * <p>项目文档： 消息的接收</p>
 *
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月21日 11:13:00
 */
public class ReceiveLogs02 {
    // 交换机名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        // 获取一个信道
        Channel channel = RabbitUtils.getChannel();
        // 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 声明一个临时队列，队列的名称是随机的，当消费者断开与队列的连接后，队列就自动删除
        String queueName = channel.queueDeclare().getQueue();
        // 绑定交换机与队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("ReceiveLogs02等待接收消息......");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {

            System.out.println("ReceiveLogs02控制台打印接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
