/**
 * Copyright (c) 2016 云智盛世
 * Created with Money.
 */
package top.gabin.shop.common.money;

import java.math.BigDecimal;

/**
 * 先预留货币类
 * @author linjiabin on  16/8/4
 */
public class Money {
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    private BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
