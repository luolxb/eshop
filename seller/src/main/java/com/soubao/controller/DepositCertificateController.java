package com.soubao.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.common.vo.SBApi;
import com.soubao.entity.*;
import com.soubao.service.DepositCertificateService;

import com.alibaba.fastjson.JSONObject;
import com.soubao.service.MallService;
import com.soubao.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 通证业务 前端控制器
 * </p>
 *
 * @author
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/depositCertificate")
public class DepositCertificateController {


    @Autowired
    private DepositCertificateService depositCertificateService;

    @Autowired
    private SellerService sellerService;

    //@PreAuthorize("hasAnyRole('SELLER')")
    @PostMapping("/insertIn")
    public SBApi addDepositCertificate(@RequestBody JSONObject depositCertificateInfo) {

        DepositCertificate depositCertificate = (DepositCertificate) JSONObject.toJavaObject(depositCertificateInfo,DepositCertificate.class);
        Seller seller = sellerService.getOne(new QueryWrapper<Seller>().eq("group_id", depositCertificate.getUserId()));
        if (null != depositCertificate) {
            if(null != seller) {
                depositCertificate.setSellerId(Long.parseLong(seller.getSellerId() + ""));
            }
            depositCertificate.setSaleStatus(false);
            depositCertificate.setSendStatus(false);
            depositCertificateService.save(depositCertificate);
        } else {

            throw new ShopException(ResultEnum.UNKNOWN_ERROR);
        }
        return new SBApi();
    }

    //@PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("/getOne")
    public DepositCertificate getDepositCertificateInfoById(@RequestParam Integer dcId) {
        DepositCertificate dc= depositCertificateService.getById(dcId);
        return dc;
    }

    /**
     * 根据商家id获取已发布的存证
     * @param sellerId
     * @return
     */
    @GetMapping("list/on")
    public List<DepositCertificate>  getDepositCertificateBySellerId(@RequestParam(value = "seller_id") Integer sellerId) {
        return depositCertificateService.list(new QueryWrapper<DepositCertificate>().eq("seller_id",sellerId).isNotNull("goods_id"));
    }

}
