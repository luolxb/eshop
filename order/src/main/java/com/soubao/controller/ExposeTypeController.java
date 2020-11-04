package com.soubao.controller;


import com.soubao.entity.ExposeType;
import com.soubao.service.ExposeTypeService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 举报类型表 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-02
 */
@RestController
@RequestMapping("expose_type")
public class ExposeTypeController {

    @Autowired
    private ExposeTypeService exposeTypeService;

    @GetMapping("list")
    public List<ExposeType> getExposeTypeList() {
        return exposeTypeService.list();
    }

    @PostMapping
    public SBApi complainSubject(@Valid @RequestBody ExposeType exposeType) {
        exposeType.setExposeTypeState(1);
        exposeTypeService.save(exposeType);
        return new SBApi();
    }

    @DeleteMapping("{type_id}")
    public SBApi deleteComplainSubject(@PathVariable("type_id") Integer typeId) {
        exposeTypeService.removeById(typeId);
        return new SBApi();
    }

}
