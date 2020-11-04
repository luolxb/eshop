package com.soubao.controller;

import com.baomidou.jobs.model.JobsLog;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.service.IJobsLogService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 日志信息
 *
 * @author jobob
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/jobs_log")
public class JobsLogController {
    @Autowired
    private IJobsLogService jobsLogService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public IPage<JobsLog> page(@RequestParam(value = "start_time", required = false) Long startTime,
                               @RequestParam(value = "end_time", required = false) Long endTime,
                               @RequestParam(value = "job_id", required = false) Long jobId,
                               @RequestParam(value = "handler", required = false) String handler,
                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<JobsLog> wrapper = new QueryWrapper<>();
        if (startTime != null && endTime != null) {
            wrapper.between("create_time", startTime, endTime);
        }
        if (null != jobId) {
            wrapper.eq("job_id", jobId);
        }
        if (!StringUtils.isEmpty(handler)) {
            wrapper.like("handler", handler);
        }
        wrapper.orderByDesc("create_time");
        return jobsLogService.page(new Page<>(page, size), wrapper);
    }

    /**
     * 总执行次数
     */
    @GetMapping("/count")
    public int count() {
        return jobsLogService.count();
    }

    /**
     * 总执行成功次数
     */
    @GetMapping("/count/success")
    public Integer countSuccess() {
        return jobsLogService.countSuccess();
    }

    /**
     * 根据id批量删除日志
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public SBApi deleteLog(@PathVariable("id") Set<Long> id) {
        jobsLogService.removeByIds(id);
        return new SBApi();
    }
}
