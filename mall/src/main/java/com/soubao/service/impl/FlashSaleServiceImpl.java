package com.soubao.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.*;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.*;
import com.soubao.dao.FlashSaleMapper;
import com.soubao.dao.PreSellMapper;
import com.soubao.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service("flashSaleService")
@Slf4j
public class FlashSaleServiceImpl extends ServiceImpl<FlashSaleMapper, FlashSale>
        implements FlashSaleService {
    @Autowired private FlashSaleMapper flashSaleMapper;
    @Autowired private GoodsService goodsService;
    @Autowired private RedisUtil redisUtil;
    @Autowired private PreSellService preSellService;
    @Autowired private GroupBuyService groupBuyService;
    @Autowired private GroupBuyGoodsItemService groupBuyGoodsItemService;
    @Autowired private PromGoodsService promGoodsService;
    @Autowired private SpecGoodsPriceService specGoodsPriceService;

    @Value("${spring.datasource.name}")
    private String dataBaseName;

    @Autowired private PreSellMapper preSellMapper;

    @Override
    public IPage<FlashSale> selectFlashSaleGoodsPage(
            Page<FlashSale> page, QueryWrapper<FlashSale> wrapper) {
        return flashSaleMapper.selectFlashSaleGoodsPage(page, wrapper);
    }

    @Override
    public void updateFlashSaleStatus(FlashSale flashSale) {
        Integer id = flashSale.getId();
        int status = flashSale.getStatus() == 1 ? 1 : 2;
        FlashSale fs = getById(id);
        redisUtil.lRemove("flash_sale", 1, fs);
        fs.setStatus(status);
        if (status == 1) {
            redisUtil.lSet("flash_sale", fs);
        }
        if (status == 2) {
            goodsService.recoveryPromType(fs.getGoodsId(), fs.getItemId());
        }
        updateById(fs);
    }

    @Override
    public void removeFlashSale(Integer promId) {
        FlashSale fs = getById(promId);
        redisUtil.lRemove("flash_sale", 1, fs);
        fs.setIsDel(1);
        goodsService.recoveryPromType(fs.getGoodsId(), fs.getItemId());
        updateById(fs);
    }

    @Override
    public void closeProm(Integer promId) {
        FlashSale flashSale = getById(promId);
        redisUtil.lRemove("flash_sale", 1, flashSale);
        flashSale.setStatus(3);
        updateById(flashSale);
        goodsService.recoveryPromType(flashSale.getGoodsId(), flashSale.getItemId());
    }

    @Override
    public void schedule() {
        List<Object> fList = redisUtil.lGet("flash_sale", 0, -1);
        Set<Integer> specGoodsIds = new HashSet<>(); //有商品规格的goodsId
        Set<Integer> goodsIds = new HashSet<>();
        Set<Integer> itemIds = new HashSet<>();
        Set<Integer> flashSaleIds = new HashSet<>();
        Set<Integer> flashSaleStatus = new HashSet<>();
        for (Object o : fList) {
            FlashSale flashSale = (FlashSale) o;
            if (flashSale.getEndTime() <= System.currentTimeMillis() / 1000
                    || flashSale.getGoodsNum() < 1) {
                if (flashSale.getEndTime() <= System.currentTimeMillis() / 1000) {
                    flashSaleIds.add(flashSale.getId());
                }
                if (flashSale.getGoodsNum() < 1) {
                    flashSaleIds.add(flashSale.getId());
                    flashSaleStatus.add(flashSale.getId());
                }
                if (flashSale.getItemId() > 0) {
                    itemIds.add(flashSale.getItemId());
                    specGoodsIds.add(flashSale.getGoodsId());
                } else {
                    goodsIds.add(flashSale.getGoodsId());
                }
                redisUtil.lRemove("flash_sale", 1, o);
            }
        }
        if (flashSaleStatus.size() > 0) {
            update(new UpdateWrapper<FlashSale>().set("status", 4).in("id", flashSaleStatus));
        }
        if (flashSaleIds.size() > 0) {
            update(new UpdateWrapper<FlashSale>().set("is_end", 1).in("id",flashSaleIds));
        }
        // 将商品恢复成普通商品
        goodsService.recoveryPromTypes(goodsIds,itemIds,specGoodsIds);
    }

    @PostConstruct
    private void getOnlineFlashSale() {
        redisUtil.del("flash_sale");
        Class<?> cls = FlashSale.class;
        String tableName = cls.getAnnotation(TableName.class).value();
        int count = preSellMapper.hashTable(dataBaseName, tableName);
        log.info("表名：{}", tableName);
        if (count > 0) {
            QueryWrapper<FlashSale> flashSaleQueryWrapper = new QueryWrapper<>();
            flashSaleQueryWrapper.eq("is_end", 0).eq("status", 1);
            List<FlashSale> list = list(flashSaleQueryWrapper);
            for (FlashSale flashSale : list) {
                redisUtil.lSet("flash_sale", flashSale);
            }
        }
    }

    @Override
    public void deleteTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -3);
        Date m3 = calendar.getTime();
        remove(new QueryWrapper<FlashSale>().eq("is_del", 1).le("gmt_create", m3));

        preSellService.remove(new QueryWrapper<PreSell>().eq("is_deleted", 1).le("gmt_create", m3));

        groupBuyService.remove(
                new QueryWrapper<GroupBuy>().eq("is_deleted", 1).le("gmt_create", m3));
        Set<Integer> groupBuyIds =
                groupBuyService.list().stream().map(GroupBuy::getId).collect(Collectors.toSet());
        groupBuyGoodsItemService.remove(
                new QueryWrapper<GroupBuyGoodsItem>().notIn("group_buy_id", groupBuyIds));

        promGoodsService.remove(
                new QueryWrapper<PromGoods>().eq("is_deleted", 1).le("gmt_create", m3));
    }

    @Override
    public void saveFlashSale(FlashSale flashSale) {
        setFlashSale(flashSale);
        save(flashSale);
        setGoodsPromType(flashSale);
    }

    @Override
    public void updateFlashSale(FlashSale flashSale) {
        setFlashSale(flashSale);
        goodsService.recoveryPromType(flashSale.getGoodsId(), flashSale.getItemId());
        flashSale.setStatus(0);
        updateById(flashSale);
        setGoodsPromType(flashSale);
    }

    private void setFlashSale(FlashSale flashSale) {
        flashSale.setEndTime(flashSale.getStartTime() + 7200);
        Integer goodsId = flashSale.getGoodsId();
        Integer itemId = flashSale.getItemId();
        if (itemId != null && itemId != 0) {
            SpecGoodsPrice specGoodsPrice = specGoodsPriceService.getById(itemId);
            if (flashSale.getGoodsNum() > specGoodsPrice.getStoreCount()) {
                throw new ShopException(ResultEnum.ACTIVITY_CANNOT_BE_ADD);
            }
        }
        Goods goods = goodsService.getById(goodsId);
        if (flashSale.getGoodsNum() > goods.getStoreCount()) {
            throw new ShopException(ResultEnum.ACTIVITY_CANNOT_BE_ADD);
        }
    }

    private void setGoodsPromType(FlashSale flashSale) {
        if (flashSale.getItemId() != null && flashSale.getItemId() != 0) { // 有商品规格的活动
            specGoodsPriceService.update(
                    new UpdateWrapper<SpecGoodsPrice>()
                            .set("prom_id", flashSale.getId())
                            .set("prom_type", 1)
                            .eq("item_id", flashSale.getItemId()));
            goodsService.update(
                    new UpdateWrapper<Goods>()
                            .set("prom_type", 1)
                            .eq("goods_id", flashSale.getGoodsId()));
        } else {
            goodsService.update(
                    new UpdateWrapper<Goods>()
                            .set("prom_id", flashSale.getId())
                            .set("prom_type", 1)
                            .eq("goods_id", flashSale.getGoodsId()));
        }
    }
}
