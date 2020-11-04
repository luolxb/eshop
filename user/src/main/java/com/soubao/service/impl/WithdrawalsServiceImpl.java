package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.WithdrawalsMapper;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.entity.User;
import com.soubao.entity.Withdrawals;
import com.soubao.service.MallService;
import com.soubao.service.UserService;
import com.soubao.service.WithdrawalsService;
import com.soubao.vo.DistributeSurvey;
import com.soubao.vo.WithdrawalsExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-04-08
 */
@Service
public class WithdrawalsServiceImpl extends ServiceImpl<WithdrawalsMapper, Withdrawals> implements WithdrawalsService {
    @Autowired
    private WithdrawalsMapper withdrawalsMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private MallService mallService;

    @Override
    public void userWithdrawal(User user, Withdrawals withdrawals) {
        if (!"1".equals(mallService.config().get("cash_cash_open"))) {
            throw new ShopException(ResultEnum.WITHDRAWAL_DISABLE);
        }
        if (user.getIsLock() == 1) {
            throw new ShopException(ResultEnum.FREEZING_OF_FUND);
        }
        if ("微信".equals(withdrawals.getBankName())) {
            withdrawals.setRealname(withdrawals.getBankCard());
        }
        BigDecimal ableWithdrawMoney = (withdrawals.getType() == 1) ? getDistributeSurvey(user).getAbleWithdrawMoney() : user.getUserMoney();
        if (withdrawals.getMoney().compareTo(ableWithdrawMoney) > 0) {
            throw new ShopException(ResultEnum.NOT_SUFFICIENT_FUNDS);
        }

        //最大最小提现额度验证
        int minCash = (withdrawals.getType() == 1) ? Integer.parseInt((String) mallService.config().get("distribut_distribut_withdrawals_money"))
                : Integer.parseInt((String) mallService.config().get("cash_min_cash"));
        int maxCash = Integer.parseInt((String)mallService.config().get("cash_max_cash"));
        if (minCash > 0 && withdrawals.getMoney().compareTo(new BigDecimal(minCash)) < 0) {
            throw new ShopException(ResultEnum.MIN_CASH);
        }
        if (maxCash > 0 && withdrawals.getMoney().compareTo(new BigDecimal(maxCash)) > 0) {
            throw new ShopException(ResultEnum.MAX_CASH);
        }

        //手续费
        int serviceRatio = Integer.parseInt((String) mallService.config().get("cash_service_ratio"));
        if (serviceRatio > 0) {
            BigDecimal taxFee = withdrawals.getMoney().multiply(new BigDecimal(serviceRatio)).divide(new BigDecimal(100), 2, RoundingMode.UP);
            //限手续费
            int maxServiceMoney = Integer.parseInt((String) mallService.config().get("cash_max_service_money"));
            if (maxServiceMoney > 0 && taxFee.compareTo(new BigDecimal(maxServiceMoney)) > 0) {
                taxFee = new BigDecimal(maxServiceMoney);
            }
            //最小手续费
            int minServiceMoney = Integer.parseInt((String) mallService.config().get("cash_min_service_money"));
            if (minServiceMoney > 0 && taxFee.compareTo(new BigDecimal(minServiceMoney)) < 0) {
                taxFee = new BigDecimal(minServiceMoney);
            }
            if (taxFee.compareTo(withdrawals.getMoney()) >= 0) {
                throw new ShopException(ResultEnum.TAXFEE_ERROR);
            }
            withdrawals.setTaxfee(taxFee);
        } else {
            withdrawals.setTaxfee(new BigDecimal(0));
        }

        //当天已申请提现次数,已提现额度
        Withdrawals todayMoney = getOne(new QueryWrapper<Withdrawals>()
                .select("SUM( money ) as todayMoney, count( 1 ) AS todayWithdrawalCount")
                .eq("user_id", user.getUserId())
                .in("status", 0, 1, 2, 3)
                .apply("create_time > unix_timestamp(curdate())")
        );
        //当天提现总额度限定
        int countCash = Integer.parseInt((String) mallService.config().get("cash_count_cash"));
        if (countCash > 0) {
            //当天已提现额度
            if (withdrawals.getMoney().add(todayMoney.getTodayMoney()).compareTo(new BigDecimal(countCash)) > 0) {
                //今日还可提现额度
                BigDecimal canWithdrawalMoney = new BigDecimal(countCash).subtract(todayMoney.getTodayMoney());
                if (canWithdrawalMoney.compareTo(new BigDecimal(0)) > 0) {
                    throw new ShopException(ResultEnum.FAIL.getCode(), "你今天累计提现额为" + todayMoney.getTodayMoney() + "，最多可提现" + canWithdrawalMoney + "账户余额.");
                } else {
                    throw new ShopException(ResultEnum.FAIL.getCode(), "你今天累计提现额为" + todayMoney.getTodayMoney() + "，不能再提现了.");
                }
            }
        }

        //当天限申请次数限定
        Integer cashTimes = Integer.parseInt((String) mallService.config().get("cash_cash_times"));
        if (cashTimes > 0 && todayMoney.getTodayWithdrawalCount() >= cashTimes) {
            throw new ShopException(ResultEnum.MAX_WITHDRAWALS_COUNT);
        }

        withdrawals.setUserId(user.getUserId());
        withdrawals.setCreateTime(System.currentTimeMillis() / 1000);
        save(withdrawals);
        userService.update(new UpdateWrapper<User>()
                .set("bank_name", withdrawals.getBankName())
                .set("bank_card", withdrawals.getBankCard())
                .set("realname", withdrawals.getRealname())
                .eq("user_id", user.getUserId())
        );
    }

    @Override
    public IPage<Withdrawals> getWithdrawalsPage(Page<Withdrawals> page, QueryWrapper<Withdrawals> queryWrapper) {
        return withdrawalsMapper.selectWithdrawalsPage(page, queryWrapper);
    }

    @Override
    public Withdrawals getWithdrawalsById(Integer id) {
        return withdrawalsMapper.selectUserWithdrawalsById(id);
    }

    @Override
    public void updateWithdrawals(Withdrawals withdrawals) {
        withdrawals.setCheckTime(System.currentTimeMillis() / 1000);
        if (withdrawals.getStatus() == -1 || withdrawals.getStatus() == -2) {
            Boolean b = updateUserMoney(withdrawals.getUserId(), withdrawals.getMoney());
            if (b) {
                updateById(withdrawals);
            }
        } else {
            updateById(withdrawals);
        }
    }

    @Override
    public void updateWithdrawalsStatus(Set<Integer> ids, Integer status) {
        if (status == -1 || status == -2) {
            for (Integer id : ids) {
                Withdrawals withdrawals = getById(id);
                withdrawals.setCheckTime(System.currentTimeMillis() / 1000);
                withdrawals.setStatus(status);
                if (updateUserMoney(id, withdrawals.getMoney())) {
                    updateById(withdrawals);
                }
            }
        } else {
            UpdateWrapper<Withdrawals> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("status", status).set("check_time", System.currentTimeMillis() / 1000).in("id", ids);
            update(updateWrapper);
        }
    }

    @Override
    public DistributeSurvey getDistributeSurvey(User user) {
        DistributeSurvey distributeSurvey = new DistributeSurvey();
        distributeSurvey.setWithdrawingMoney((getOne((new QueryWrapper<Withdrawals>())
                .select("IFNULL(sum(money), 0.00) as money").eq("user_id", user.getUserId()).eq("type", 1)
                .in("status", 0, 1))).getMoney());
        distributeSurvey.setAbleWithdrawMoney(user.getDistributMoney().subtract(user.getDistributWithdrawalsMoney())
                .subtract(distributeSurvey.getWithdrawingMoney()));
        distributeSurvey.setApplyWithdrawMoney((getOne((new QueryWrapper<Withdrawals>())
                .select("IFNULL(sum(money), 0.00) as money").eq("user_id", user.getUserId()).eq("type", 1)
                .eq("status", 0))).getMoney());
        distributeSurvey.setWaitWithdrawMoney((getOne((new QueryWrapper<Withdrawals>())
                .select("IFNULL(sum(money), 0.00) as money").eq("user_id", user.getUserId()).eq("type", 1)
                .eq("status", 1))).getMoney());
        return distributeSurvey;
    }

    //审核失败时将“冻结资金”退回到“可用余额”
    public Boolean updateUserMoney(Integer userId, BigDecimal withdrawalsMoney) {
        User user = userService.getById(userId);
        user.setUserMoney(user.getUserMoney().add(withdrawalsMoney));
        user.setFrozenMoney(user.getFrozenMoney().subtract(withdrawalsMoney));
        userService.updateById(user);
        return true;
    }

    @Override
    public List<WithdrawalsExcel> getWithdrawalsExportData(QueryWrapper<Withdrawals> wrapper) {
        return withdrawalsMapper.selectWithDrawalsExportData(wrapper);
    }


}
