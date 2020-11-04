package com.soubao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue createOrders() {
        return new Queue("create_order.mall");
    }

    @Bean
    public Queue payOrders() {
        return new Queue("pay_order.mall");
    }

    @Bean
    public Queue cancelledOrder() {
        return new Queue("cancelled_order.mall");
    }

    @Bean
    public Queue commentOrder() {
        return new Queue("comment_order.mall");
    }

    @Bean
    public Queue refundOrder() {
        return new Queue("refund_order.mall");
    }

}
