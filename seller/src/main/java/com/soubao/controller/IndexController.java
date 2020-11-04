package com.soubao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.common.vo.SBApi;
import com.soubao.entity.DcGoodInfo;
import com.soubao.entity.DepositCertificate;
import com.soubao.entity.Goods;
import com.soubao.entity.Seller;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.AuthService;
import com.soubao.service.DepositCertificateService;
import com.soubao.service.MallService;
import com.soubao.service.SellerService;
import com.soubao.common.utils.ShopStringUtil;
import com.soubao.service.impl.AuthenticationFacade;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@RestController
@Slf4j
public class IndexController {

    @Value("${security.oauth2.client.scope}")
    private String scope;
    @Value("${security.oauth2.client.id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;
    @Autowired
    private AuthService authService;
    @Autowired
    private SellerService sellerService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private DepositCertificateService depositCertificateService;

    @Autowired
    private MallService mallService;

    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    @GetMapping("/index")
    public String index() {

        System.out.println("IndexController ==> index");
        return "你好~";
    }

    @PostMapping("/login")
    public String login(@RequestBody Seller loginSeller) {
        if (!authService.verify(loginSeller.getVerification())) {
            throw new ShopException(ResultEnum.VERIFY_ERROR);
        }
        String resultStr = authService.getToken("password", scope, clientId,
                clientSecret, "{seller}" + loginSeller.getSellerName(), loginSeller.getPassword());
        sellerService.update((new UpdateWrapper<Seller>()).set("last_login_time", System.currentTimeMillis() / 1000)
                .eq("seller_name", loginSeller.getSellerName()));
        return resultStr;
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("/depositCertificate/list")
    public IPage<DepositCertificate> getDepositCertificateList(@RequestParam(value = "store_id", required = true) Integer store_id,
                                                               @RequestParam(value = "p", defaultValue = "1") Integer page,
                                                               @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Seller seller = sellerService.getOne(new QueryWrapper<Seller>().eq("store_id", store_id));
        if (null == seller)
            seller = authenticationFacade.getPrincipal(Seller.class);
        IPage<DepositCertificate> depositCertificatePage = null;
        depositCertificatePage = depositCertificateService.page(new Page<>(page, size), new QueryWrapper<DepositCertificate>().eq("user_id", seller.getGroupId()));
        return depositCertificatePage;
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PostMapping("/depositCertificate/addGoods")
    public SBApi addGoods(@RequestBody Goods goods) {
        SBApi sbApi = new SBApi();
        // 判断存证是否发布
        Goods goods1 = mallService.goodsByDcId(goods.getDepositCertificateId());
        if (goods1 != null) {
            sbApi.setStatus(400);
            sbApi.setMsg("商品已发布，请勿重新发布");
            return sbApi;
        }

        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        goods.setGoodsCount(1);//这里强制库存数为1;
        goods.setExchangeIntegral(0);//积分强制设置为0
        goods.setIsOnSale(1);//积分强制设置为0
        goods.setStoreCount(1);
        goods.setGoodsContent(goods.getGoodsName());
        goods.setUserId(seller.getUserId());//首次发售的持有者
        goods.setOwnerId(seller.getUserId());
        goods.setDcId(goods.getDepositCertificateId());
        Integer goodsId = mallService.addGoods1(goods);
        DepositCertificate depositCertificate = depositCertificateService.getById(goods.getDepositCertificateId());
        depositCertificate.setSaleStatus(true);
        depositCertificate.setShopPrice(goods.getShopPrice());
        depositCertificate.setOwnerId(seller.getUserId());
        depositCertificate.setGoodsId(goodsId);
        depositCertificateService.updateById(depositCertificate);
        sbApi.setResult(goods);
        return sbApi;
    }

//    @PreAuthorize("hasAnyRole('SELLER')")
//    @PostMapping("/depositCertificate/modifyGoods")
//    public SBApi modifyGoods(@RequestBody Goods goods) {
//        List<Goods> goodsList = new ArrayList<>();
//        goodsList.add(goods);
//        return mallService.updateGoods(goodsList,new SBApi());
//    }
//
//    @PreAuthorize("hasAnyRole('SELLER')")
//    @PostMapping("/depositCertificate/listGoods")
//    public Goods listGoods(@RequestParam Integer goodsId) {
//        return mallService.getGoods(goodsId);
//    }


}
