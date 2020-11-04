package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.ComplainTalk;
import com.soubao.service.ComplainTalkService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 投诉对话表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-02-29
 */
@RestController
@RequestMapping("/complain_talk")
public class ComplainTalkController {

    @Autowired
    private ComplainTalkService complainTalkService;

    @GetMapping("/list")
    public List<ComplainTalk> getComplainTalkList(@RequestParam(value = "complain_id") Integer complainId) {
        return complainTalkService.list(new QueryWrapper<ComplainTalk>()
                .eq("complain_id", complainId).orderByAsc("talk_time"));
    }

    @PutMapping
    public SBApi update(@RequestBody ComplainTalk complainTalk) {
        complainTalk.setTalkState(2);
        complainTalkService.updateById(complainTalk);
        return new SBApi();
    }

    @PostMapping
    public SBApi save(@RequestBody ComplainTalk complainTalk) {
        complainTalkService.save(complainTalk);
        return new SBApi();
    }

}
