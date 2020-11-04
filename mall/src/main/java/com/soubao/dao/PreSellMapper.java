package com.soubao.dao;

import com.soubao.entity.PreSell;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-10-24
 */
public interface PreSellMapper extends BaseMapper<PreSell> {
    int hashTable(@Param(value = "dataBaseName") String dataBaseName, @Param(value = "tableName") String tableName);
}
