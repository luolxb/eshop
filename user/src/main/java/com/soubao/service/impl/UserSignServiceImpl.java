package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.UserSignMapper;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.entity.AccountLog;
import com.soubao.entity.User;
import com.soubao.entity.UserSign;
import com.soubao.service.AccountLogService;
import com.soubao.service.MallService;
import com.soubao.service.UserSignService;
import com.soubao.vo.UserSignStatisticsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-04-08
 */
@Service
public class UserSignServiceImpl extends ServiceImpl<UserSignMapper, UserSign> implements UserSignService {

    @Autowired
    private MallService mallService;
    @Autowired
    private AccountLogService accountLogService;
    @Autowired
    private UserSignMapper userSignMapper;

    @Override
    public UserSignStatisticsVo getStatisticsBySign(UserSign sign) {
        UserSignStatisticsVo vo = new UserSignStatisticsVo();
        if (sign != null) {
            vo.setConsecutiveDays(sign.getSignCount());
            vo.setMonthPoint(sign.getThisMonth());
            vo.setSignTime(sign.getSignTime());
            vo.setTotalPoint(sign.getCumtrapz());
        } else {
            vo.setConsecutiveDays(0);
            vo.setMonthPoint(0);
            vo.setSignTime("");
            vo.setTotalPoint(0);
        }
        return vo;
    }

    @Override
    public void sign(User user) {
        int isOn = Integer.parseInt((String) mallService.config().get("sign_sign_on_off"));
        if (isOn != 1) {
            throw new ShopException(ResultEnum.SIGN_CONFIG_NO_ON);
        }
        UserSign userSign = getOne((new QueryWrapper<UserSign>()).eq("user_id", user.getUserId()).last("limit 1"));
        String nowDayStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        int signIntegral = Integer.parseInt((String) mallService.config().get("sign_sign_integral"));
        int signAward = Integer.parseInt((String) mallService.config().get("sign_sign_award"));
        int signCount = Integer.parseInt((String) mallService.config().get("sign_sign_signcount"));
        if (userSign != null) {
            if (nowDayStr.equals(userSign.getSignLast())) {
                throw new ShopException(ResultEnum.SIGN_REPEAT);
            }
            int finalIntegral = (userSign.getSignCount() + 1) >= signCount ? signAward : signIntegral;
            String yesterdayStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(System.currentTimeMillis() - 1000 * 86400));
            String nowMonthStr = (new SimpleDateFormat("yyyy-MM")).format(new Date());
            userSign.setSignCount(yesterdayStr.equals(userSign.getSignLast()) ? userSign.getSignCount() + 1 : 1);//不是连续签变回1
            userSign.setSignTotal(userSign.getSignTotal() + 1);
            userSign.setCumtrapz(userSign.getCumtrapz() + finalIntegral);
            String lastSignMonth = userSign.getSignLast().substring(0, userSign.getSignLast().lastIndexOf("-"));
            userSign.setSignTime(nowMonthStr.equals(lastSignMonth) ? userSign.getSignTime() + "," + nowDayStr : nowDayStr);
            userSign.setThisMonth(nowMonthStr.equals(lastSignMonth) ? userSign.getThisMonth() + finalIntegral : finalIntegral);
            userSign.setSignLast(nowDayStr);
            updateById(userSign);
            AccountLog accountLog = new AccountLog();
            accountLog.setUserId(user.getUserId());
            accountLog.setUserMoney(BigDecimal.ZERO);
            accountLog.setPayPoints(finalIntegral);
            accountLog.setFrozenMoney(BigDecimal.ZERO);
            accountLog.setDesc("签到赠送" + finalIntegral + "积分");
            accountLogService.saveAccountLog(accountLog);
        } else {
            UserSign addUserSign = new UserSign();
            addUserSign.setUserId(user.getUserId());
            addUserSign.setSignTotal(1);
            addUserSign.setSignLast(nowDayStr);
            addUserSign.setSignTime(nowDayStr);
            addUserSign.setCumtrapz(signIntegral);
            addUserSign.setSignCount(1);
            addUserSign.setThisMonth(addUserSign.getCumtrapz());
            save(addUserSign);
            AccountLog accountLog = new AccountLog();
            accountLog.setUserId(user.getUserId());
            accountLog.setUserMoney(BigDecimal.ZERO);
            accountLog.setPayPoints(addUserSign.getCumtrapz());
            accountLog.setFrozenMoney(BigDecimal.ZERO);
            accountLog.setDesc("第一次签到赠送" + addUserSign.getCumtrapz() + "积分");
            accountLogService.saveAccountLog(accountLog);
        }
    }

    @Override
    public IPage<UserSign> getUserSignPage(Page page, QueryWrapper<UserSign> wrapper) {
        return userSignMapper.selectUserSignPage(page, wrapper);
    }
}
