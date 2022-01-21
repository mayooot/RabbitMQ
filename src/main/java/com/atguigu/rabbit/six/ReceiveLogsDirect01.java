package com.atguigu.rabbit.six;

import com.atguigu.rabbit.utils.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * <p>项目文档： 接收消息</p>
 * 直接交换机
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月21日 11:49:00
 */
public class ReceiveLogsDirect01 {
    // 交换机的名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        // 获取信道
        Channel channel = RabbitUtils.getChannel();
        // 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明一个队列
        channel.queueDeclare("console", false, false, false, null);
        // 绑定交换机和队列
        channel.queueBind("console", EXCHANGE_NAME, "info");
        channel.queueBind("console", EXCHANGE_NAME, "warning");
        System.out.println("ReceiveLogsDirect01接收消息");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {

            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume("console", true, deliverCallback, consumerTag -> {});
    }
}
