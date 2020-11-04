package com.soubao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.AdMapper;
import com.soubao.entity.Ad;
import com.soubao.entity.AdPosition;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.AdPositionService;
import com.soubao.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service("adService")
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {
    @Autowired
    private AdPositionService adPositionService;

    @Override
    public void withADPosition(List<Ad> ads) {
        Set<Integer> pids = ads.stream().map(Ad::getPid).collect(Collectors.toSet());
        if (!pids.isEmpty()){
            Map<Integer, AdPosition> adPositionMap = adPositionService.listByIds(pids).stream()
                    .collect(Collectors.toMap(AdPosition::getPositionId, adPosition -> adPosition));
            for (Ad ad : ads) {
                ad.setAdPosition(adPositionMap.get(ad.getPid()));
            }
        }
    }

    @Override
    public void updateAd(Ad ad) {
        if (ad.getEndTime() < ad.getStartTime()){
            throw new ShopException(ResultEnum.START_TIME_GT_END_TIME);
        }
        this.updateById(ad);
    }
}
