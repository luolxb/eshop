package com.soubao.jobhandler;

import com.baomidou.jobs.api.JobsResponse;
import com.baomidou.jobs.exception.JobsException;
import com.baomidou.jobs.handler.IJobsHandler;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.SmsLog;
import com.soubao.service.SmsLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MouthJobHandler implements IJobsHandler {
    @Autowired
    private SmsLogService smsLogService;

    @Override
    public JobsResponse execute(String tenantId, String param) throws JobsException {
        long time = System.currentTimeMillis() / 1000 - 2592000;
        smsLogService.remove(new QueryWrapper<SmsLog>().lt("add_time", time));//删除一个月以前的短信
        return JobsResponse.ok();
    }
}
