package com.soubao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.common.constant.WxCode;
import com.soubao.dao.WxUserMapper;
import com.soubao.entity.WxUser;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.WxUserService;
import com.soubao.common.utils.HttpClientUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 微信公共帐号 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-06-22
 */
@Service
public class WxUserServiceImpl extends ServiceImpl<WxUserMapper, WxUser> implements WxUserService {
    @Autowired
    private WxUserService wxUserService;

    @Override
    public JSONObject resultHandle(String result) {
        //判断结果是否为空
        if (StringUtils.isEmpty(result)){
            throw new ShopException(ResultEnum.RESPONSE_EMPTY);
        }
        JSONObject jsonObject = JSON.parseObject(result);
        Object errCode = jsonObject.get("errcode");
        String errMsg;
        Integer code;
        if (errCode != null && (code = (Integer) errCode) != 0) {
            if (code == 40001) {
                update(new UpdateWrapper<WxUser>().set("web_expires", 0));
                throw new ShopException(ResultEnum.UPDATING_TOKEN);
            }
            errMsg = WxCode.getErrMsg(code);
            if (errMsg == null) {
                errMsg = jsonObject.get("errmsg").toString();
                int index = errMsg.indexOf("hint");
                if (index > 0) {
                    errMsg = errMsg.substring(0, index);
                }
            }
            throw new ShopException(code, "微信提醒：" + errMsg);
        }
        return jsonObject;
    }

    @Override
    public String getAccessToken() {
        WxUser wxUser = wxUserService.getOne(new QueryWrapper<WxUser>().last("limit 1"));
        if (wxUser == null) {
            throw new ShopException(ResultEnum.WX_USER_NOT_EXISTS);
        }
        //缓存未过期直接返回web_access_token
        long currentTime = System.currentTimeMillis() / 1000;
        if (wxUser.getWebExpires() > currentTime) {
            return wxUser.getWebAccessToken();
        }
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + wxUser.getAppid() + "&secret=" + wxUser.getAppsecret();
        String result = HttpClientUtil.doGet(url);
        JSONObject resultJson = resultHandle(result);
        Object accessToken = resultJson.get("access_token");
        if (accessToken == null) {
            wxUser.setWebExpires(0L);
            updateById(wxUser);
            throw new ShopException(ResultEnum.FAIL);
        }

        wxUser.setWebExpires(currentTime + 7000); //提前200秒过期
        wxUser.setWebAccessToken(accessToken.toString());
        updateById(wxUser);
        return accessToken.toString();
    }
}
