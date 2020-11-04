package com.soubao.jobhandler;

import com.baomidou.jobs.exception.JobsException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.PickOrder;
import com.soubao.service.MallService;
import com.soubao.service.PickOrderActionService;
import com.soubao.service.PickOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AutoReceiveJob {

    @Autowired
    private MallService mallService;
    @Autowired
    private PickOrderService pickOrderService;

    @Autowired
    private PickOrderActionService pickOrderActionService;

    /**
     * 每小时执行一次
     * 自动收货确认
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void execute() throws JobsException {
        log.info("自动收货确认");
        String date = (String) mallService.config().getOrDefault("shopping_auto_confirm_date", "7");
        List<PickOrder> pickOrderList = pickOrderService.list(new QueryWrapper<PickOrder>()
                .eq("pick_order_status", 2)
                .apply("update_time < UNIX_TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL " + date + " DAY))"));

        if (pickOrderList.size() > 0) {
            for (PickOrder pickOrder : pickOrderList) {
                pickOrder.setPickOrderStatus(3);
                pickOrder.setUpdateTime(System.currentTimeMillis() / 1000);
                pickOrderService.updateById(pickOrder);
                pickOrderActionService.addActionLog(pickOrder, "系统自动确认收货", "系统自动确认收货", null, 2, null);
            }
        }
    }
}
