package com.soubao.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.dto.StoreMemberCountDto;
import com.soubao.dto.StoreMemberIdsDto;
import com.soubao.entity.OauthUser;
import com.soubao.entity.User;
import com.soubao.vo.UserDayReport;
import com.soubao.vo.UserExcel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
public interface UserService extends IService<User> {

    //新增用户
    Integer addUser(User user);

    //获取用户每日情况
    List<UserDayReport> getDaysReport(Long startTime, Long endTime);

    //更新用户基本信息（无需验证的信息）
    void updateUserBaseInfo(User user, User updateUser);

    //检查注册账号的合法性
    void checkRegMobile(String mobile);

    //检查绑定账号的合法性
    void checkOauthBind(User user, String mobile);

    //获取会员导出数据
    List<UserExcel> getUserExportData(QueryWrapper<UserExcel> wrapper);

    /**
     * 小程序登录
     *
     * @param loginUser 要进行登录的用户
     * @return 登录身份
     */
    User loginAndGetUser(User loginUser, OauthUser oauthUser);

    void register(User user);

    User addOauthUserReturnUser(OauthUser oauthUser, User user);

    void withLeaderCount(List<User> records);

    void withOrderCount(List<User> records, Long startTime, Long endTime);

    List<StoreMemberCountDto> getStoreMemberCountGroup(Set<Integer> storeIds);

    Map<Integer, Set<Integer>> getUserIdsByIsStoreMember(Set<Integer> storeIds);

}
