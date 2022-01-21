package com.atguigu.rabbit.five;

import com.atguigu.rabbit.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * <p>项目文档： 发消息给交换机</p>
 *
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月21日 11:25:00
 */
public class EmitLog {
    // 交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        // 声明一个信道
        Channel channel = RabbitUtils.getChannel();
        // 声明一个交换机，类型为扇出
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.nextLine();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
