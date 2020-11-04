package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.ComplainSubject;
import com.soubao.service.ComplainSubjectService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 投诉主题表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-02-28
 */
@RestController
@RequestMapping("complain_subject")
public class ComplainSubjectController {

    @Autowired
    private ComplainSubjectService complainSubjectService;

    @GetMapping("page")
    public IPage<ComplainSubject> getPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return complainSubjectService.page(new Page<>(page, size),
                new QueryWrapper<ComplainSubject>().eq("subject_state", 1));
    }

    @PostMapping
    public SBApi save(@Valid @RequestBody ComplainSubject complainSubject) {
        complainSubjectService.saveComplainSubject(complainSubject);
        return new SBApi();
    }

    @DeleteMapping("{subject_id}")
    public SBApi remove(@PathVariable("subject_id") Integer subjectId) {
        complainSubjectService.update(new UpdateWrapper<ComplainSubject>()
                .set("subject_state", 2).eq("subject_id", subjectId));
        return new SBApi();
    }

}
