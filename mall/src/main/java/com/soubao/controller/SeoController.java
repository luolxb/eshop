package com.soubao.controller;

import com.soubao.entity.Seo;
import com.soubao.service.SeoService;
import com.soubao.common.vo.SBApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "SEO控制器", tags = {"SEO相关接口"})
@RestController
@RequestMapping("seo")
public class SeoController {
    @Autowired
    private SeoService seoService;

    @GetMapping("list")
    public List<Seo> list() {
        return seoService.list();
    }

    @PutMapping
    public SBApi updateSeo(@ApiParam("SEO对象") @RequestBody List<Seo> seos) {
        seoService.updateSeo(seos);
        return new SBApi();
    }

}
