package com.kgc.dm.utils.common;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.jms.Topic;

@Component
public class MqUtils {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    //创建队列模式

    public void sendMessageQueue(String key,Object message)
    {
        Queue queue = new ActiveMQQueue(key);
        jmsMessagingTemplate.convertAndSend(queue, message);
    }

    //创建订阅模式

    public void sendMessageToic(String key,Object message)
    {
        Topic topic = new ActiveMQTopic(key);
        jmsMessagingTemplate.convertAndSend(topic, message);
    }
}
