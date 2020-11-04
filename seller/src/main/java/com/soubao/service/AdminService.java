package com.soubao.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "admin")
public interface AdminService {

}
