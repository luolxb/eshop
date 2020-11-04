package com.soubao.controller;

import com.soubao.common.vo.SBApi;
import com.soubao.service.FeedbackService;
import com.soubao.vo.FeedbackRq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@Api(value = "意见反馈", tags = {"意见反馈"})
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @ApiOperation(value = "新增意见反馈", notes = "新增意见反馈", httpMethod = "POST")
    @PostMapping
    public SBApi save(@Valid @RequestBody FeedbackRq feedbackRq,
                      BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new SBApi(-1,bindingResult.getFieldError().getDefaultMessage(),null);
        }
        feedbackService.saveFeedback(feedbackRq);
        return new SBApi();
    }
}
