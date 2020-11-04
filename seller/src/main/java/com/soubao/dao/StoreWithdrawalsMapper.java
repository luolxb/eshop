package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.StoreWithdrawals;
import com.soubao.vo.StoreWithdrawalsExcel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-12-18
 */
public interface StoreWithdrawalsMapper extends BaseMapper<StoreWithdrawals> {
    //查询商家提现申请列表/转款列表
    IPage<StoreWithdrawals> selectStoreWithdrawalsPage(Page<StoreWithdrawals> page,
                                                       @Param("storeId") Integer storeId,
                                                       @Param("startTime") Integer startTime,
                                                       @Param("endTime") Integer endTime,
                                                       @Param("status") Integer status,
                                                       @Param("bankCard") String bankCard,
                                                       @Param("realname") String realname );

    //根据id查询店铺提现申请/转款记录
    StoreWithdrawals selectStoreWithdrawalsById(@Param("id") Integer id);

    //查询商家转款导出数据
    List<StoreWithdrawalsExcel> selectStoreWithdrawalsExportData( @Param("ids") Set<Integer> ids,
                                                                  @Param("storeId") Integer storeId,
                                                                  @Param("startTime") Integer startTime,
                                                                  @Param("endTime") Integer endTime,
                                                                  @Param("status") Integer status,
                                                                  @Param("bankCard") String bankCard,
                                                                  @Param("realname") String realname);
}
