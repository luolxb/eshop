package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.entity.PreSell;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dyr
 * @since 2019-10-24
 */
public interface PreSellService extends IService<PreSell> {
    /**
     * 定时更新过期活动
     */
    void schedule();

    //预售审核
    void updatePreSellStatus(PreSell preSell);

    //取消预售
    void closeProm(Integer promId);

    void addPreSell(PreSell preSell);

    void isFinished(PreSell preSell);

    void deletePreSell(PreSell preSell);

    void updatePreSell(PreSell preSell);
}
