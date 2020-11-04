package com.soubao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soubao.dto.GuaranteeApplyDto;
import com.soubao.entity.Guarantee;

import java.util.List;

/**
 * <p>
 * 店铺消费者保障服务加入情况表 服务类
 * </p>
 *
 * @author dyr
 * @since 2020-03-11
 */
public interface GuaranteeService extends IService<Guarantee> {

    void withGuaranteeItem(List<Guarantee> guaranteeList);

    //消费者保障服务管理，开启/关闭
    void updateIsOpen(Guarantee guarantee, String logMsg);

    //加入/退出服务
    void apply(GuaranteeApplyDto guaranteeApplyDto);
}
