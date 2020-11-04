package com.soubao.thread;

import com.baomidou.jobs.model.JobsLog;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.config.JobsAdminConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class JobLogReportHelper {

    private static JobLogReportHelper instance = new JobLogReportHelper();
    public static JobLogReportHelper getInstance(){
        return instance;
    }

    private Thread logrThread;
    private volatile boolean toStop = false;

    public void start() {
        logrThread = new Thread(new Runnable() {
            @Override
            public void run() {

                // last clean log time
                long lastCleanLogTime = 0;
                while (!toStop) {
                    //每天执行一次，删除一个月前的日志记录
                    if (System.currentTimeMillis() - lastCleanLogTime > 24*60*60*1000) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DAY_OF_MONTH, -1 * JobsAdminConfig.getAdminConfig().getLogretentiondays());
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        long clearBeforeTime = calendar.getTime().getTime();
                        Set<Long> logIds = JobsAdminConfig.getAdminConfig().getJobsLogMapper().selectList(new QueryWrapper<JobsLog>().le("create_time", clearBeforeTime))
                                .stream().map(JobsLog::getId).collect(Collectors.toSet());
                        if (logIds != null && logIds.size() > 0) {
                            JobsAdminConfig.getAdminConfig().getJobsLogMapper().deleteBatchIds(logIds);
                        }
                        // update clean time
                        lastCleanLogTime = System.currentTimeMillis();
                    }
                }
            }
        });
        logrThread.setDaemon(true);
        logrThread.setName("job, admin JobLogReportHelper");
        logrThread.start();
    }

    public void toStop(){
        toStop = true;
        // interrupt and wait
        logrThread.interrupt();
        try {
            logrThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

}
