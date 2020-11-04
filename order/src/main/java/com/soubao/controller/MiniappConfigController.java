package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.entity.MiniappConfig;
import com.soubao.service.MiniappConfigService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dyr
 * @since 2019-12-03
 */
@RestController
@RequestMapping("/miniapp_config")
public class MiniappConfigController {
    @Autowired
    private MiniappConfigService miniappConfigService;

    @GetMapping
    public MiniappConfig getConfig(){
        return miniappConfigService.getOne((new QueryWrapper<MiniappConfig>()).last("limit 1"));
    }

    @PostMapping
    public SBApi saveConfig(@RequestBody MiniappConfig requestMiniappConfig){
        MiniappConfig miniappConfig = miniappConfigService.getOne((new QueryWrapper<MiniappConfig>()).last("limit 1"));
        if(miniappConfig != null){
            miniappConfigService.remove((new QueryWrapper<>()));
        }
        miniappConfigService.save(requestMiniappConfig);
        return new SBApi();
    }
}
