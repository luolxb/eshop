package com.soubao.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.StoreGrade;
import com.soubao.service.StoreGradeService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/store_grade")
public class StoreGradeController {
    @Autowired private StoreGradeService storeGradeService;

    @GetMapping("list")
    public List<StoreGrade> list() {
        return storeGradeService.list();
    }

    @GetMapping("page")
    public IPage<StoreGrade> page(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return storeGradeService.page(new Page<>(page, size));
    }

    @GetMapping
    public StoreGrade storeGrade(@RequestParam("sg_id") Integer gradeId) {
        return storeGradeService.getById(gradeId);
    }

    @PostMapping
    public SBApi insert(@Valid @RequestBody StoreGrade storeGrade) {
        storeGradeService.insert(storeGrade);
        return new SBApi();
    }

    @PutMapping
    public SBApi update(@Valid @RequestBody StoreGrade storeGrade) {
        storeGradeService.update(storeGrade);
        return new SBApi();
    }

    @DeleteMapping
    public SBApi remove(@RequestParam("sg_id") Integer gradeId) {
        storeGradeService.remove(gradeId);
        return new SBApi();
    }
}
