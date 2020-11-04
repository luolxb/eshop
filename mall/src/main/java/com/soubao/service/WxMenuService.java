package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.WxMenu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dyr
 * @since 2020-06-17
 */
public interface WxMenuService extends IService<WxMenu> {

    List<WxMenu> listToTree(List<WxMenu> menuList);

    /**
     * 添加菜单
     * @param wxMenu
     */
    void addMenu(WxMenu wxMenu);

    //菜单更新到公众号
    void pushMenu();
}
