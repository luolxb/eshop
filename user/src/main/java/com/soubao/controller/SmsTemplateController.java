package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.SmsTemplate;
import com.soubao.service.SmsTemplateService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 短信模板配置表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-06
 */
@RestController
@RequestMapping("/sms_template")
public class SmsTemplateController {

    @Autowired
    private SmsTemplateService smsTemplateService;

    @GetMapping("page")
    public IPage<SmsTemplate> getPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return smsTemplateService.page(new Page<>(page, size));
    }

    @GetMapping
    public SmsTemplate getSmsTpl(@RequestParam(value = "tpl_id", required = false) Integer tplId,
                                 @RequestParam(value = "send_scene", required = false) String sendScene) {
        if (tplId != null) {
            return smsTemplateService.getById(tplId);
        }
        if (sendScene != null) {
            return smsTemplateService.getOne(new QueryWrapper<SmsTemplate>().eq("send_scene", sendScene));
        }
        return null;
    }

    @DeleteMapping("{tpl_id}")
    public SBApi delete(@PathVariable(value = "tpl_id") Integer tplId, SBApi sbApi) {
        smsTemplateService.removeById(tplId);
        return sbApi;
    }

    @PutMapping
    public SBApi update(@Valid @RequestBody SmsTemplate smsTemplate, SBApi sbApi) {
        smsTemplateService.updateById(smsTemplate);
        return sbApi;
    }

    @PostMapping
    public SBApi save(@Valid @RequestBody SmsTemplate smsTemplate, SBApi sbApi) {
        smsTemplate.setAddTime(System.currentTimeMillis() / 1000);
        smsTemplateService.save(smsTemplate);
        return sbApi;
    }

    @GetMapping("send_scene")
    public List<Map<String, String>> getSendScene() {
        return smsTemplateService.getSendScene();
    }

}
