package com.soubao.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soubao.dao.MessageActivityMapper;
import com.soubao.entity.*;
import com.soubao.service.MessageActivityService;
import com.soubao.service.MallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dyr
 * @since 2019-08-26
 */
@Service
public class MessageActivityServiceImpl extends ServiceImpl<MessageActivityMapper, MessageActivity> implements MessageActivityService {
    @Autowired
    private MessageActivityMapper messageActivityMapper;
    @Autowired
    private MallService mallService;

    @Override
    public IPage<MessageActivity> userMessagePage(Page<MessageActivity> page, Integer userId) {
        UserMessage userMessage = new UserMessage();
        userMessage.setUserId(userId);
        userMessage.setDeleted(0);
        IPage<MessageActivity> msgPage = messageActivityMapper.selectUserMessagePage(page, userMessage);
        messageActivityWithGoodsId(msgPage.getRecords());
        return msgPage;
    }

    private void messageActivityWithGoodsId(List<MessageActivity> messageActivities){
        //拙劣的数据表设计导致的循环里查询不同表
        for (MessageActivity messageActivity : messageActivities) {
            switch (messageActivity.getMmtCode()){
                case "flash_sale_activity":
                    FlashSale flashSale = mallService.flashSale(messageActivity.getPromId());
                    messageActivity.setGoodsId(flashSale.getGoodsId());
                    messageActivity.setStartTime(flashSale.getStartTime());
                    messageActivity.setIsFinish(flashSale.getIsEnd());
                    break;
                case "group_buy_activity":
                    GroupBuy groupBuy = mallService.groupBuy(messageActivity.getPromId());
                    messageActivity.setGoodsId(groupBuy.getGoodsId());
                    messageActivity.setStartTime(groupBuy.getStartTime());
                    messageActivity.setIsFinish(groupBuy.getIsEnd());
                    break;
                case "team_activity":
                    TeamActivity teamActivity = mallService.teamActivity(messageActivity.getPromId());
                    messageActivity.setGoodsId(teamActivity.getGoodsId());
                    messageActivity.setStartTime(0L);
                    messageActivity.setIsFinish(teamActivity.getIsOn() ? 0 : 1);
                    break;
                case "pre_sell_activity":
                    PreSell preSell = mallService.preSell(messageActivity.getPromId());
                    messageActivity.setGoodsId(preSell.getGoodsId());
                    messageActivity.setStartTime(preSell.getSellStartTime());
                    messageActivity.setIsFinish(preSell.getIsFinished());
                    break;
                default:
                    messageActivity.setGoodsId(0);
                    messageActivity.setStartTime(0L);
                    messageActivity.setIsFinish(0);
                    break;
            }
        }
    }
}
