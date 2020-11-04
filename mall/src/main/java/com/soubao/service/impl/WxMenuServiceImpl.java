package com.soubao.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.WxMenuMapper;
import com.soubao.dto.MenuButtonDto;
import com.soubao.entity.MiniappConfig;
import com.soubao.entity.WxMenu;
import com.soubao.common.exception.ResultEnum;
import com.soubao.common.exception.ShopException;
import com.soubao.service.OrderService;
import com.soubao.service.WxMenuService;
import com.soubao.service.WxUserService;
import com.soubao.utils.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dyr
 * @since 2020-06-17
 */
@Service
public class WxMenuServiceImpl extends ServiceImpl<WxMenuMapper, WxMenu> implements WxMenuService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private WxUserService wxUserService;

    @Override
    public List<WxMenu> listToTree(List<WxMenu> list) {
        //用递归找子。
        List<WxMenu> treeList = new ArrayList<>();
        for (WxMenu tree : list) {
            if (tree.getPid() == 0) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    private static WxMenu findChildren(WxMenu tree, List<WxMenu> list) {
        for (WxMenu node : list) {
            if (node.getPid().equals(tree.getId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }
//                tree.getChildren().add(findChildren(node, list));
                tree.getChildren().add(node);   //该菜单为两级结构,直接添加
            }
        }
        return tree;
    }

    @Override
    public void addMenu(WxMenu wxMenu) {
        Integer pid = wxMenu.getPid();
        int menuCount = count(new QueryWrapper<WxMenu>().eq("pid", pid));
        if (pid == 0){
            //一级菜单不能超过三个
            if (menuCount >= 3){
                throw new ShopException(ResultEnum.FIRST_MENU_MAX_3);
            }
        }else{
            //子菜单不能超过五个
            if (menuCount >= 5){
                throw new ShopException(ResultEnum.CHILDREN_MENU_MAX_5);
            }
        }
        save(wxMenu);
    }

    @Override
    public void pushMenu() {
        List<WxMenu> wxMenuList = list();
        if (wxMenuList.isEmpty()){
            throw new ShopException(ResultEnum.NOT_MENU_PUSH);
        }
        List<WxMenu> wxMenuTree = listToTree(wxMenuList);
        List<MenuButtonDto> reqMenuList = convertMenu(wxMenuTree);
        String requestJSON = "{\"button\":" + JSON.toJSONString(reqMenuList) + "}";
        String accessToken = wxUserService.getAccessToken();
        wxUserService.resultHandle(WeChatUtil.createMenu(accessToken, requestJSON));
    }

    /**
     * 菜单转换
     * @param wxMenuTree
     * @return
     */
    private List<MenuButtonDto> convertMenu(List<WxMenu> wxMenuTree){
        MiniappConfig miniappConfig = orderService.getMiniAppConfig();
        String appid = "";
        if (miniappConfig != null){
            appid = miniappConfig.getAppId();
        }
        List<MenuButtonDto> menuButtonDtoList = new ArrayList<>();
        for (WxMenu menu : wxMenuTree) {
            MenuButtonDto topMenu = new MenuButtonDto();
            menuButtonDtoList.add(topMenu);
            topMenu.setName(menu.getName());
            List<WxMenu> menuChildren = menu.getChildren();
            if (menuChildren == null || menuChildren.isEmpty()){
                //一级菜单
                String type = menu.getType();
                String value = menu.getValue();
                topMenu.setType(type);
                if ("click".equals(type)){
                    topMenu.setKey(value);
                }else if ("view".equals(type)){
                    topMenu.setUrl(value);
                }else if("miniprogram".equals(type)){
                    topMenu.setUrl(value);
                    topMenu.setPagepath(value);
                    topMenu.setAppid(appid);
                }else{
                    topMenu.setKey(value);
                }
            }else{
                //二级菜单
                List<MenuButtonDto> subButtonList = new ArrayList<>();
                topMenu.setSubButton(subButtonList);
                for(WxMenu childMenu : menuChildren){
                    MenuButtonDto subMenu = new MenuButtonDto();
                    subMenu.setName(childMenu.getName());
                    String type = childMenu.getType();
                    String value = childMenu.getValue();
                    subMenu.setType(type);
                    if ("click".equals(type)){
                        subMenu.setKey(value);
                    }else if ("view".equals(type)){
                        subMenu.setUrl(value);
                    }else if("miniprogram".equals(type)){
                        subMenu.setUrl(value);
                        subMenu.setPagepath(value);
                        subMenu.setAppid(appid);
                    }else{
                        subMenu.setKey(value);
                    }
                    subButtonList.add(subMenu);
                }
            }
        }
        return menuButtonDtoList;
    }


}

