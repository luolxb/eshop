package com.soubao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue createOrders() {
        return new Queue("create_order.user");
    }

    @Bean
    public Queue userCancelOrder() {
        return new Queue("cancelled_order.user");
    }

    @Bean
    public Queue payOrders() {
        return new Queue("pay_order.user");
    }

    @Bean
    public Queue receiveOrder() {
        return new Queue("receive_order.user");
    }

    @Bean
    public Queue confirmRebate() {
        return new Queue("confirm_rebate.user");
    }
}
