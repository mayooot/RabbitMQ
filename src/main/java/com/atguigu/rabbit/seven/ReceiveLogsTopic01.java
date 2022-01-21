package com.atguigu.rabbit.seven;

import com.atguigu.rabbit.utils.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * <p>项目文档： 声明主题交换机及相关队列</p>
 * 消费者C1
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月21日 16:58:00
 */
public class ReceiveLogsTopic01 {
    // 交换机的名称
    public static final String EXCHANGE_NAME = "topic_logs";

    /**
     * 接收消息
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 获取信道
        Channel channel = RabbitUtils.getChannel();
        // 声明交换机及其类型
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 声明队列
        String queueName = "Q1";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("消费者C1等待接收消息......");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收队列：" + queueName + "，绑定键：" + message.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("接收失败");
        };
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
