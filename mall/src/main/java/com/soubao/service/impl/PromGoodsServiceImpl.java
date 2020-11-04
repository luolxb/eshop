package com.soubao.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Goods;
import com.soubao.entity.PromGoods;
import com.soubao.entity.SpecGoodsPrice;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.GoodsService;
import com.soubao.service.PromGoodsService;
import com.soubao.service.SpecGoodsPriceService;
import com.soubao.dao.PreSellMapper;
import com.soubao.dao.PromGoodsMapper;
import com.soubao.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-19
 */
@Slf4j
@Service("promGoodsService")
public class PromGoodsServiceImpl extends ServiceImpl<PromGoodsMapper, PromGoods> implements PromGoodsService {
    private int promType = 3;   //活动类型，优惠促销
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${spring.datasource.name}")
    private String dataBaseName;
    @Autowired
    private PreSellMapper preSellMapper;
    @Autowired
    private PromGoodsMapper promGoodsMapper;

    @Override
    public void removePromGoods(Integer promId) {
        PromGoods promGoods = getById(promId);
        if (promGoods.getIsEnd() == 0 && promGoods.getStatus() == 1) {
            throw new ShopException(ResultEnum.ACTIVITY_CANNOT_DELETED);
        }
        redisUtil.lRemove("prom_goods",1,promGoods);
        promGoods.setIsDeleted(1);
        updateById(promGoods);
        recoveryPromType(promId);
    }

    @Override
    public void closeProm(Integer promId) {
        PromGoods promGoods = getById(promId);
        redisUtil.lRemove("prom_goods",1,promGoods);
        promGoods.setStatus(0);
        updateById(promGoods);
        recoveryPromType(promId);
    }

    @Override
    public void schedule() {
        List<Object> pList = redisUtil.lGet("prom_goods", 0, -1);
        Set<Integer> promGoodsIds = new HashSet<>();
        for (Object o : pList) {
            PromGoods promGoods = (PromGoods)o;
            if (promGoods.getEndTime() <= System.currentTimeMillis() / 1000) {
                    promGoodsIds.add(promGoods.getId());
                redisUtil.lRemove("prom_goods",1,o);
            }
        }
        Set<Integer> specGoodsIds = new HashSet<>(); //有商品规格的goodsId
        Set<Integer> itemIds = new HashSet<>();
        if (promGoodsIds.size() > 0) {
            update(new UpdateWrapper<PromGoods>().set("is_end", 1).in("id", promGoodsIds));
            List<SpecGoodsPrice> specGoodsList = specGoodsPriceService.list(new QueryWrapper<SpecGoodsPrice>()
                    .select("goods_id,item_id")
                    .eq("prom_type", promType)
                    .in("prom_id", promGoodsIds));
            if (specGoodsList.size() > 0) {
                Set<Long> ids = specGoodsList.stream().map(SpecGoodsPrice::getItemId).collect(Collectors.toSet());
                if (ids.size() > 0) {
                    for (Long id : ids) {
                        itemIds.add(id.intValue());
                    }
                }
                specGoodsIds = specGoodsList.stream().map(SpecGoodsPrice::getGoodsId).collect(Collectors.toSet());
            }
            QueryWrapper<Goods> goodsWrapper = new QueryWrapper<>();
            goodsWrapper.select("goods_id").eq("prom_type", promType).in("prom_id", promGoodsIds);
            if (specGoodsIds.size() > 0) {
                goodsWrapper.notIn("goods_id", specGoodsIds);
            }
            //没商品规格的goodsId
            Set<Integer> goodsIds = goodsService.list(goodsWrapper).stream().map(Goods::getGoodsId).collect(Collectors.toSet());
            goodsService.recoveryPromTypes(goodsIds,itemIds,specGoodsIds);
        }
    }

    @Override
    public PromGoods getPromGoods(Integer promGoodsId) {
        PromGoods promGoods = getById(promGoodsId);
        List<SpecGoodsPrice> specGoodsPrices = specGoodsPriceService.list(new QueryWrapper<SpecGoodsPrice>()
                .eq("prom_type", promType)
                .eq("prom_id", promGoodsId));
        //参与此活动的规格商品id集合
        Set<Integer> specGoodsIds = specGoodsPrices.stream()
                .map(SpecGoodsPrice::getGoodsId)
                .collect(Collectors.toSet());
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        goodsQueryWrapper.eq("prom_type",promType).eq("prom_id",promGoodsId);
        if (specGoodsIds.size() > 0) {
            goodsQueryWrapper.notIn("goods_id",specGoodsIds);
        }
        List<Goods> goodsList = goodsService.list(goodsQueryWrapper);
        List<SpecGoodsPrice> specGoodsPriceList = new ArrayList<>();
        for (SpecGoodsPrice specGoodsPrice : specGoodsPrices) {
            SpecGoodsPrice specGoodsPriceTemp = new SpecGoodsPrice();
            String goodsName = goodsService.getById(specGoodsPrice.getGoodsId()).getGoodsName();
            BeanUtils.copyProperties(specGoodsPrice,specGoodsPriceTemp);
            specGoodsPriceTemp.setGoodsName(goodsName);
            specGoodsPriceList.add(specGoodsPriceTemp);
        }
        for (Goods goods : goodsList) {
            SpecGoodsPrice specGoodsPrice = new SpecGoodsPrice();
            BeanUtils.copyProperties(goods,specGoodsPrice);
            specGoodsPriceList.add(specGoodsPrice);
        }
        promGoods.setSpecGoodsPriceList(specGoodsPriceList);
        return promGoods;
    }

    @Override
    public void addPromGoods(PromGoods promGoods) {
        setPromGoods(promGoods);
        save(promGoods);
        setGoodsPromType(promGoods);
        PromGoods pg = getById(promGoods.getId());
        redisUtil.lSet("prom_goods",pg);
    }

    private void recoveryPromType(Integer promGoodsId) {
        List<SpecGoodsPrice> specGoodsList = specGoodsPriceService.list(new QueryWrapper<SpecGoodsPrice>()
                .eq("prom_type", promType).eq("prom_id", promGoodsId));
        Goods goods = goodsService.getOne(new QueryWrapper<Goods>()
                .eq("prom_type", promType)
                .eq("prom_id", promGoodsId));
        if (specGoodsList.size() > 0) {
            Set<Long> itemIds = specGoodsList.stream().map(SpecGoodsPrice::getItemId).collect(Collectors.toSet());
            Set<Integer> goodsIds = specGoodsList.stream().map(SpecGoodsPrice::getGoodsId).collect(Collectors.toSet());
            int count = specGoodsPriceService.count(new QueryWrapper<SpecGoodsPrice>()
                    .in("goods_id", goodsIds)
                    .notIn("item_id", itemIds)
                    .eq("prom_type", promType));
            specGoodsPriceService.update(new UpdateWrapper<SpecGoodsPrice>()
                    .set("prom_id", 0)
                    .set("prom_type", 0)
                    .in("item_id", itemIds));
            if (count == 0) {//该商品下的规格都没有活动
                goodsService.update(new UpdateWrapper<Goods>()
                        .set("prom_id", 0)
                        .set("prom_type", 0).in("goods_id", goodsIds));
            }
        }
        if (null != goods) {
            goodsService.update(new UpdateWrapper<Goods>()
                    .set("prom_id", 0)
                    .set("prom_type", 0).eq("goods_id", goods.getGoodsId()));
        }
    }

    @Override
    public void updatePromGoods(PromGoods promGoods) {
        //将商品变回普通商品
        recoveryPromType(promGoods.getId());
        promGoods.setStatus(1);
        this.updateById(promGoods);
        this.setGoodsPromType(promGoods);
    }

    @Override
    public IPage<Goods> goodsPage(Page<PromGoods> page, QueryWrapper<PromGoods> wrapper) {
        return promGoodsMapper.goodsPage(page, wrapper);
    }

    @PostConstruct
    public void setPromGoods() {
        redisUtil.del("prom_goods");
        Class<?> cls = PromGoods.class;
        String tableName = cls.getAnnotation(TableName.class).value();
        int count = preSellMapper.hashTable(dataBaseName,tableName);
        log.info("表名：{}", tableName);
        if (count > 0) {
            List<PromGoods> list = this.list(new QueryWrapper<PromGoods>().eq("is_end", 0));
            for (PromGoods promGoods : list) {
                redisUtil.lSet("prom_goods",promGoods);
            }
            log.info("redis放入" + list.size() + "促销商品记录");
        }
    }

    private void setPromGoods(PromGoods promGoods) {
        promGoods.setStatus(1);
        List<SpecGoodsPrice> specGoodsPriceList = promGoods.getSpecGoodsPriceList();
        for (SpecGoodsPrice specGoodsPrice : specGoodsPriceList) {
            if (specGoodsPrice.getItemId() != 0) {//有规格
                Integer storeCount = specGoodsPriceService.getById(specGoodsPrice.getItemId()).getStoreCount();
                if (promGoods.getBuyLimit() > storeCount) {
                    throw new ShopException(ResultEnum.ACTIVITY_CANNOT_BE_ADD);
                }
            }else {
                Integer storeCount = goodsService.getById(specGoodsPrice.getGoodsId()).getStoreCount();
                if (promGoods.getBuyLimit() > storeCount){
                    throw new ShopException(ResultEnum.ACTIVITY_CANNOT_BE_ADD);
                }
            }
        }
    }

    private void setGoodsPromType(PromGoods promGoods) {
        List<SpecGoodsPrice> specGoodsPriceList = promGoods.getSpecGoodsPriceList();
        for (SpecGoodsPrice specGoodsPrice : specGoodsPriceList) {
            if (specGoodsPrice.getItemId() != null && specGoodsPrice.getItemId() != 0) {//有商品规格的活动
                specGoodsPriceService.update(new UpdateWrapper<SpecGoodsPrice>().set("prom_id",promGoods.getId())
                        .set("prom_type",this.promType).eq("item_id",specGoodsPrice.getItemId()));
            }
            goodsService.update(new UpdateWrapper<Goods>().set("prom_id",promGoods.getId())
                    .set("prom_type",this.promType).eq("goods_id",specGoodsPrice.getGoodsId()));
        }
    }
}
