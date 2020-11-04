package com.soubao.dao;

import com.soubao.entity.SpecGoodsPrice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-19
 */
public interface SpecGoodsPriceMapper extends BaseMapper<SpecGoodsPrice> {
    /**
     * 规格表减库存
     *
     * @param specGoodsPrice
     */
    void stock(@Param("specGoodsPrice") SpecGoodsPrice specGoodsPrice);

}
