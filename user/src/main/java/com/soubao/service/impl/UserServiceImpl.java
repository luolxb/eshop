package com.soubao.service.impl;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.common.constant.ShopConstant;
import com.soubao.dao.UserMapper;
import com.soubao.dto.StoreMemberCountDto;
import com.soubao.dto.StoreMemberIdsDto;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.entity.AccountLog;
import com.soubao.entity.OauthUser;
import com.soubao.entity.User;
import com.soubao.service.*;
import com.soubao.vo.UserDayReport;
import com.soubao.vo.UserExcel;
import com.soubao.vo.UserOrderStatistics;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private MallService mallService;
    @Autowired
    private AccountLogService accountLogService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OauthUserService oauthUserService;
    @Autowired
    private ShopConstant shopConstant;
    @Value("${security.salt}")
    private String salt;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Integer addUser(User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isEmpty(user.getMobile()) && StringUtils.isEmpty(user.getEmail())){
            throw new ShopException(ResultEnum.ACCOUNT_PARAM_ERROR);
        }
        if (StringUtils.isNotEmpty(user.getEmail())){
            userQueryWrapper.or().eq("email", user.getEmail());
        }
        if (StringUtils.isNotEmpty(user.getMobile())){
            userQueryWrapper.or().eq("mobile", user.getMobile());
        }
        if (this.count(userQueryWrapper) > 0) {
            throw new ShopException(ResultEnum.ACCOUNT_EXISTS);
        }
        user.setPassword(salt + passwordEncoder.encode(user.getPassword()));
        user.setIsDistribut(1);// 默认每个人都可以成为分销商
        user.setRegTime(System.currentTimeMillis() / 1000);
        save(user);
        Object basicRegIntegral = mallService.config().get("basic_reg_integral");
        int regIntegral = basicRegIntegral == null ? 0 : Integer.parseInt(String.valueOf(basicRegIntegral));

        if (regIntegral > 0) {
            //更新用户信息
            user.setPayPoints(regIntegral);
            updateById(user);

            //记录日志流水
            BigDecimal userMoney = user.getUserMoney();
            AccountLog ac = new AccountLog();
            ac.setUserId(user.getUserId());
            if (userMoney == null) {
                userMoney = BigDecimal.ZERO;
            }
            ac.setUserMoney(userMoney);
            ac.setPayPoints(regIntegral);
            ac.setChangeTime(System.currentTimeMillis() / 1000);
            ac.setFrozenMoney(new BigDecimal(0));
            ac.setDesc("会员注册赠送积分");
            ac.setOrderId(0);
            ac.setOrderSn("");
            accountLogService.save(ac);
        }
        return user.getUserId();
    }

    @Override
    public void updateUserBaseInfo(User user, User updateUser) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.set("last_login", System.currentTimeMillis()/1000).eq("user_id", user.getUserId());
        if (StringUtils.isNotEmpty(updateUser.getEmail())) {
            User emailUser = getOne(new QueryWrapper<User>()
                    .eq("email", updateUser.getEmail())
                    .ne("user_id", user.getUserId()));
            if (emailUser != null) {
                throw new ShopException(ResultEnum.EMAIL_IN_USE);
            }
            userUpdateWrapper.set("email", updateUser.getEmail());
        }
        if (StringUtils.isNotEmpty(updateUser.getNickname())) {
            userUpdateWrapper.set("nickname", updateUser.getNickname());
        }
        if (updateUser.getSex() != null) {
            userUpdateWrapper.set("sex", updateUser.getSex());
        }
        if (StringUtils.isNotEmpty(updateUser.getHeadPic())) {
            userUpdateWrapper.set("head_pic", updateUser.getHeadPic());
        }
        update(userUpdateWrapper);
    }

    @Override
    public void checkRegMobile(String mobile) {
        if (count(new QueryWrapper<User>().eq("mobile", mobile)) > 0) {
            throw new ShopException(ResultEnum.MOBILE_IS_BINDING);
        }
    }

    @Override
    public void checkOauthBind(User user, String mobile) {
        User mobileUser = getOne(new QueryWrapper<User>().eq("mobile", mobile));
        OauthUser oauthUser = oauthUserService.getOne(new QueryWrapper<OauthUser>()
                .eq("user_id", user.getUserId()).eq("oauth", "miniapp"));
        if (mobileUser == null || oauthUser == null) {
            throw new ShopException(ResultEnum.ACCOUNT_NOT_EXISTS);
        }

        //1.判断一个账号绑定多个QQ
        //2.判断一个QQ绑定多个账号
        OauthUser ou = oauthUserService.getOne(new QueryWrapper<OauthUser>()
                .eq("user_id", mobileUser.getUserId())
                .eq("oauth", "miniapp"));
        if (StringUtils.isNotEmpty(oauthUser.getUnionid())) {
            if (
                    (!oauthUser.getUserId().equals(mobileUser.getUserId()))
                            ||
                            (ou != null && !ou.getUnionid().equals(oauthUser.getUnionid()))
            ) {
                throw new ShopException(ResultEnum.ACCOUNT_IS_BINDIND);
            }
        } else {
            if (StringUtils.isNotEmpty(oauthUser.getOpenid()) || ou != null) {
                throw new ShopException(ResultEnum.ACCOUNT_IS_BINDIND);
            }
        }
    }

    @Override
    public List<UserDayReport> getDaysReport(Long startTime, Long endTime) {
        int dayLimit = (int) Math.ceil((double) (endTime - startTime) / 86400);
        if ((dayLimit == 1) && !DateUtil.isSameDay(new Date(startTime * 1000), new Date(endTime * 1000))) {
            dayLimit++;
        }
        return userMapper.selectDayReport(startTime, endTime, dayLimit);
    }

    @Override
    public List<UserExcel> getUserExportData(QueryWrapper<UserExcel> wrapper) {
        return userMapper.selectUserExportData(wrapper);
    }

    @Override
    public User loginAndGetUser(User loginUser, OauthUser oauthUser) {
        //php不做校验签名，我们也不做，因为signature（签名没传）,
        User user = getUserByRequestOauthUser(oauthUser);
        if (user == null) {
            int isBindAccount = Integer.parseInt((String) mallService.config().get("basic_is_bind_account"));
            if (isBindAccount == 1) {
                throw new ShopException(ResultEnum.NEED_BIND_ACCOUNT, oauthUser);//请绑定账号
            }
            register(loginUser);
        }
        assert user != null;
        OauthUser queryOauthUser = oauthUserService.getOne((new QueryWrapper<OauthUser>())
                .eq("oauth", oauthUser.getOauth()).eq("user_id", user.getUserId()));
        if (queryOauthUser != null) {
            if (!queryOauthUser.getUnionid().equals(oauthUser.getUnionid())) {
                queryOauthUser.setUnionid(oauthUser.getUnionid());
                oauthUserService.updateById(queryOauthUser);
            }
        } else {
            oauthUser.setUserId(user.getUserId());
            oauthUserService.save(oauthUser);
        }
        return user;
    }

    @Override
    public void register(User user) {
        if (user.getMobile() != null) {
            int hasCount = count((new QueryWrapper<User>()).eq("mobile", user.getMobile()));
            if (hasCount > 0) {
                throw new ShopException(ResultEnum.MOBILE_IS_BINDING);
            }
        }
        user.setRegTime(System.currentTimeMillis() / 1000);
        if (user.getFirstLeader() != null && user.getFirstLeader() > 0) {
            User firstLeaderUser = getById(user.getFirstLeader());
            if (firstLeaderUser != null) {
                user.setIsStoreMember(firstLeaderUser.getIsStoreMember());
                user.setSecondLeader(firstLeaderUser.getFirstLeader());
                user.setThirdLeader(firstLeaderUser.getSecondLeader());
                update((new UpdateWrapper<User>()).setSql("underling_number = underling_number + 1")
                        .eq("user_id", user.getFirstLeader()));
                update((new UpdateWrapper<User>()).setSql("underling_number = underling_number + 1")
                        .eq("user_id", user.getSecondLeader()));
                update((new UpdateWrapper<User>()).setSql("underling_number = underling_number + 1")
                        .eq("user_id", user.getThirdLeader()));
            }
        }
        user.setIsDistribut(1);
        user.setLastLogin(System.currentTimeMillis() / 1000);
        user.setPassword(salt + passwordEncoder.encode(user.getPassword()));
        user.setPaypwd(salt+ passwordEncoder.encode(user.getPaypwd()));
        save(user);
    }

    @Override
    public User addOauthUserReturnUser(OauthUser oauthUser, User user) {
        QueryWrapper<OauthUser> oauthUserQuery = new QueryWrapper<>();
        oauthUserQuery.eq("oauth", oauthUser.getOauth()).eq("openid", oauthUser.getOpenid());
        if (StringUtils.isNotEmpty(oauthUser.getUnionid())) {
            oauthUserQuery.eq("unionid", oauthUser.getUnionid());
        }
        OauthUser exOauthUser = oauthUserService.getOne(oauthUserQuery);
        if (exOauthUser != null) {
            throw new ShopException(ResultEnum.ACCOUNT_IS_BINDIND);
        }
        User retrunUser = null;
        if (user.getPassword() != null) {
            //有密码表示先创建账号后再创建身份
            register(user);
            oauthUser.setUserId(user.getUserId());
            oauthUserService.save(oauthUser);
        } else {
            retrunUser = getOne((new QueryWrapper<User>()).eq("mobile", user.getMobile()));
            if (retrunUser == null) {
                throw new ShopException(ResultEnum.ACCOUNT_NOT_EXISTS);
            }
            oauthUser.setUserId(retrunUser.getUserId());
            oauthUserService.save(oauthUser);
        }
        return retrunUser;
    }

    @Override
    public void withLeaderCount(List<User> records) {
        if(records.size() > 0){
            Set<Integer> userIds = records.stream().map(User::getUserId).collect(Collectors.toSet());
            List<User> userFirstLeaderCount = list((new QueryWrapper<User>()).select("first_leader, count(user_id) as first_leader_count")
                    .in("first_leader", userIds).groupBy("first_leader"));
            List<User> userSecondLeaderCount = list((new QueryWrapper<User>()).select("second_leader, count(user_id) as second_leader_count")
                    .in("second_leader", userIds).groupBy("second_leader"));
            List<User> userThirdLeaderCount = list((new QueryWrapper<User>()).select("third_leader, count(user_id) as third_leader_count")
                    .in("third_leader", userIds).groupBy("third_leader"));
            Map<Integer, Integer> firstLeaderMap = userFirstLeaderCount.stream().collect(Collectors.toMap(User::getFirstLeader, User::getFirstLeaderCount));
            Map<Integer, Integer> secondLeaderMap = userSecondLeaderCount.stream().collect(Collectors.toMap(User::getSecondLeader, User::getSecondLeaderCount));
            Map<Integer, Integer> thirdLeaderMap = userThirdLeaderCount.stream().collect(Collectors.toMap(User::getThirdLeader, User::getThirdLeaderCount));
            for(User user : records){
                user.setFirstLeaderCount(firstLeaderMap.getOrDefault(user.getUserId(), 0));
                user.setSecondLeaderCount(secondLeaderMap.getOrDefault(user.getUserId(), 0));
                user.setThirdLeaderCount(thirdLeaderMap.getOrDefault(user.getUserId(), 0));
            }
        }
    }

    @Override
    public void withOrderCount(List<User> records,Long startTime, Long endTime) {
        if(records.size() > 0){
            Set<Integer> userIds = records.stream().map(User::getUserId).collect(Collectors.toSet());
            List<UserOrderStatistics> userOrderStatistics = orderService.userOrderStatisticsList(userIds, startTime ,endTime);
            Map<Integer, UserOrderStatistics> statisticsMap = userOrderStatistics.stream().collect(Collectors.toMap(UserOrderStatistics::getUserId, statistics -> statistics));
            for(User user : records){
                user.setOrderNum(statisticsMap.containsKey(user.getUserId()) ? statisticsMap.get(user.getUserId()).getOrderCount() : 0);
                user.setAmount(statisticsMap.containsKey(user.getUserId()) ? statisticsMap.get(user.getUserId()).getTotalAmount() : BigDecimal.ZERO);
            }
        }
    }

    @Override
    public List<StoreMemberCountDto> getStoreMemberCountGroup(Set<Integer> storeIds) {
        return userMapper.selectStoreMemberCountGroup(storeIds);
    }

    @Override
    public Map<Integer, Set<Integer>> getUserIdsByIsStoreMember(Set<Integer> storeIds) {
        List<StoreMemberIdsDto> storeMemberIdsDtoList = userMapper.selectUserIdsByIsStoreMember(storeIds);
        HashMap<Integer, Set<Integer>> memberIdMap = new HashMap<>();
        storeMemberIdsDtoList.forEach(storeMemberIdsDto -> {
            Integer storeId = storeMemberIdsDto.getStoreId();
            if (memberIdMap.containsKey(storeId)){
                memberIdMap.get(storeId).add(storeMemberIdsDto.getUserId());
            }else{
                HashSet<Integer> memberIdSet = new HashSet<>();
                memberIdSet.add(storeMemberIdsDto.getUserId());
                memberIdMap.put(storeId, memberIdSet);
            }
        });
        return memberIdMap;
    }

    private User getUserByRequestOauthUser(OauthUser requestOauthUser) {
        User user = null;
        if (requestOauthUser.getOauth().equals(shopConstant.getMiniAppOauth())) {
            if (StringUtils.isNotEmpty(requestOauthUser.getUnionid())) {
                OauthUser onlyOauthUser = oauthUserService.getOne((new QueryWrapper<OauthUser>())
                        .eq("unionid", requestOauthUser.getUnionid()).last("limit 1"));
                if (onlyOauthUser != null) {
                    user = getById(onlyOauthUser.getUserId());
                }
            } else {
                OauthUser onlyOauthUser = oauthUserService.getOne((new QueryWrapper<OauthUser>())
                        .eq("openid", requestOauthUser.getOpenid()).eq("oauth", requestOauthUser.getOauth()).last("limit 1"));
                if (onlyOauthUser != null) {
                    user = getById(onlyOauthUser.getUserId());
                }
            }
        }
        return user;
    }
}










