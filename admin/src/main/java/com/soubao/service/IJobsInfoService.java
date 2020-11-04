package com.soubao.service;

import com.baomidou.jobs.model.JobsInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.AdminLog;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 任务信息接口
 *
 * @author jobob
 * @since 2019-07-18
 */
public interface IJobsInfoService extends IService<JobsInfo> {

    List<JobsInfo> listNextTime(long nextTime);

    /**
     * 执行、指定 ID 任务
     *
     * @param id    主键 ID
     * @param param 执行参数
     * @return
     */
    boolean execute(Long id, String param);

    /**
     * 启动、指定 ID 任务
     *
     * @param id 主键 ID
     * @return
     */
    boolean start(Long id);

    /**
     * 停止、指定 ID 任务
     *
     * @param id 主键 ID
     * @return
     */
    boolean stop(Long id);

    void addJobsInfo(JobsInfo jobsInfo);
}
