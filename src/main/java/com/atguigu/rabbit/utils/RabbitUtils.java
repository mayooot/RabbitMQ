package com.atguigu.rabbit.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * <p>项目文档： 创建连接工厂获取信道的工具类</p>
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月19日 11:18:00
 */
public class RabbitUtils {

    public static Channel getChannel() throws Exception{
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 工厂ip，连接RabbitMQ队列
        factory.setHost("182.92.176.32");
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 创建连接
        Connection connection = factory.newConnection();
        // 获取信道并返回
        return connection.createChannel();
    }
}
