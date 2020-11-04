package com.soubao.service.impl;

import com.baomidou.jobs.model.JobsLog;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.JobsLogMapper;
import com.soubao.service.IJobsLogService;
import com.soubao.vo.JobsDateDistributionVO;
import com.soubao.vo.JobsDateTempVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobsLogServiceImpl extends ServiceImpl<JobsLogMapper, JobsLog> implements IJobsLogService {
    @Resource
    private JobsLogMapper jobsLogMapper;

    @Override
    public int countSuccess() {
        return jobsLogMapper.selectCount(Wrappers.<JobsLog>lambdaQuery()
                .eq(JobsLog::getTriggerCode, 0));
    }

    @Override
    public boolean updateById(JobsLog jobsLog) {
        return jobsLogMapper.updateById(jobsLog) > 0;
    }

    @Override
    public boolean save(JobsLog jobsLog) {
        return jobsLogMapper.insert(jobsLog) > 0;
    }

//    @Override
//    public R<IPage<JobsLog>> page(HttpServletRequest request, JobsLog jobsLog) {
//        QueryWrapper<JobsLog> wrapper = new QueryWrapper<>();
//        if (!StringUtils.isEmpty(request.getParameter("search"))) {
//            wrapper.like("handler",request.getParameter("search"));
//        } else if (request.getParameter("job_id") != null) {
//            wrapper.eq("job_id", request.getParameter("job_id"));
//        }
//        return R.ok(jobsLogMapper.selectPage(
//                JobsPageHelper.getPage(request), wrapper.orderByDesc("create_time")
//        ));
//        return null;
//    }

    @Override
    public List<JobsDateDistributionVO> getJobsDateDistributionVO() {
        List<JobsDateTempVO> tempVOList = jobsLogMapper.selectJobsDateTempVO();
        if (CollectionUtils.isEmpty(tempVOList)) {
            return null;
        }
        List<JobsDateDistributionVO> voList = new ArrayList<>();
        for (JobsDateTempVO tempVO : tempVOList) {
            JobsDateDistributionVO vo = new JobsDateDistributionVO();
            if (0 == tempVO.getCode()) {
                vo.setSuccessful(tempVO.getNum());
                vo.setFailed(0);
            } else {
                vo.setSuccessful(0);
                vo.setFailed(tempVO.getNum());
            }
            vo.setAtDate(tempVO.getAtDate());
            voList.add(vo);
        }
        return voList;
    }
}