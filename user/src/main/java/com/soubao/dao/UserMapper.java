package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.soubao.dto.StoreMemberCountDto;
import com.soubao.dto.StoreMemberIdsDto;
import com.soubao.entity.User;
import com.soubao.vo.UserDayReport;
import com.soubao.vo.UserExcel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
public interface UserMapper extends BaseMapper<User> {
    List<UserDayReport> selectDayReport(Long startTime, Long endTime, Integer dayNum);

    //查询会员导出数据
    List<UserExcel> selectUserExportData(@Param(Constants.WRAPPER) QueryWrapper<UserExcel> wrapper);

    List<StoreMemberCountDto> selectStoreMemberCountGroup(@Param("storeIds") Set<Integer> storeIds);

    List<StoreMemberIdsDto> selectUserIdsByIsStoreMember(@Param("storeIds") Set<Integer> storeIds);
}


