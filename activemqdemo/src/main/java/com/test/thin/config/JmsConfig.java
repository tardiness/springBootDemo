package com.test.thin.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

/**
 * @author Chengjiazong
 * @project onlytech-server
 * @createDate 2017/11/27
 * @modfiyDate
 * @function
 */
@Configuration
@EnableJms
public class JmsConfig {

    @Autowired
    private SettingConfig settingConfig;

    @Bean
    public JmsListenerContainerFactory<?> topicListenerFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                settingConfig.getJcTopicUser(), settingConfig.getJcTopicPwd(), settingConfig.getJcTopicUrl());
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(true);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> queueListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(false);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}
