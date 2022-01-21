package com.atguigu.rabbit.four;

import cn.hutool.core.util.IdUtil;
import com.atguigu.rabbit.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <p>
 *     项目文档： 发布确认模式
 *          1. 单个确认模式
 *          2. 批量确认模式
 *          3. 异步批量确认模式
 * </p>
 * @author liming
 * @version 1.0.0
 * @createTime 2022年01月20日 17:54:00
 */
public class ConfirmMessage {

    // 批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        // 1. 单个确认模式，发布1000条单独确认消息，耗时：14487ms
        // ConfirmMessage.publishMessageIndividually();
        // 2. 批量确认模式，发布1000条批量确认消息，耗时：268ms
        // ConfirmMessage.publishMessageBatch();
        // 3. 异步批量确认模式，发布1000条异步确认消息，耗时：106ms
        ConfirmMessage.publishMessageAsync();
    }

    /**
     * 单个确认模式
     * @throws Exception
     */
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitUtils.getChannel();
        String queueName = IdUtil.simpleUUID();
        // 声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        // 批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            // 单个消息马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条单独确认消息，耗时：" + (end - begin) + "ms");
    }

    /**
     * 批量确认模式
     * @throws Exception
     */
    public static void publishMessageBatch() throws Exception {
        // 获取信道
        Channel channel = RabbitUtils.getChannel();
        String queueName = IdUtil.simpleUUID();
        // 声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        // 批量确认消息的大小
        int batchSize = 100;
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            // 发送消息
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            // 判断达到100条消息时，批量确认一次
            if ((i + 1) % batchSize == 0) {
                // 发布确认
                channel.waitForConfirms();
            }
            System.out.println("消息发送成功");
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条批量确认消息，耗时：" + (end - begin) + "ms");
    }

    /**
     * 异步确认模式
     * @throws Exception
     */
    public static void publishMessageAsync() throws Exception {
        // 获取信道
        Channel channel = RabbitUtils.getChannel();
        String queueName = IdUtil.simpleUUID();
        // 声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        /**
         * 开启一个线程安全有序的哈希表，适用于高并发的情况，优点：
         * 1. 将序号(key)和消息(value)进行关联
         * 2. 批量删除消息，只要给到序号
         * 3. 支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        // 消息确认成功，回调函数
        /**
         * ConfirmCallback方法参数列表
         * 1. 消息的标记
         * 2. 是否为批量确认
         */
        ConfirmCallback confirmCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                // 2. 删除已经确认的消息，剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed =
                        outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息：" + deliveryTag);
        };

        // 消息确认失败，回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息：" + message + "，消息的tag：" + deliveryTag);
        };

        // 准备消息的监听器，监听哪些消息成功了，哪些消息失败了
        channel.addConfirmListener(confirmCallback, nackCallback);

        // 开启时间
        long begin = System.currentTimeMillis();
        // 批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 1. 此处记录下所有要发送的消息，即消息总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }


        // 结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条异步确认消息，耗时：" + (end - begin) + "ms");
    }
}
