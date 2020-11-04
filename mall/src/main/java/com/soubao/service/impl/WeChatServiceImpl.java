package com.soubao.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.soubao.constant.WeChatContant;
import com.soubao.common.constant.WeChatContant;
import com.soubao.dto.ArticleItem;
import com.soubao.dto.WeChatFollower;
import com.soubao.dto.WeChatUser;
import com.soubao.service.WeChatService;
import com.soubao.service.WxUserService;
import com.soubao.common.utils.RedisUtil;
import com.soubao.utils.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
public class WeChatServiceImpl implements WeChatService {
    @Autowired
    private WxUserService wxUserService;

    @Override
    public String processRequest(HttpServletRequest request) {
        // xml格式的消息数据
        String respXml = null;
        // 默认返回的文本消息内容
        String respContent;
        try {
            // 调用parseXml方法解析请求消息
            Map<String, String> requestMap = WeChatUtil.parseXml(request);
            // 消息类型
            String msgType = requestMap.get(WeChatContant.MsgType);
            log.info("=======type========");
            log.info(msgType);
            String mes = null;
            // 文本消息
            if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_TEXT)) {
                mes = requestMap.get(WeChatContant.Content);
                log.info("==============mes===============");
                log.info(mes);
                if (mes != null && mes.length() < 2) {
                    List<ArticleItem> items = new ArrayList<>();
                    ArticleItem item = new ArticleItem();
                    item.setTitle("照片墙");
                    item.setDescription("阿狸照片墙");
                    item.setPicUrl("http://changhaiwx.pagekite.me/photo-wall/a/iali11.jpg");
                    item.setUrl("http://changhaiwx.pagekite.me/page/photowall");
                    items.add(item);

                    item = new ArticleItem();
                    item.setTitle("哈哈");
                    item.setDescription("一张照片");
                    item.setPicUrl("http://changhaiwx.pagekite.me/images/me.jpg");
                    item.setUrl("http://changhaiwx.pagekite.me/page/index");
                    items.add(item);

                    item = new ArticleItem();
                    item.setTitle("小游戏2048");
                    item.setDescription("小游戏2048");
                    item.setPicUrl("http://changhaiwx.pagekite.me/images/2048.jpg");
                    item.setUrl("http://changhaiwx.pagekite.me/page/game2048");
                    items.add(item);

                    item = new ArticleItem();
                    item.setTitle("百度");
                    item.setDescription("百度一下");
                    item.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505100912368&di=69c2ba796aa2afd9a4608e213bf695fb&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170510%2F0634355517-9.jpg");
                    item.setUrl("http://www.baidu.com");
                    items.add(item);

                    respXml = WeChatUtil.sendArticleMsg(requestMap, items);
                } else if ("我的信息".equals(mes)) {
                    //TODO获取微信粉丝信息
//                    Map<String, String> userInfo = getUserInfo(requestMap.get(WeChatContant.FromUserName));
                    Map<String, String> userInfo = new HashMap<>();
                    System.out.println(userInfo.toString());
                    String nickname = userInfo.get("nickname");
                    String city = userInfo.get("city");
                    String province = userInfo.get("province");
                    String country = userInfo.get("country");
                    String headimgurl = userInfo.get("headimgurl");
                    List<ArticleItem> items = new ArrayList<>();
                    ArticleItem item = new ArticleItem();
                    item.setTitle("你的信息");
                    item.setDescription("昵称:" + nickname + " 地址:" + country + " " + province + " " + city);
                    item.setPicUrl(headimgurl);
                    item.setUrl("http://www.baidu.com");
                    items.add(item);

                    respXml = WeChatUtil.sendArticleMsg(requestMap, items);
                }
            }
            // 图片消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_IMAGE)) {
                respContent = "您发送的是图片消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 语音消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_VOICE)) {
                respContent = "您发送的是语音消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 视频消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_VIDEO)) {
                respContent = "您发送的是视频消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 地理位置消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_LOCATION)) {
                respContent = "您发送的是地理位置消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 链接消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_LINK)) {
                respContent = "您发送的是链接消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 事件推送
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get(WeChatContant.Event);
                log.info("===========eventType==============");
                log.info(eventType);
                // 关注
                if (eventType.equals(WeChatContant.EVENT_TYPE_SUBSCRIBE)) {
                    respContent = "谢谢您的关注！";
                    respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
                }
                // 取消关注
                else if (eventType.equals(WeChatContant.EVENT_TYPE_UNSUBSCRIBE)) {
                    //取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
                }
                // 扫描带参数二维码
                else if (eventType.equals(WeChatContant.EVENT_TYPE_SCAN)) {
                    // TODO 处理扫描带参数二维码事件
                }
                // 上报地理位置
                else if (eventType.equals(WeChatContant.EVENT_TYPE_LOCATION)) {
                    // TODO 处理上报地理位置事件
                }
                // 自定义菜单
                else if (eventType.equals(WeChatContant.EVENT_TYPE_CLICK)) {
                    // TODO 处理菜单点击事件
                }
            }
            if (respXml == null) {
                mes = mes == null ? "你这说的什么话！" : mes;
                respXml = WeChatUtil.sendTextMsg(requestMap, mes);
            }
            log.info("==============respXml==============");
            log.info(respXml);
            return respXml;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    @Override
    public WeChatFollower getWeChatFollower() {
        WeChatFollower weChatFollower = new WeChatFollower();
        //第一次从头开始获取，设置next_openid为空串
        weChatFollower.setNextOpenID("");
        getAllFollowerOpenIds(weChatFollower);
        return weChatFollower;
    }

    /**
     * 获取公众号所有粉丝
     * @param weChatFollower
     * @return
     */
    private void getAllFollowerOpenIds(WeChatFollower weChatFollower){
        String accessToken = wxUserService.getAccessToken();
        String result = WeChatUtil.getUserOpenIds(accessToken, weChatFollower.getNextOpenID());
        JSONObject resultJSON = wxUserService.resultHandle(result);
        Integer total = resultJSON.getInteger("total");
        //保存一次总粉丝数
        if (weChatFollower.getTotal() == null){
            weChatFollower.setTotal(total);
            weChatFollower.setOpenIds(new JSONArray());
        }
        Integer count = resultJSON.getInteger("count");
        if (count > 0){
            weChatFollower.getOpenIds().addAll(resultJSON.getJSONObject("data").getJSONArray("openid"));
            int max = 10000; //粉丝列表每次最大拉取量
            if (total > max && count == max){
                weChatFollower.setNextOpenID(resultJSON.getString("next_openid"));
                getAllFollowerOpenIds(weChatFollower);
            }
        }
    }

    @Override
    public List<WeChatUser> getWeChatFollowersUserInfo(List<Object> openIdsPage) {
        String accessToken = wxUserService.getAccessToken();
        //转换接口需要的请求数据
        JSONObject userListJSON = new JSONObject();
        JSONObject userJSON = new JSONObject();
        userListJSON.put("user_list", userJSON);
        for (Object openID : openIdsPage) {
            userJSON.put("openid", openID);
            userJSON.put("lang", "zh_CN");
        }
        String result = WeChatUtil.getUserInfoBatch(accessToken, userListJSON.toString());
        JSONObject jsonObject = wxUserService.resultHandle(result);
        System.out.println("拿到用户信息了");
        System.out.println(jsonObject);

        return null;
    }
}
