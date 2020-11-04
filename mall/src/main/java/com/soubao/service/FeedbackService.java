package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Feedback;
import com.soubao.vo.FeedbackRq;

public interface FeedbackService extends IService<Feedback> {

    void saveFeedback(FeedbackRq feedbackRq);
}
