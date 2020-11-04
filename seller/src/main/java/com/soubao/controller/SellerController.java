package com.soubao.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import com.soubao.service.impl.AuthenticationFacade;
import com.soubao.common.vo.SBApi;
import com.soubao.vo.StoreAddVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 卖家用户表 前端控制器
 * </p>
 *
 * @author dyrdd
 * @since 2019-11-14
 */
@Slf4j
@RestController
public class SellerController {

    @Autowired
    private SellerService sellerService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private StoreBindClassService storeBindClassService;
    @Autowired
    private UserService userService;
    @Autowired
    private DepositCertificateService depositCertificateService;

    @Autowired
    private UserSellerStoreService userSellerStoreService;

    @Value("${security.salt}")
    private String salt;

    @Autowired
    private StoreService storeService;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //这里是认证服务要用用户信息，不要乱修改
    @GetMapping("/credential")
    @ApiOperation(value = "获取用户凭证")
    public Seller credential(@RequestParam(value = "seller_name", required = false) String sellerName,
                             @RequestParam(value = "user_id", required = false) Integer userId) {
        QueryWrapper<Seller> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("seller_id,seller_name,user_id,store_id");
        if (null != sellerName)
            queryWrapper.eq("seller_name", sellerName);
        if (null != userId)
            queryWrapper.eq("user_id", userId);
        return sellerService.getOne(queryWrapper);
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("/current")
    public Seller getSelf() {
        return sellerService.getById((authenticationFacade.getPrincipal(Seller.class)).getSellerId());
    }

    @GetMapping("/list")
    public List<Seller> sellers(@RequestParam(value = "seller_ids", required = false) Set<Integer> sellerIds) {
        if (sellerIds.isEmpty()) {
            return sellerService.list();
        }
        return sellerService.list(new QueryWrapper<Seller>().in("seller_id", sellerIds));
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PutMapping("/open_teach")
    public SBApi updateOpenTeach(@RequestParam("open_teach") Integer openTeach) {
        sellerService.update(new UpdateWrapper<Seller>().set("open_teach", openTeach)
                .eq("seller_id", getSelf().getSellerId()));
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @GetMapping("/seller/page")
    public Page<Seller> sellerPage(@RequestParam(value = "seller_name", required = false) String sellerName,
                                   @RequestParam(value = "p", defaultValue = "1") Integer page,
                                   @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Seller seller = authenticationFacade.getPrincipal(Seller.class);
        QueryWrapper<Seller> sellerQueryWrapper = new QueryWrapper<>();
        sellerQueryWrapper.select("seller_id,seller_name,add_time,enabled,is_admin,user_id");
        sellerQueryWrapper.eq("store_id", seller.getStoreId());
        if (!StringUtils.isEmpty(sellerName)) {
            sellerQueryWrapper.like("seller_name", sellerName);
        }
        return (Page<Seller>) sellerService.page((new Page<>(page, size)), sellerQueryWrapper);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/seller/list")
    public Page<Seller> getSellerList(@RequestParam(value = "seller_name", required = false) String sellerName,
                                      @RequestParam(value = "p", defaultValue = "1") Integer page,
                                      @RequestParam(value = "size", defaultValue = "20") Integer size) {
        QueryWrapper<Seller> sellerQueryWrapper = new QueryWrapper<>();
        sellerQueryWrapper.select("seller_id,seller_name,add_time,enabled,is_admin,user_id");
        if (!StringUtils.isEmpty(sellerName)) {
            sellerQueryWrapper.like("seller_name", sellerName);
        }
        return (Page<Seller>) sellerService.page((new Page<>(page, size)), sellerQueryWrapper);
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PostMapping("/seller")
    public SBApi addSeller(@RequestBody Seller seller) {
        if (sellerService.count(new QueryWrapper<Seller>().eq("seller_name", seller.getSellerName())) > 0) {
            throw new ShopException(ResultEnum.SAME_SELLER_NAME);
        }
        User user = userService.getUserByMobileOrEmail(seller.getMobile(), seller.getEmail());
        if (user != null) {
            if (!passwordEncoder.matches(seller.getPassword(), user.getPassword().replace(salt, ""))) {
                //登陆密码错误
                throw new ShopException(ResultEnum.USER_PWD_ERROR);
            }
            if (sellerService.count(new QueryWrapper<Seller>().eq("user_id", user.getUserId())) > 0) {
                //该用户已经添加过店铺管理员
                throw new ShopException(ResultEnum.SELLER_USER_EXISTS);
            }
            seller.setAddTime(System.currentTimeMillis() / 1000);
            seller.setUserId(user.getUserId());
            sellerService.save(seller);
        } else {
            //请先注册前台会员
            throw new ShopException(ResultEnum.USER_NOT_EXISTS);
        }
        return new SBApi();
    }

    @PostMapping("member/add")
    public SBApi addSellerByInner(@RequestBody final DepositCertificatePublisher publisher) {
        SBApi sbApi = new SBApi();

        System.out.println(publisher.getNikeName() + "," + publisher.getMobile());
        if (sellerService.count(new QueryWrapper<Seller>().eq("seller_name", publisher.getNikeName())) > 0) {
            throw new ShopException(ResultEnum.SAME_SELLER_NAME);
        }
        String nikeName = publisher.getNikeName();
        String mobile = publisher.getMobile();
        if (1 == userService.checkUserByNickname(nikeName) ||
                1 == userService.checkUserByMobile(mobile)) {
            throw new ShopException(ResultEnum.SAME_USER_NAME);
        }

        String email_ = null;
        if (org.apache.commons.lang.StringUtils.isEmpty(publisher.getEmail())) {
            StringBuffer email = new StringBuffer();
            email.append("nts").append(publisher.getUserId()).append("@ntsitech.com");
            email_ = email.toString();
        } else {
            email_ = publisher.getEmail();
        }

        String mobile_ = null;
        if (org.apache.commons.lang.StringUtils.isEmpty(publisher.getMobile())) {
            Long phone = 13800000000L + Long.parseLong(publisher.getUserId() + "");
            mobile_ = phone.toString();
        } else {
            mobile_ = publisher.getMobile();
        }
        ;
        StoreAddVo storeAddVo = new StoreAddVo();
        storeAddVo.setMobile(mobile_);
        storeAddVo.setEmail(email_);
        storeAddVo.setIsOwnShop(1);
        storeAddVo.setPassword(publisher.getPassword());
        storeAddVo.setStoreName(publisher.getNikeName());
        storeAddVo.setSellerName(publisher.getNikeName());
        Integer storeId = storeService.addStore(storeAddVo);//这里头除了创建商铺外，会去根据判断创建用户和商铺管理员
        storeBindGoodCategory(storeId);//绑定商品类型
        Store store = storeService.getById(storeId);
        Seller seller = sellerService.getOne(new QueryWrapper<Seller>().eq("user_id", store.getUserId()));
        seller.setGroupId(publisher.getUserId());
        sellerService.updateById(seller);
        return new SBApi();

    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @PutMapping("/seller")
    public SBApi updateSeller(@RequestBody Seller seller) {
        User user = userService.getUserById(seller.getUserId());
        if (null != user) {
            if (!passwordEncoder.matches(seller.getPassword(), user.getPassword().replace(salt, ""))) {
                //登陆密码错误
                throw new ShopException(ResultEnum.USER_PWD_ERROR);
            }
            if (!seller.getNewPwd().equals(seller.getCheckNewPwd())) {
                throw new ShopException(ResultEnum.USER_PWD_NOT_SAME);
            }
            user.setPassword(salt + passwordEncoder.encode(seller.getNewPwd()));
            userService.updateUser(user);
            sellerService.updateById(seller);
        } else {
            throw new ShopException(ResultEnum.ACCOUNT_NOT_EXISTS);
        }
        return new SBApi();
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @GetMapping("/seller/{seller_id}")
    public Seller getSeller(@PathVariable(value = "seller_id") Integer sellerId) {
        return sellerService.getById(sellerId);
    }

    @PreAuthorize("hasAnyRole('SELLER')")
    @DeleteMapping("/seller/assign")
    public SBApi deleteSeller(@PathVariable(value = "seller_id") Integer sellerId) {
        sellerService.removeById(sellerId);
        return new SBApi();
    }

    @GetMapping("/update_depositCertificate")
    public SBApi updateDepositCertificateStatus(@RequestParam(value = "user_id", required = true) Integer userId,
                                                @RequestParam(value = "dc_id", required = true) Integer dcId,
                                                @RequestParam(value = "sale_status", required = false) Integer saleStatus,
                                                @RequestParam(value = "send_status", required = false) Integer sendStatus) {

        DepositCertificate depositCertificate = depositCertificateService.getOne(new QueryWrapper<DepositCertificate>().eq("id", dcId));
        if (depositCertificate != null) {
            if (saleStatus != null)
                depositCertificate.setSaleStatus(saleStatus == 1 ? true : false);
            if (sendStatus != null)
                depositCertificate.setSendStatus(sendStatus == 1 ? true : false);
            depositCertificate.setOwnerId(userId);
            depositCertificateService.updateById(depositCertificate);
        } else {
            throw new ShopException(ResultEnum.UNKNOWN_ERROR);
        }


        return new SBApi();
    }

    @GetMapping("/depositCertificate/user_id")
    public List<DepositCertificate> getDepositCertificateByUserId(@RequestParam("user_id") Integer userId) {
        QueryWrapper<DepositCertificate> wrapper = new QueryWrapper<DepositCertificate>();
        wrapper.eq("owner_id", userId);
        return depositCertificateService.list(wrapper);
    }


    private void storeBindGoodCategory(Integer storeId) {
        StoreBindClass storeBindClass1 = new StoreBindClass();
        storeBindClass1.setClass1(188);
        storeBindClass1.setClass2(183);
        storeBindClass1.setClass3(189);
        storeBindClass1.setCommisRate(0);
        storeBindClass1.setState(1);
        storeBindClass1.setStoreId(storeId);
        storeBindClassService.save(storeBindClass1);
        StoreBindClass storeBindClass2 = new StoreBindClass();
        storeBindClass2.setClass1(188);
        storeBindClass2.setClass2(183);
        storeBindClass2.setClass3(190);
        storeBindClass2.setCommisRate(0);
        storeBindClass2.setState(1);
        storeBindClass2.setStoreId(storeId);
        storeBindClassService.save(storeBindClass2);

        StoreBindClass storeBindClass3 = new StoreBindClass();
        storeBindClass3.setClass1(188);
        storeBindClass3.setClass2(184);
        storeBindClass3.setClass3(191);
        storeBindClass3.setCommisRate(0);
        storeBindClass3.setState(1);
        storeBindClass3.setStoreId(storeId);
        storeBindClassService.save(storeBindClass3);

        StoreBindClass storeBindClass4 = new StoreBindClass();
        storeBindClass4.setClass1(188);
        storeBindClass4.setClass2(184);
        storeBindClass4.setClass3(192);
        storeBindClass4.setCommisRate(0);
        storeBindClass4.setState(1);
        storeBindClass4.setStoreId(storeId);
        storeBindClassService.save(storeBindClass4);

        StoreBindClass storeBindClass5 = new StoreBindClass();
        storeBindClass5.setClass1(188);
        storeBindClass5.setClass2(185);
        storeBindClass5.setClass3(193);
        storeBindClass5.setCommisRate(0);
        storeBindClass5.setState(1);
        storeBindClass5.setStoreId(storeId);
        storeBindClassService.save(storeBindClass5);

        StoreBindClass storeBindClass6 = new StoreBindClass();
        storeBindClass6.setClass1(188);
        storeBindClass6.setClass2(185);
        storeBindClass6.setClass3(194);
        storeBindClass6.setCommisRate(0);
        storeBindClass6.setState(1);
        storeBindClass6.setStoreId(storeId);
        storeBindClassService.save(storeBindClass6);
    }


    /**
     * 根据userId获取商家商铺
     *
     * @param userId
     * @return
     */
    @GetMapping("stores/userId")
    Store getStoresByUserId(@RequestParam("user_id") Integer userId){
        Seller seller = sellerService.getOne(new QueryWrapper<Seller>().eq("user_id", userId));
        return storeService.getById(seller.getStoreId());
    }

    @GetMapping("/store/sellerId")
    Store storeSellerId(@RequestParam("seller_id") Integer sellerId) {
        Seller seller = sellerService.getById(sellerId);
        return storeService.getById(seller.getStoreId());
    }

    @GetMapping("/seller/storeId")
    Seller getSellerByStoreId(@RequestParam("store_id") Integer storeId) {
        return sellerService.getOne(new QueryWrapper<Seller>().eq("store_id",storeId));
    }


    /**
     * 根据 storeId  获取userId
     * @param storeId
     * @return
     */
    @GetMapping("/user/storeId")
    Integer getUserByStoreId(@RequestParam(value = "store_id") Integer storeId) {
        // 查询是否为商家
        Seller seller = sellerService.getOne(new QueryWrapper<Seller>().eq("store_id", storeId));
        if (seller != null) {
            return seller.getUserId();
        }
        return userSellerStoreService.getOne(new QueryWrapper<UserSellerStore>().eq("store_id", storeId)).getUserId();
    }


}
