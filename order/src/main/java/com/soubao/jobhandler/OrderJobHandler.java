package com.soubao.jobhandler;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.exception.JobsException;
import com.baomidou.jobs.handler.IJobsHandler;
import com.soubao.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderJobHandler implements IJobsHandler {
    @Autowired
    private OrderService orderService;

    @Override
    public JobsResponse execute(String tenantId, String param) throws JobsException {
        orderService.schedule();
        return JobsResponse.ok();
    }
}
