package com.atguigu.rabbit.six;

import com.atguigu.rabbit.utils.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * <p>项目文档： 发送消息给交换机</p>
 * 直接交换机
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月21日 11:59:00
 */
public class DirectLogs {
    // 交换机的名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        // 声明一个信道
        Channel channel = RabbitUtils.getChannel();
        // 声明一个交换机，类型为直接交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.nextLine();
            channel.basicPublish(EXCHANGE_NAME, "error", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
