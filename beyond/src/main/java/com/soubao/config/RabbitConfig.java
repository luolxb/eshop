package com.soubao.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    /********************************* 创建订单广播  *************************/

    @Bean
    public Queue createOrdersWithShop() {
        return new Queue("create_order.mall");
    }

    @Bean
    public Queue createOrdersWithUser() {
        return new Queue("create_order.user");
    }

    @Bean
    public Queue createOrdersWithOrder() {
        return new Queue("create_order.order");
    }

    @Bean
    FanoutExchange createOrders() {
        return new FanoutExchange("create_order");
    }

    @Bean
    Binding bindingExchangeCreateOrdersWithShop(Queue createOrdersWithShop, FanoutExchange createOrders) {
        return BindingBuilder.bind(createOrdersWithShop).to(createOrders);
    }

    @Bean
    Binding bindingExchangeCreateOrdersWithUser(Queue createOrdersWithUser, FanoutExchange createOrders) {
        return BindingBuilder.bind(createOrdersWithUser).to(createOrders);
    }

    @Bean
    Binding bindingExchangeCreateOrdersWithOrder(Queue createOrdersWithOrder, FanoutExchange createOrders) {
        return BindingBuilder.bind(createOrdersWithOrder).to(createOrders);
    }

    /********************************* 支付订单广播  *************************/

    @Bean
    public Queue payOrdersWithShop() {
        return new Queue("pay_order.mall");
    }

    @Bean
    public Queue payOrdersWithUser() {
        return new Queue("pay_order.user");
    }

    @Bean
    public Queue payOrdersWithOrder() {
        return new Queue("pay_order.order");
    }

    @Bean
    FanoutExchange payOrders() {
        return new FanoutExchange("pay_order");
    }

    @Bean
    Binding bindingExchangePayOrdersWithShop(Queue payOrdersWithShop, FanoutExchange payOrders) {
        return BindingBuilder.bind(payOrdersWithShop).to(payOrders);
    }

    @Bean
    Binding bindingExchangePayOrdersWithUser(Queue payOrdersWithUser, FanoutExchange payOrders) {
        return BindingBuilder.bind(payOrdersWithUser).to(payOrders);
    }

    @Bean
    Binding bindingExchangePayOrdersWithOrder(Queue payOrdersWithOrder, FanoutExchange payOrders) {
        return BindingBuilder.bind(payOrdersWithOrder).to(payOrders);
    }

    /********************************* 取消订单广播  *************************/

    @Bean
    public Queue cancelledOrderWithShop() {
        return new Queue("cancelled_order.mall");
    }

    @Bean
    public Queue cancelledOrderWithUser() {
        return new Queue("cancelled_order.user");
    }

    @Bean
    public Queue cancelledOrderWithOrder() {
        return new Queue("cancelled_order.order");
    }

    @Bean
    FanoutExchange cancelOrder() {
        return new FanoutExchange("cancelled_order");
    }

    @Bean
    Binding bindingExchangeCancelOrderWithShop(Queue cancelledOrderWithShop, FanoutExchange cancelOrder) {
        return BindingBuilder.bind(cancelledOrderWithShop).to(cancelOrder);
    }

    @Bean
    Binding bindingExchangeCancelOrderWithUser(Queue cancelledOrderWithUser, FanoutExchange cancelOrder) {
        return BindingBuilder.bind(cancelledOrderWithUser).to(cancelOrder);
    }

    @Bean
    Binding bindingExchangeCancelOrderWithOrder(Queue cancelledOrderWithOrder, FanoutExchange cancelOrder) {
        return BindingBuilder.bind(cancelledOrderWithOrder).to(cancelOrder);
    }

    /********************************* 确认收货订单  *************************/


    @Bean
    public Queue receiveOrderWithUser() {
        return new Queue("receive_order.user");
    }

    @Bean
    public Queue receiveOrderWithOrder() {
        return new Queue("receive_order.order");
    }

    @Bean
    FanoutExchange receiveOrder() {
        return new FanoutExchange("receive_order");
    }

    @Bean
    Binding bindingExchangeReceiveOrderWithUser(Queue receiveOrderWithUser, FanoutExchange commentOrder) {
        return BindingBuilder.bind(receiveOrderWithUser).to(commentOrder);
    }

    @Bean
    Binding bindingExchangeReceiveOrderWithOrder(Queue receiveOrderWithOrder, FanoutExchange commentOrder) {
        return BindingBuilder.bind(receiveOrderWithOrder).to(commentOrder);
    }

    /********************************* 评论订单广播  *************************/

    @Bean
    public Queue commentOrderWithShop() {
        return new Queue("comment_order.mall");
    }

    @Bean
    public Queue commentOrderWithOrder() {
        return new Queue("comment_order.order");
    }

    @Bean
    FanoutExchange commentOrder() {
        return new FanoutExchange("comment_order");
    }

    @Bean
    Binding bindingExchangeCommentOrderWithShop(Queue commentOrderWithShop, FanoutExchange commentOrder) {
        return BindingBuilder.bind(commentOrderWithShop).to(commentOrder);
    }

    @Bean
    Binding bindingExchangeCommentOrderWithOrder(Queue commentOrderWithOrder, FanoutExchange commentOrder) {
        return BindingBuilder.bind(commentOrderWithOrder).to(commentOrder);
    }

    /********************************* 分销结算广播  *************************/

    @Bean
    public Queue confirmRebateWithUser() {
        return new Queue("confirm_rebate.user");
    }


    @Bean
    FanoutExchange confirmRebate() {
        return new FanoutExchange("confirm_rebate");
    }

    @Bean
    Binding bindingExchangeConfirmRebateWithShop(Queue confirmRebateWithUser, FanoutExchange confirmRebate) {
        return BindingBuilder.bind(confirmRebateWithUser).to(confirmRebate);
    }


    /********************************* 订单结算广播  *************************/

    @Bean
    public Queue settleOrderWithSeller() {
        return new Queue("settle_order.seller");
    }


    @Bean
    FanoutExchange settleOrder() {
        return new FanoutExchange("settle_order");
    }

    @Bean
    Binding bindingExchangeSettleOrderWithSeller(Queue settleOrderWithSeller, FanoutExchange settleOrder) {
        return BindingBuilder.bind(settleOrderWithSeller).to(settleOrder);
    }

}
