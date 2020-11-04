package com.soubao.controller;


import com.soubao.entity.AdPosition;
import com.soubao.service.AdPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-02-27
 */
@RestController
@RequestMapping("/ad_position")
public class AdPositionController {
    @Autowired
    private AdPositionService adPositionService;

    @RequestMapping("list")
    public List<AdPosition> list(){
        return adPositionService.list();
    }
}
