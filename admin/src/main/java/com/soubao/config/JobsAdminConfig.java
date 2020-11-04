package com.soubao.config;

import com.baomidou.jobs.handler.IJobsResultHandler;
import com.baomidou.jobs.starter.EnableJobsAdmin;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.soubao.dao.JobsLogMapper;
import com.soubao.thread.JobLogReportHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Jobs Admin 启动配置
 *
 * @author jobob
 * @since 2019-06-08
 */
@Slf4j
@EnableJobsAdmin
@Configuration
public class JobsAdminConfig implements InitializingBean, DisposableBean {

    private static JobsAdminConfig adminConfig = null;
    public static JobsAdminConfig getAdminConfig() {
        return adminConfig;
    }

    @Value("${job.logretentiondays}")
    private int logretentiondays;

    public int getLogretentiondays() {
        return logretentiondays;
    }

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 任务调度结果处理器，可用于失败报警成功通知
     *
     * @return
     */
    @Bean
    public IJobsResultHandler jobsResultHandler() {
        return (jobInfo, address, jobsResponse) ->
                log.debug("Jobs 处理器，心跳检测调度地址："
                        + address);
    }

    @Override
    public void destroy() throws Exception {
        JobLogReportHelper.getInstance().toStop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        adminConfig = this;
        JobLogReportHelper.getInstance().start();
    }

    @Resource
    private JobsLogMapper jobsLogMapper;

    public JobsLogMapper getJobsLogMapper() {
        return jobsLogMapper;
    }
}
