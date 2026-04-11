package com.example.submissionservice.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    
    // Tham số 'true' nghĩa là dữ liệu sẽ không bị mất kể cả khi RabbitMQ bị reset
    @Bean
    public Queue submissionQueue() {
        return new Queue("submission.queue", true);
    }
}