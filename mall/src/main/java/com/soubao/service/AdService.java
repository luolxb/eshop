package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.Ad;

import java.util.List;

public interface AdService extends IService<Ad> {

    void withADPosition(List<Ad> ads);

    void updateAd(Ad ad);

}
