package com.soubao.controller;


import com.soubao.entity.DistributLevel;
import com.soubao.service.DistributLevelService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("distribut/level")
public class DistributLevelController {
    @Autowired
    private DistributLevelService distributLevelService;


    @GetMapping("list")
    public List<DistributLevel> listDistributLevel() {
        return distributLevelService.list();
    }

    @PostMapping
    public SBApi addDistributLevel(@Valid @RequestBody DistributLevel distributLevel) {
        distributLevelService.save(distributLevel);
        return new SBApi();
    }

    @PutMapping
    public SBApi updateDistributLevel(@Valid @RequestBody DistributLevel distributLevel) {
        distributLevelService.updateById(distributLevel);
        return new SBApi();
    }

    @GetMapping
    public DistributLevel getDistributLevel(@RequestParam(value = "level_id") Integer levelId) {
        return distributLevelService.getById(levelId);
    }

}
