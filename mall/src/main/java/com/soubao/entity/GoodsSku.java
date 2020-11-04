package com.soubao.entity;

import com.soubao.service.GoodsService;
import com.soubao.common.utils.SpringUtils;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GoodsSku extends Goods {

    /**
     * 是否使用单独购买，还是拼团购买
     * 该字段用于拼团活动的商品
     * 默认是单独购买
     */
    private Boolean isTeam = false;
    public GoodsSku(Goods goods, SpecGoodsPrice specGoodsPrice) {
        setGoods(goods);
        if(null != specGoodsPrice){
            setSpecGoodsPrice(specGoodsPrice);
        }
    }

    public GoodsSku(Goods goods) {
        setGoods(goods);
    }

    public void useGoodsProm(){
        if(getPromType() > 0 && getPromId() > 0){
            if(getPromType() == 6 && !isTeam){
                //拼团商品单独购买
                setPromTypeDesc("该商品正在参与拼团");
                return;
            }
            GoodsProm goodsProm = ((GoodsService) SpringUtils.getBean("goodsService"))
                    .getGoodsProm(getPromType(), getPromId(), getItemId());
            if(goodsProm != null){
                setGoodsProm(goodsProm);
            }
        }else{
            setPromType(0);
            setPromId(0);
        }
    }

    private void setGoodsProm(GoodsProm goodsProm){
        if(!goodsProm.getIsOn()){
            return;
        }
        setCostPrice(getShopPrice());//参与活动的时候，本店价变成原价
        setShopPrice(goodsProm.getShopPrice());//参与活动的时候，活动价变成本店价
        if(goodsProm.getPayMoney() != null){
            setPaidMoney(goodsProm.getPayMoney());//预售订金
        }
        if(goodsProm.getStock() != null){
            setStoreCount(goodsProm.getStock());//库存
        }
        if(goodsProm.getBuyLimit() != null){
            setLimitNum(goodsProm.getBuyLimit());//单次购买限制数
        }
        setPromTypeDesc(goodsProm.getPromTypeDesc());
        setPromStartTime(goodsProm.getPromStartTime());
        setPromEndTime(goodsProm.getPromEndTime());
    }

    public void setSpecGoodsPrice(SpecGoodsPrice specGoodsPrice){
        setSpecKey(specGoodsPrice.getKey());
        setSpecKeyName(specGoodsPrice.getKeyName());
        setShopPrice(specGoodsPrice.getPrice());
        setStoreCount(specGoodsPrice.getStoreCount());
        setLimitNum(specGoodsPrice.getStoreCount());
        setBarCode(specGoodsPrice.getBarCode());
        setSku(specGoodsPrice.getSku());
        setPromId(specGoodsPrice.getPromId());
        setPromType(specGoodsPrice.getPromType());
        setCostPrice(specGoodsPrice.getCost());
        setOriginalImg(specGoodsPrice.getSpecImg());
        setItemId(specGoodsPrice.getItemId());
    }

    private void setGoods(Goods goods){
        setGoodsId(goods.getGoodsId());
        setCatId1(goods.getCatId1());
        setCatId2(goods.getCatId2());
        setCatId3(goods.getCatId3());
        setStoreCatId1(goods.getStoreCatId1());
        setStoreCatId2(goods.getStoreCatId2());
        setGoodsSn(goods.getGoodsSn());
        setGoodsName(goods.getGoodsName());
        setClickCount(goods.getClickCount());
        setBrandId(goods.getBrandId());
        setStoreCount(goods.getStoreCount());
        setLimitNum(goods.getStoreCount());
        setCollectSum(goods.getCollectSum());
        setCommentCount(goods.getCommentCount());
        setWeight(goods.getWeight());
        setVolume(goods.getVolume());
        setMarketPrice(goods.getMarketPrice());
        setShopPrice(goods.getShopPrice());
        setCostPrice(goods.getCostPrice());
        setExchangeIntegral(goods.getExchangeIntegral());
        setKeywords(goods.getKeywords());
//        setGoodsContent(goods.getGoodsContent());
        setMobileContent(goods.getMobileContent());
        setOriginalImg(goods.getOriginalImg());
        setIsVirtual(goods.getIsVirtual());
        setVirtualIndate(goods.getVirtualIndate());
        setVirtualLimit(goods.getVirtualLimit());
        setVirtualRefund(goods.getVirtualRefund());
        setIsOnSale(goods.getIsOnSale());
        setIsFreeShipping(goods.getIsFreeShipping());
        setOnTime(goods.getOnTime());
        setSort(goods.getSort());
        setIsRecommend(goods.getIsRecommend());
        setIsNew(goods.getIsNew());
        setIsHot(goods.getIsHot());
        setLastUpdate(goods.getLastUpdate());
        setGoodsType(goods.getGoodsType());
        setGiveIntegral(goods.getGiveIntegral());
        setSalesSum(goods.getSalesSum());
        setVirtualSalesSum(goods.getVirtualSalesSum());
        setPromType(goods.getPromType());
        setPromId(goods.getPromId());
        setDistribut(goods.getDistribut());
        setStoreId(goods.getStoreId());
        setSpu(goods.getSpu());
        setSku(goods.getSku());
        setGoodsState(goods.getGoodsState());
        setCloseReason(goods.getCloseReason());
        setSuppliersId(goods.getSuppliersId());
        setTemplateId(goods.getTemplateId());
        setIsOwnShop(goods.getIsOwnShop());
        setVideo(goods.getVideo());
        setLabelId(goods.getLabelId());
    }

    private String specKey = "";//默认无规格
    private String specKeyName = "";//默认无规格
    private String barCode;
    private Long itemId = (long) 0;
    private BigDecimal paidMoney = BigDecimal.ZERO;//预售活动订金
    private String promTypeDesc;//活动类型描述
    private Long promStartTime;//活动开始时间
    private Long promEndTime;//活动结束时间
    private Integer limitNum;//单次购买限制数

    /**
     * 是否能使用订单优惠
     *  预售，拼团，积分兑换不能使用订单优惠
     * @return
     */
    public boolean getIsAbleOrderProm(){
        if(getPromType() == 0 || getPromType() == 1
                || getPromType() == 2 || getPromType() == 3 || getExchangeIntegral() == 0){
            return true;
        }else{
            return false;
        }
    }

}
