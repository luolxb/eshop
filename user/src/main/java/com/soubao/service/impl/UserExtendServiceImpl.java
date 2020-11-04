package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.UserExtendMapper;
import com.soubao.entity.User;
import com.soubao.entity.UserExtend;
import com.soubao.service.UserExtendService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-20
 */
@Service
public class UserExtendServiceImpl extends ServiceImpl<UserExtendMapper, UserExtend> implements UserExtendService {

    @Override
    public void saveInvoice(User user, UserExtend userExtend) {
        if ("个人".equals(userExtend.getInvoiceTitle())) {
            userExtend.setTaxpayer("");
        }
        userExtend.setUserId(user.getUserId());
        UserExtend ue = getOne(new QueryWrapper<UserExtend>().eq("user_id", user.getUserId()), false);
        // 如果用户有历史发票记录则取ID赋值给对象用于saveOrUpdate方法执行更新操作
        if (ue != null) {
            userExtend.setId(ue.getId());
        }
        saveOrUpdate(userExtend);
    }
}
