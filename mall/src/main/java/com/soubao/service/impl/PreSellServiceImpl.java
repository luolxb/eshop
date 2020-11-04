package com.soubao.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Goods;
import com.soubao.entity.PreSell;
import com.soubao.entity.SpecGoodsPrice;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.GoodsService;
import com.soubao.service.PreSellService;
import com.soubao.service.SpecGoodsPriceService;
import com.soubao.dao.PreSellMapper;
import com.soubao.common.utils.RedisUtil;
import com.soubao.common.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author dyr
 * @since 2019-10-24
 */
@Slf4j
@Service("perSellService")
public class PreSellServiceImpl extends ServiceImpl<PreSellMapper, PreSell>
        implements PreSellService {
    private int promType = 4;//活动类型，预售
    @Autowired private PreSellMapper preSellMapper;
    @Autowired private GoodsService goodsService;
    @Autowired private RedisUtil redisUtil;
    @Autowired private SpecGoodsPriceService specGoodsPriceService;

    @Value("${spring.datasource.name}")
    private String dataBaseName;

    @Override
    public void schedule() {
        List<Object> pList = redisUtil.lGet("pre_sell", 0, -1);
        Set<Integer> specGoodsIds = new HashSet<>(); //有商品规格的goodsId
        Set<Integer> goodsIds = new HashSet<>(); //没商品规格的goodsId
        Set<Integer> itemIds = new HashSet<>();
        Set<Integer> preSellIds = new HashSet<>();
        for (Object o : pList) {
            PreSell preSell = (PreSell) o;
            if (preSell.getSellEndTime() <= System.currentTimeMillis() / 1000
                    || preSell.getStockNum() < 1) {
                preSellIds.add(preSell.getPreSellId());
                if (preSell.getItemId() > 0) {
                    itemIds.add(preSell.getItemId());
                    specGoodsIds.add(preSell.getGoodsId());//有商品规格的goodsId
                } else {
                    goodsIds.add(preSell.getGoodsId());
                }
                redisUtil.lRemove("pre_sell", 1, o);
            }
        }
        if (preSellIds.size() > 0) {
            update(new UpdateWrapper<PreSell>().set("is_finished", 1).in("pre_sell_id", preSellIds));
        }
        goodsService.recoveryPromTypes(goodsIds,itemIds,specGoodsIds);

        /**
         * 1,三个Set,一个是（有规格）goods_id，一个是（没规格）goods_id,一个是item_id
         * 2,in item_id,把spec_goods_price,先查询b(not in a），再更新a
         * 3,如果b没有参与活动，直接更新goods，如果有，就不更新goods。
         * 4,in(（有规格）goods_id )查询出还有其他规格参与活动的商品id, 跟（没规格）goods_id合并得出要更新的商品id，更新goods表
         * 5，redis删除列表
         */
    }

    @Override
    public void updatePreSellStatus(PreSell preSell) {
        PreSell ps = getById(preSell.getPreSellId());
        redisUtil.lRemove("pre_sell", 1, ps);
        ps.setStatus(preSell.getStatus() == 1 ? 1 : 2);
        updateById(ps);
        if (preSell.getStatus() == 1) {
            redisUtil.lSet("pre_sell", ps);
        }
        if (preSell.getStatus() == 2) {
            goodsService.recoveryPromType(ps.getGoodsId(), ps.getItemId());
        }
    }

    @Override
    public void closeProm(Integer promId) {
        PreSell preSell = getById(promId);
        redisUtil.lRemove("pre_sell", 1, preSell);
        preSell.setSellEndTime(System.currentTimeMillis() / 1000);
        preSell.setIsFinished(1);
        preSell.setStatus(3);
        updateById(preSell);
        goodsService.recoveryPromType(preSell.getGoodsId(), preSell.getItemId());
    }

    @Override
    public void addPreSell(PreSell preSell) {
        setPreSell(preSell);
        save(preSell);
        setGoodsPromType(preSell);
    }

    @Override
    public void isFinished(PreSell preSell) {
        PreSell ps = getById(preSell.getPreSellId());
        redisUtil.lRemove("pre_sell",1,ps);
        ps.setIsFinished(preSell.getIsFinished());
        if (preSell.getIsFinished() == 1) {
            ps.setSellEndTime(preSell.getSellEndTime());
        }
        updateById(ps);
        goodsService.recoveryPromType(ps.getGoodsId(), ps.getItemId());
    }

    @Override
    public void deletePreSell(PreSell preSell) {
        if (preSell.getIsFinished() ==  0) {
            throw new ShopException(ResultEnum.ACTIVITY_CANNOT_BE_DELETED);
        }
        preSell.setIsDeleted(1);
        updateById(preSell);
    }

    @Override
    public void updatePreSell(PreSell preSell) {
        goodsService.recoveryPromType(preSell.getGoodsId(),preSell.getItemId());
        preSell.setStatus(0);
        setPreSell(preSell);
        updateById(preSell);
        setGoodsPromType(preSell);
    }

    @PostConstruct
    public void setPreSells() {
        redisUtil.del("pre_sell");
        Class<?> cls = PreSell.class;
        String tableName = cls.getAnnotation(TableName.class).value();
        int count = preSellMapper.hashTable(dataBaseName, tableName);
        log.info("表名：{}", tableName);
        if (count > 0) {
            QueryWrapper<PreSell> preSellQueryWrapper = new QueryWrapper<>();
            preSellQueryWrapper.eq("is_finished", 0);
            List<PreSell> list = list(preSellQueryWrapper);
            for (PreSell preSell : list) {
                redisUtil.lSet("pre_sell", preSell);
            }
            log.info("数据：{}", list);
        }
    }

    private void setPreSell(PreSell preSell) {
        preSell.setSellStartTime(
                TimeUtil.transForSecond(preSell.getStartTimeShow(), "yyyy-MM-dd HH:mm:ss"));
        preSell.setSellEndTime(
                TimeUtil.transForSecond(preSell.getEndTimeShow(), "yyyy-MM-dd HH:mm:ss"));
        if (preSell.getDepositPrice() != null
                && !preSell.getDepositPrice().equals(BigDecimal.ZERO)) {
            preSell.setPayStartTime(
                    TimeUtil.transForSecond(preSell.getPayStartTimeShow(), "yyyy-MM-dd HH:mm:ss"));
            preSell.setPayEndTime(
                    TimeUtil.transForSecond(preSell.getPayEndTimeShow(), "yyyy-MM-dd HH:mm:ss"));
            if (preSell.getPayStartTime() < preSell.getSellEndTime()) {
                throw new ShopException(ResultEnum.ADVANCE_ACTIVITY_PAY_TIME_FIRST_ERROR);
            }
            if (preSell.getPayEndTime() < preSell.getPayStartTime()) {
                throw new ShopException(ResultEnum.ADVANCE_ACTIVITY_PAY_TIME_SECOND_ERROR);
            }
        }
        preSell.setPriceLadder(JSON.toJSONString(preSell.getLadderPrice()));
        Integer goodsId = preSell.getGoodsId();
        Integer itemId = preSell.getItemId();
        if (itemId != null && itemId != 0) {
            SpecGoodsPrice specGoodsPrice = specGoodsPriceService.getById(itemId);
            if (preSell.getStockNum() > specGoodsPrice.getStoreCount()) {
                throw new ShopException(ResultEnum.ACTIVITY_CANNOT_BE_ADD);
            }
        }
        Goods goods = goodsService.getById(goodsId);
        if (preSell.getStockNum() > goods.getStoreCount()) {
            throw new ShopException(ResultEnum.ACTIVITY_CANNOT_BE_ADD);
        }
    }

    private void setGoodsPromType(PreSell preSell) {
        if (preSell.getItemId() != null && preSell.getItemId() != 0) { // 有商品规格的活动
            specGoodsPriceService.update(
                    new UpdateWrapper<SpecGoodsPrice>()
                            .set("prom_id", preSell.getPreSellId())
                            .set("prom_type", promType)
                            .eq("item_id", preSell.getItemId()));
            goodsService.update(
                    new UpdateWrapper<Goods>()
                            .set("prom_type", promType)
                            .eq("goods_id", preSell.getGoodsId()));
        } else {
            goodsService.update(
                    new UpdateWrapper<Goods>()
                            .set("prom_id", preSell.getPreSellId())
                            .set("prom_type", promType)
                            .eq("goods_id", preSell.getGoodsId()));
        }
    }
}
