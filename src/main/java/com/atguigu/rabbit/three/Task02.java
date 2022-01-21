package com.atguigu.rabbit.three;

import com.atguigu.rabbit.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * <p>项目文档： 生产者
 * 消息在手动应答时不丢失。
 * 如果消费者断开连接后，会把消息放进队列中重新消费
 * </p>
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月19日 17:33:00
 */
public class Task02 {
     public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        // 从RabbitUtils中获取一个信道
        Channel channel = RabbitUtils.getChannel();
        // 开启发布确定模式
        channel.confirmSelect();

        // 声明队列持久化
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        // 从控制台获取信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.nextLine();
            // 设置生产者发送的消息为持久化消息（要求保存到本地磁盘中，默认保存在内存中）
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("生产者发出一条消息，内容是：" + message);
        }
    }

}
