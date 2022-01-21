package com.atguigu.rabbit.three;

import com.atguigu.rabbit.utils.RabbitUtils;
import com.atguigu.rabbit.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * <p>项目文档： TODO</p>
 *
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月19日 17:50:00
 */
public class Work03 {
    // 队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    // 接收消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        System.out.println("C1等待接收消息处理时间较短");

        // 采用手动应答
        // 成功消费后的回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            // 沉睡1S
            SleepUtils.sleep(10);
            System.out.println("C1接收到消息：" + new String(message.getBody()));
            /**
             * 手动应答的方法
             * 1. 消息的标记 tag
             * 2. 是否批量应答 true:批量应答信道中的消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        // 消费失败后的回调函数
        CancelCallback cancelCallback = consumerTag -> System.out.println(consumerTag + "消费者取消消费接口的回调逻辑");

        // 设置不公平分发
        // int prefetchCount = 1;
        // 设置预取值为2
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
        // 采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
