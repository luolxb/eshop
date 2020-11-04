package com.soubao.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.soubao.common.constant.ShopConstant;
import com.soubao.dao.OauthUsersMapper;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.entity.MiniappConfig;
import com.soubao.entity.OauthUser;
import com.soubao.service.OauthUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.service.MallService;
import com.soubao.common.utils.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-03-25
 */
@Service("oauthUserService")
public class OauthUserServiceImpl extends ServiceImpl<OauthUsersMapper, OauthUser> implements OauthUserService {
    @Autowired
    private MallService mallService;
    @Autowired
    private ShopConstant shopConstant;

    @Override
    public OauthUser getOauthUserByCode(String jsCode) {
        MiniappConfig miniappConfig = mallService.miniappConfig();
        JSONObject wxLoginResult = WechatUtil.getSessionKeyOrOpenId(jsCode, miniappConfig.getAppId(), miniappConfig.getAppSecret());
        log.warn("微信小程序登录接口返回："+wxLoginResult.toString());
        String errCode = wxLoginResult.getString("errcode");
        if(!StringUtils.isEmpty(errCode) && Integer.parseInt(errCode) != 0){
            throw new ShopException(ResultEnum.WX_MINI_APP_LOGIN_ERROR);//小程序登录失败
        }
        OauthUser oauthUser = new OauthUser();
        oauthUser.setUnionid(wxLoginResult.getString("unionid"));
        oauthUser.setOpenid(wxLoginResult.getString("openid"));
        oauthUser.setOauth(shopConstant.getMiniAppOauth());
        return oauthUser;
    }
}
