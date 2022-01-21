package com.atguigu.rabbit.two;

import com.atguigu.rabbit.utils.RabbitUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * <p>项目文档： 这是一个工作线程，相当于之前的消费者</p>
 *
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月19日 11:24:00
 */
public class Work01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();

        // 消息的接收
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };

        // 消息接收被取消时，执行下面的内容
        CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "消费者取消消费接口的回调逻辑");

        /**
         * 消费者消费消息
         * 1. 消息哪个队列
         * 2. 消费成功之后是否要自动应答， true代表自动应答，false代表手动应答
         * 3. 消费者成功消费后的回调
         * 4. 消费者中断消费后的回调
         */
        System.out.println("C2等待接收消息");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
