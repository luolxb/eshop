package com.soubao.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.StoreMsgTpl;
import com.soubao.service.StoreMsgTplService;
import com.soubao.common.vo.SBApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 商家消息模板 前端控制器
 * </p>
 *
 * @author dyr
 * @since 2020-03-06
 */
@RestController
@RequestMapping("/store_msg_tpl")
public class StoreMsgTplController {

    @Autowired
    private StoreMsgTplService storeMsgTplService;

    @GetMapping("list")
    public List<StoreMsgTpl> getList() {
        return storeMsgTplService.list();
    }

    @GetMapping("page")
    public IPage<StoreMsgTpl> getPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return storeMsgTplService.page(new Page<>(page, size));
    }

    @GetMapping
    public StoreMsgTpl getStoreMsgTpl(@RequestParam(value = "smt_code") String smtCode) {
        return storeMsgTplService.getOne(new QueryWrapper<StoreMsgTpl>().eq("smt_code", smtCode));
    }

    @PutMapping
    public SBApi updateStoreMsgTpl(@Valid @RequestBody StoreMsgTpl storeMsgTpl) {
        storeMsgTplService.updateById(storeMsgTpl);
        return new SBApi();
    }

}
