package com.soubao.service.impl;

import com.baomidou.jobs.JobsClock;
import com.baomidou.jobs.model.JobsInfo;
import com.baomidou.jobs.model.JobsLock;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.JobsInfoMapper;
import com.soubao.dao.JobsLockMapper;
import com.soubao.service.IJobsLockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JobsLockServiceImpl extends ServiceImpl<JobsLockMapper, JobsLock> implements IJobsLockService {
    @Resource
    private JobsLockMapper jobsLockMapper;

    @Override
    public int insert(String name, String owner) {
        JobsLock jobsLock = new JobsLock();
        jobsLock.setName(name);
        jobsLock.setOwner(owner);
        jobsLock.setCreateTime(JobsClock.currentTimeMillis());
        return jobsLockMapper.insert(jobsLock);
    }

    @Override
    public int delete(String name, String owner) {
        return jobsLockMapper.delete(Wrappers.<JobsLock>lambdaQuery().eq(JobsLock::getName, name)
                .eq(null != owner, JobsLock::getOwner, owner));
    }
}
