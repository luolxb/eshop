package com.soubao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.entity.OauthUser;
import com.soubao.service.OauthUserService;
import com.soubao.dao.OauthUserMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-26
 */
@Service("oauthUserService")
public class OauthUserServiceImpl extends ServiceImpl<OauthUserMapper, OauthUser> implements OauthUserService {
}
