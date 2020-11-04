package com.soubao.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soubao.entity.UserSign;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dyr
 * @since 2020-04-08
 */
public interface UserSignMapper extends BaseMapper<UserSign> {

    IPage<UserSign> selectUserSignPage(Page page, @Param(Constants.WRAPPER) QueryWrapper<UserSign> wrapper);
}
