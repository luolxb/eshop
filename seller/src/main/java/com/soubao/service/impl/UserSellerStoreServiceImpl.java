package com.soubao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.UserSellerStoreMapper;
import com.soubao.entity.UserSellerStore;
import com.soubao.service.UserSellerStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserSellerStoreServiceImpl extends ServiceImpl<UserSellerStoreMapper,UserSellerStore> implements UserSellerStoreService  {
}
