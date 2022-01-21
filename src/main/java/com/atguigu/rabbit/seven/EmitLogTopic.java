package com.atguigu.rabbit.seven;

import com.atguigu.rabbit.utils.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>项目文档： 生产者</p>
 * rotingKey中的 * 代表任意一个字符串， #代表多个字符串
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月21日 17:11:00
 */
public class EmitLogTopic {
    // 交换机的名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        // 声明一个信道
        Channel channel = RabbitUtils.getChannel();
        // 声明交换机及其名称
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        bindingKeyMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        bindingKeyMap.put("quick.orange.fox","被队列 Q1 接收到");
        bindingKeyMap.put("lazy.brown.fox","被队列 Q2 接收到");
        bindingKeyMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingKeyMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");

        for (Map.Entry<String, String> stringEntry : bindingKeyMap.entrySet()) {
            String rotingKey = stringEntry.getKey();
            String message = stringEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME, rotingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息：" + message + "发送成功！");
        }
    }
}
