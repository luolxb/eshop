package com.soubao.controller;

import com.baomidou.jobs.model.JobsRegistry;
import com.baomidou.mybatisplus.extension.api.R;
import com.soubao.service.IJobsRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息
 *
 * @author jobob
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/jobs_registry")
public class JobsRegistryController {
    @Autowired
    private IJobsRegistryService jobRegistryService;

    /**
     * 分页
     */
    @GetMapping("/page")
    public R<Object> page(JobsRegistry jobRegistry) {
        return null;//jobRegistryService.page(request, jobRegistry));
    }
}
