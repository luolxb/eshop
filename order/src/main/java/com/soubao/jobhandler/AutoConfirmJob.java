package com.soubao.jobhandler;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.exception.JobsException;
import com.baomidou.jobs.handler.IJobsHandler;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.Order;
import com.soubao.entity.RebateLog;
import com.soubao.service.MallService;
import com.soubao.service.OrderService;
import com.soubao.service.OrderStatisService;
import com.soubao.service.RebateLogService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AutoConfirmJob implements IJobsHandler {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderStatisService orderStatisService;
    @Autowired
    private RebateLogService rebateLogService;
    @Autowired
    private MallService mallService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public JobsResponse execute(String tenantId, String param) throws JobsException {
        Map<Object, Object> config = mallService.config();
        ///申请售后时间段 商家结算时间拿来 跟分销结算一起同一时间
        String date = (String) config.getOrDefault("shopping_auto_service_date", "7");
        List<RebateLog> rebateLogs = rebateLogService.list(new QueryWrapper<RebateLog>().eq("status", 2)
                .apply("confirm < UNIX_TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL " + date + " DAY))"));
        if (rebateLogs.size() > 0) {
            for (RebateLog rebateLog : rebateLogs) {
                rebateLog.setStatus(3);
                rebateLog.setConfirmTime(System.currentTimeMillis() / 1000);
                rebateLog.setRemark(rebateLog.getRemark() + "满" + date + "天,程序自动分成.");
            }
            rebateLogService.updateBatchById(rebateLogs);
            rabbitTemplate.convertAndSend("confirm_rebate", "", rebateLogs);
        }

        //结算给商家
        List<Order> orders = orderService.list((new QueryWrapper<Order>()).eq("pay_status", 1).in("order_status", 2, 4)
                .eq("order_statis_id", 0).orderByAsc("confirm_time").apply("confirm_time < UNIX_TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL " + date + " DAY))"));
        orderStatisService.createOrderSettlement(config, orders);
        return JobsResponse.ok();
    }
}
