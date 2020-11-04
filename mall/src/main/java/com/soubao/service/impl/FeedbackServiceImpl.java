package com.soubao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.FeedbackMapper;
import com.soubao.entity.Feedback;
import com.soubao.entity.User;
import com.soubao.service.FeedbackService;
import com.soubao.vo.FeedbackRq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/***
 * 意见反馈服务层
 */
@Service
@Slf4j
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper,Feedback> implements FeedbackService {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public void saveFeedback(FeedbackRq feedbackRq) {
        User user = authenticationFacade.getPrincipal(User.class);
        feedbackRq.setUserId(user.getUserId());
        feedbackRq.setCreateTime(new Date());
        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(feedbackRq,feedback);
        this.save(feedback);
    }
}
