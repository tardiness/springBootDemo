package com.test.thin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author Chengjiazong
 * @project onlytech-server
 * @createDate 2017/12/1
 * @modfiyDate
 * @function
 */
@Configuration
@PropertySource("classpath:config.properties")
public class SettingConfig {

    @Value("${url.prefix}")
    private String urlPrefix;

    @Value("${jc.topic.url}")
    private String jcTopicUrl;

    @Value("${jc.topic.user}")
    private String jcTopicUser;

    @Value("${jc.topic.pwd}")
    private String jcTopicPwd;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigure() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public String getJcTopicUrl() {
        return jcTopicUrl;
    }

    public String getJcTopicUser() {
        return jcTopicUser;
    }

    public String getJcTopicPwd() {
        return jcTopicPwd;
    }
}
