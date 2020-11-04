package com.soubao.service.impl;

import com.soubao.service.IJobsInfoService;
import com.soubao.service.IJobsLogService;
import com.soubao.service.IJobsRegistryService;
import com.soubao.service.IJobsStatisticsService;
import com.soubao.vo.JobsDateDistributionVO;
import com.soubao.vo.JobsImportantNumVO;
import com.soubao.vo.JobsSuccessRatioVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobsStatisticsServiceImpl implements IJobsStatisticsService {
    @Autowired
    private IJobsInfoService jobsInfoService;
    @Autowired
    private IJobsLogService jobsLogService;
    @Autowired
    private IJobsRegistryService jobsRegistryService;

    @Override
    public JobsImportantNumVO getImportantNum() {
        JobsImportantNumVO vo = new JobsImportantNumVO();
        vo.setRunTaskNum(jobsRegistryService.countAll());
        vo.setTriggeredNum(jobsLogService.count());
        vo.setOnlineExecutorNum(jobsRegistryService.countOnline());
        return vo;
    }

    @Override
    public JobsSuccessRatioVO getSuccessRatio() {
        JobsSuccessRatioVO vo = new JobsSuccessRatioVO();
        vo.setSuccessful(jobsLogService.countSuccess());
        vo.setFailed(jobsLogService.count() - vo.getSuccessful());
        return vo;
    }

    @Override
    public List<JobsDateDistributionVO> getDateDistribution() {
        return jobsLogService.getJobsDateDistributionVO();
    }
}
