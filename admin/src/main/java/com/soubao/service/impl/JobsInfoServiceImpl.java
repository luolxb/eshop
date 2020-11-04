package com.soubao.service.impl;

import com.baomidou.jobs.model.JobsInfo;
import com.baomidou.jobs.service.JobsHelper;
import com.baomidou.jobs.trigger.JobsTrigger;
import com.baomidou.jobs.trigger.TriggerTypeEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.JobsInfoMapper;
import com.soubao.service.IJobsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class JobsInfoServiceImpl extends ServiceImpl<JobsInfoMapper, JobsInfo> implements IJobsInfoService {
    @Autowired
    private JobsInfoMapper jobInfoMapper;

    @Override
    public List<JobsInfo> listNextTime(long nextTime) {
        return jobInfoMapper.selectList(Wrappers.<JobsInfo>lambdaQuery()
                .eq(JobsInfo::getStatus, 0)
                .le(JobsInfo::getNextTime, nextTime));
    }

    @Override
    public boolean execute(Long id, String param) {
        return JobsTrigger.trigger(id, TriggerTypeEnum.MANUAL, -1, param);
    }

    @Override
    public boolean start(Long id) {
        JobsInfo dbJobInfo = getById(id);
        if (null == dbJobInfo) {
            return false;
        }
        JobsInfo jobsInfo = new JobsInfo();
        jobsInfo.setId(dbJobInfo.getId());
        jobsInfo.setStatus(0);
        jobsInfo.setLastTime(0L);
        Assert.fail(!JobsHelper.cronValidate(dbJobInfo.getCron()), "CRON 表达式不可用");

        // next trigger time (10s后生效，避开预读周期)
        jobsInfo.setNextTime(JobsHelper.cronNextTime(dbJobInfo.getCron()) + 10000);
        return jobInfoMapper.updateById(jobsInfo) > 0;
    }

    @Override
    public boolean stop(Long id) {
        JobsInfo jobsInfo = new JobsInfo();
        jobsInfo.setId(id);
        jobsInfo.setStatus(1);
        jobsInfo.setLastTime(0L);
        jobsInfo.setNextTime(0L);
        return jobInfoMapper.updateById(jobsInfo) > 0;
    }

    @Override
    public void addJobsInfo(JobsInfo jobInfo) {
        jobInfo.setCreateTime(System.currentTimeMillis());
        jobInfoMapper.insert(jobInfo);
    }

    @Override
    public boolean updateById(JobsInfo jobsInfo) {
        return jobInfoMapper.updateById(jobsInfo) > 0;
    }

}
