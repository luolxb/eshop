package com.soubao.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.ExposeSubject;
import com.soubao.entity.ExposeType;
import com.soubao.service.ExposeSubjectService;
import com.soubao.service.ExposeTypeService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 举报主题表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-02
 */
@RestController
@RequestMapping("expose_subject")
public class ExposeSubjectController {

    @Autowired
    private ExposeSubjectService exposeSubjectService;
    @Autowired
    private ExposeTypeService exposeTypeService;

    @GetMapping("page")
    public IPage<ExposeSubject> getComplainSubject(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return exposeSubjectService.page(new Page<>(page, size));
    }

    @PostMapping
    public SBApi complainSubject(@Valid @RequestBody ExposeSubject exposeSubject) {
        ExposeType exposeType = exposeTypeService.getById(exposeSubject.getExposeSubjectTypeId());
        exposeSubject.setExposeSubjectTypeName(exposeType.getExposeTypeName());
        exposeSubject.setExposeSubjectState(1);
        exposeSubjectService.save(exposeSubject);
        return new SBApi();
    }

    @DeleteMapping("{subject_id}")
    public SBApi deleteComplainSubject(@PathVariable("subject_id") Integer subjectId) {
        exposeSubjectService.removeById(subjectId);
        return new SBApi();
    }

}
