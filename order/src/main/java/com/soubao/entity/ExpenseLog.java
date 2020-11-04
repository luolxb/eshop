package com.soubao.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 平台支出金额或赠送积分记录日志
 * </p>
 *
 * @author dyr
 * @since 2020-03-04
 */
@Getter
@Setter
public class ExpenseLog {
    private Integer adminId;

    private BigDecimal money;

    private Integer integral;

    private Integer type;

    private Integer addtime;

    private Integer logTypeId;

    private Integer userId;

    private Integer storeId;

}
