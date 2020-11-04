package com.soubao.controller;

import com.soubao.service.IJobsStatisticsService;
import com.soubao.vo.JobsDateDistributionVO;
import com.soubao.vo.JobsImportantNumVO;
import com.soubao.vo.JobsSuccessRatioVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计信息
 *
 * @author jobob
 * @since 2019-06-15
 */
@RestController
@RequestMapping("/jobs_statistics")
public class JobsStatisticsController {
    @Autowired
    private IJobsStatisticsService statisticsService;

    /**
     * 重要参数数量
     */
    @GetMapping("/important_num")
    public JobsImportantNumVO importantNum() {
        return statisticsService.getImportantNum();
    }

    /**
     * 成功比例
     */
    @GetMapping("/success_ratio")
    public JobsSuccessRatioVO successRatio() {
        return statisticsService.getSuccessRatio();
    }

    /**
     * 日期分布图
     */
    @GetMapping("/date_distribution")
    public List<JobsDateDistributionVO> dateDistribution() {
        return statisticsService.getDateDistribution();
    }
}
