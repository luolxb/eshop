package com.soubao.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.Goods;
import com.soubao.entity.GroupBuy;
import com.soubao.entity.GroupBuyGoodsItem;
import com.soubao.entity.SpecGoodsPrice;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.GoodsService;
import com.soubao.service.GroupBuyGoodsItemService;
import com.soubao.service.GroupBuyService;
import com.soubao.service.SpecGoodsPriceService;
import com.soubao.dao.GroupBuyMapper;
import com.soubao.dao.PreSellMapper;
import com.soubao.common.utils.RedisUtil;
import com.soubao.vo.GroupBuyGoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 团购商品表 服务实现类
 *
 * @author dyr
 * @since 2019-08-19
 */
@Slf4j
@Service("groupBuy")
public class GroupBuyServiceImpl extends ServiceImpl<GroupBuyMapper, GroupBuy>
        implements GroupBuyService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GroupBuyGoodsItemService groupBuyGoodsItemService;
    @Autowired
    private GroupBuyMapper groupBuyMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Value("${spring.datasource.name}")
    private String dataBaseName;

    @Autowired
    private PreSellMapper preSellMapper;
    @Autowired
    private SpecGoodsPriceService specGoodsPriceService;

    @Override
    public void updateGroupBuyStatus(GroupBuy groupBuy) {
        GroupBuy gb = this.getById(groupBuy.getId());
        redisUtil.lRemove("group_buy", 1, gb);
        this.update(new UpdateWrapper<GroupBuy>().set("status", groupBuy.getStatus()).eq("id", groupBuy.getId()));
        if (groupBuy.getStatus() == 1) {
            redisUtil.lSet("group_buy", gb);
        }
        if (groupBuy.getStatus() == 2) {
            List<GroupBuyGoodsItem> groupBuyGoodsItems = groupBuyGoodsItemService.list(new QueryWrapper<GroupBuyGoodsItem>()
                    .eq("group_buy_id", groupBuy.getId()));
            changeBack(groupBuyGoodsItems);
        }
    }

    @Override
    public void removeGroupBuy(Integer promId) {
        GroupBuy groupBuy = this.getById(promId);
        if (groupBuy.getStatus() == 1 && groupBuy.getIsEnd() == 0) {
            throw new ShopException(ResultEnum.ACTIVITY_CANNOT_DELETED);
        }
        redisUtil.lRemove("group_buy", 1, groupBuy);
        groupBuy.setIsDeleted(1);
        this.updateById(groupBuy);
        List<GroupBuyGoodsItem> groupBuyGoodsItems = groupBuyGoodsItemService.list(new QueryWrapper<GroupBuyGoodsItem>()
                .eq("group_buy_id", promId));
        changeBack(groupBuyGoodsItems);
    }

    @Override
    public void closeProm(Integer promId) {
        GroupBuy groupBuy = this.getById(promId);
        redisUtil.lRemove("group_buy", 1, groupBuy);
        groupBuy.setStatus(3);
        this.updateById(groupBuy);
        List<GroupBuyGoodsItem> groupBuyGoodsItems = groupBuyGoodsItemService.list(new QueryWrapper<GroupBuyGoodsItem>()
                .eq("group_buy_id", promId));
        changeBack(groupBuyGoodsItems);
    }

    @Override
    public void schedule() {
        List<Object> gList = redisUtil.lGet("group_buy", 0, -1);
        Set<Integer> groupBuyIds = new HashSet<>();
        if (null != gList && gList.size() > 0) {
            for (Object o : gList) {
                GroupBuy groupBuy = (GroupBuy) o;
                if (groupBuy.getEndTime() <= System.currentTimeMillis() / 1000
                        || groupBuy.getGoodsNum() < 1) {
                    groupBuyIds.add(groupBuy.getId());
                    redisUtil.lRemove("group_buy", 1, o);
                }
            }
            if (groupBuyIds.size() > 0) {
                update(new UpdateWrapper<GroupBuy>().set("is_end", 1).in("id", groupBuyIds));
                List<GroupBuyGoodsItem> gbi = groupBuyGoodsItemService.list(new QueryWrapper<GroupBuyGoodsItem>().in("group_buy_id", groupBuyIds));
                changeBack(gbi);
            }
        }
    }

    @Override
    public void saveGroupBuy(GroupBuy groupBuy) {
        this.setGroupBuy(groupBuy);
        this.save(groupBuy);
        this.addGroupBuyGoodsItem(groupBuy);
    }

    @Override
    public void updateGroupBuy(GroupBuy groupBuy) {
        Integer id = groupBuy.getId();
        GroupBuy gb = this.getById(id);
        List<GroupBuyGoodsItem> gbi =
                groupBuyGoodsItemService.list(
                        new QueryWrapper<GroupBuyGoodsItem>().eq("group_buy_id", gb.getId()));
        groupBuyGoodsItemService.remove(new QueryWrapper<GroupBuyGoodsItem>().eq("group_buy_id", gb.getId()));
        changeBack(gbi);// 变回普通商品
        this.setGroupBuy(groupBuy);
        this.updateById(groupBuy);
        this.addGroupBuyGoodsItem(groupBuy);
    }

    @Override
    public IPage<GroupBuyGoodsVo> getGroupBuyGoodsPage(Page<GroupBuy> page, QueryWrapper<GroupBuy> queryWrapper) {
        return groupBuyMapper.selectGroupBuyGoodsPage(page, queryWrapper);
    }

    @PostConstruct
    public void setGroupBuys() {
        redisUtil.del("group_buy");
        Class<?> cls = GroupBuy.class;
        String tableName = cls.getAnnotation(TableName.class).value();
        int count = preSellMapper.hashTable(dataBaseName, tableName);
        log.info("表名：{}", tableName);
        if (count > 0) {
            QueryWrapper<GroupBuy> groupBuyQueryWrapper = new QueryWrapper<>();
            groupBuyQueryWrapper.eq("is_end", 0);
            List<GroupBuy> list = this.list(groupBuyQueryWrapper);
            for (GroupBuy groupBuy : list) {
                redisUtil.lSet("group_buy", groupBuy);
            }
            log.info("数据：{}", list);
        }
    }

    private void addGroupBuyGoodsItem(GroupBuy groupBuy) {
        List<GroupBuyGoodsItem> groupBuyGoodsItem = groupBuy.getGroupBuyGoodsItem();
        for (GroupBuyGoodsItem goodsItem : groupBuyGoodsItem) {
            BigDecimal decimal =
                    goodsItem.getPrice().divide(goodsItem.getGoodsPrice(), 1, RoundingMode.HALF_UP);
            goodsItem.setRebate(decimal);
            goodsItem.setGroupBuyId(groupBuy.getId());
            groupBuyGoodsItemService.save(goodsItem);
            this.setGoodsPromType(goodsItem);
        }
    }

    private void setGoodsPromType(GroupBuyGoodsItem groupBuyGoodsItem) {
        if (groupBuyGoodsItem.getItemId() != null
                && groupBuyGoodsItem.getItemId() != 0) { // 有商品规格的活动
            specGoodsPriceService.update(
                    new UpdateWrapper<SpecGoodsPrice>()
                            .set("prom_id", groupBuyGoodsItem.getId())
                            .set("prom_type", 2)
                            .eq("item_id", groupBuyGoodsItem.getItemId()));
            goodsService.update(
                    new UpdateWrapper<Goods>()
                            .set("prom_id", groupBuyGoodsItem.getId())
                            .set("prom_type", 2)
                            .eq("goods_id", groupBuyGoodsItem.getGoodsId()));
        } else {
            goodsService.update(
                    new UpdateWrapper<Goods>()
                            .set("prom_id", groupBuyGoodsItem.getId())
                            .set("prom_type", 2)
                            .eq("goods_id", groupBuyGoodsItem.getGoodsId()));
        }
    }

    private void setGroupBuy(GroupBuy groupBuy) {
        List<GroupBuyGoodsItem> gbis = groupBuy.getGroupBuyGoodsItem();
        Integer goodsNum = 0;
        for (GroupBuyGoodsItem gbi : gbis) {
            if (gbi.getItemId() != null && gbi.getItemId() != 0) {
                SpecGoodsPrice specGoodsPrice = specGoodsPriceService.getById(gbi.getItemId());
                if (gbi.getGoodsNum() > specGoodsPrice.getStoreCount()) {
                    throw new ShopException(ResultEnum.ACTIVITY_CANNOT_BE_ADD);
                }
            }
            Goods goods = goodsService.getById(gbi.getGoodsId());
            if (gbi.getGoodsNum() > goods.getStoreCount()) {
                throw new ShopException(ResultEnum.ACTIVITY_CANNOT_BE_ADD);
            }
            goodsNum = goodsNum + gbi.getGoodsNum();
        }
        groupBuy.setGoodsNum(goodsNum);
        if (groupBuy.getGroupBuyGoodsItem().size() < 2) {
            BigDecimal rebate =
                    groupBuy.getPrice().divide(groupBuy.getGoodsPrice(), 1, RoundingMode.HALF_UP);
            groupBuy.setRebate(rebate);
        }
        if (groupBuy.getId() != null) {
            groupBuy.setIsEnd(0);
            groupBuy.setStatus(0);
        }
    }

    private void changeBack(List<GroupBuyGoodsItem> groupBuyGoodsItems) {
        if (groupBuyGoodsItems.size() > 0) {
            Set<Integer> specGoodsIds = new HashSet<>(); //有商品规格的goodsId
            Set<Integer> goodsIds = new HashSet<>(); //没商品规格的goodsId
            Set<Integer> itemIds = new HashSet<>();
            for (GroupBuyGoodsItem groupBuyGoodsItem : groupBuyGoodsItems) {
                if (groupBuyGoodsItem.getItemId() > 0) {
                    itemIds.add(groupBuyGoodsItem.getItemId());
                    specGoodsIds.add(groupBuyGoodsItem.getGoodsId());
                } else {
                    goodsIds.add(groupBuyGoodsItem.getGoodsId());
                }
            }
            goodsService.recoveryPromTypes(goodsIds, itemIds, specGoodsIds);
        }
    }
}
