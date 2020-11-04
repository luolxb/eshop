package com.soubao.service;

import com.baomidou.jobs.model.JobsLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.vo.JobsDateDistributionVO;

import java.util.List;

/**
 * 任务日志接口
 *
 * @author jobob
 * @since 2019-07-18
 */
public interface IJobsLogService extends IService<JobsLog> {

    /**
     * 执行成功日志记录总数
     */
    int countSuccess();

    List<JobsDateDistributionVO> getJobsDateDistributionVO();
}
