package com.soubao.service;


import com.soubao.vo.JobsDateDistributionVO;
import com.soubao.vo.JobsImportantNumVO;
import com.soubao.vo.JobsSuccessRatioVO;

import java.util.List;

/**
 * 统计接口
 */
public interface IJobsStatisticsService {

    /**
     * 重要数量统计
     */
    JobsImportantNumVO getImportantNum();

    /**
     * 成功比例统计
     */
    JobsSuccessRatioVO getSuccessRatio();

    /**
     * 日期分布统计
     */
    List<JobsDateDistributionVO> getDateDistribution();

}
