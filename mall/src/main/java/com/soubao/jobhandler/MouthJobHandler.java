package com.soubao.jobhandler;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.exception.JobsException;
import com.baomidou.jobs.handler.IJobsHandler;
import com.soubao.service.CombinationService;
import com.soubao.service.FlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MouthJobHandler implements IJobsHandler {

    @Autowired
    private FlashSaleService flashSaleService;
    @Autowired
    private CombinationService combinationService;

    @Override
    public JobsResponse execute(String tenantId, String param) throws JobsException {

        flashSaleService.deleteTask();
        combinationService.deleteTask();

        return JobsResponse.ok();
    }
}
