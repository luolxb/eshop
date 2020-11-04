package com.soubao.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soubao.entity.SpecItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-22
 */
public interface SpecItemMapper extends BaseMapper<SpecItem> {
    List<SpecItem> selectSpecItemWithSpecAndWithBySpecId(@Param("specItemIds") Set<Integer> specItemIds);
}
