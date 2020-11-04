package com.soubao.controller;

import com.baomidou.jobs.model.JobsInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.service.IJobsInfoService;
import com.soubao.common.vo.SBApi;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 任务信息
 *
 * @author jobob
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/jobs_info")
public class JobsInfoController {
    @Resource
    private IJobsInfoService jobInfoService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public IPage<JobsInfo> page(@RequestParam(value = "app", required = false) String app,
                                @RequestParam(value = "handler", required = false) String handler,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        QueryWrapper<JobsInfo> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(app)) {
            wrapper.like("app", app);
        }
        if (!StringUtils.isEmpty(handler)) {
            wrapper.like("handler", handler);
        }
        return jobInfoService.page(new Page<>(page, size), wrapper);
    }

    /**
     * 总任务数
     */
    @GetMapping("/count")
    public int count() {
        return jobInfoService.count();
    }

    /**
     * 执行
     */
    @PostMapping("/execute/{id}")
    public SBApi execute(@PathVariable("id") Long id, @RequestParam(value = "param", required = false) String param) {
        jobInfoService.execute(id, param);
        return new SBApi();
    }

    /**
     * 启动
     */
    @PostMapping("/start/{id}")
    public SBApi start(@PathVariable("id") Long id) {
        jobInfoService.start(id);
        return new SBApi();
    }

    /**
     * 停止
     */
    @PostMapping("/stop/{id}")
    public SBApi stop(@PathVariable("id") Long id) {
        jobInfoService.stop(id);
        return new SBApi();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public SBApi remove(@PathVariable("id") Long id) {
        jobInfoService.removeById(id);
        return new SBApi();
    }

    /**
     * 新增
     * @param jobsInfo
     * @return
     */
    @PostMapping
    public SBApi add(@RequestBody JobsInfo jobsInfo) {
        jobInfoService.addJobsInfo(jobsInfo);
        return new SBApi();
    }

    @PutMapping
    public SBApi update(@RequestBody JobsInfo jobsInfo) {
        jobsInfo.setUpdateTime(System.currentTimeMillis());
        jobInfoService.updateById(jobsInfo);
        return new SBApi();
    }

    @GetMapping("/{id}")
    public JobsInfo getJobsInfo(@PathVariable("id") Long id) {
        return jobInfoService.getById(id);
    }

}
