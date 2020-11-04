package com.soubao.jobhandler;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.exception.JobsException;
import com.baomidou.jobs.handler.IJobsHandler;
import com.soubao.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityJobHandler implements IJobsHandler {
    @Autowired
    private PreSellService preSellService;
    @Autowired
    private FlashSaleService flashSaleService;
    @Autowired
    private GroupBuyService groupBuyService;
    @Autowired
    private TeamFoundService teamFoundService;
    @Autowired
    private PromGoodsService promGoodsService;
    @Autowired
    private CombinationService combinationService;

    @Override
    public JobsResponse execute(String tenantId, String param) throws JobsException {
        //每1秒执行一次
        preSellService.schedule();
        flashSaleService.schedule();
        groupBuyService.schedule();
        promGoodsService.schedule();
        combinationService.schedule();
        //拼团过期后的处理
        teamFoundService.schedule();
        return JobsResponse.ok();
    }
}
