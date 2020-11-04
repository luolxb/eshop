package com.soubao.dao;

import com.soubao.entity.Spec;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-22
 */
public interface SpecMapper extends BaseMapper<Spec> {
    List<Spec> selectSpecWithItem(@Param("specItemIds") Set<Integer> specItemIds);
    List<Spec> selectStoreBindSpecAndItem(Integer storeId, Integer typeId);

    List<Spec> selectSpecByTypeId(Integer typeId);
}
