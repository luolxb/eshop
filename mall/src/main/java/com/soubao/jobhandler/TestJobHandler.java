package com.soubao.jobhandler;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.exception.JobsException;
import com.baomidou.jobs.handler.IJobsHandler;
import org.springframework.stereotype.Component;

@Component
public class TestJobHandler implements IJobsHandler {
    @Override
    public JobsResponse execute(String tenantId, String param) throws JobsException {
        System.out.println("执行testJobHandler");
        return JobsResponse.ok();
    }
}
