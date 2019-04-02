package com.alan.wechat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author alanpan
 * @title: WebSocketConfig
 * @projectName springboot-websocket
 * @description: 用于扫描和注册所有携带ServerEndPoint注解的实例
 * @date 2019/4/122:04
 */

@Configuration
public class WebSocketConfig {

    /**
     * 用于扫描和注册所有携带ServerEndPoint注解的实例。
     * <p>
     * PS:若部署到外部容器 则无需提供此类。
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {

        return new ServerEndpointExporter();
    }
}

