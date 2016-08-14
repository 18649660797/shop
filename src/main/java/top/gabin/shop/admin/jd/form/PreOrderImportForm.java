/**
 * Copyright (c) 2016 云智盛世
 * Created with PreOrderImportForm.
 */
package top.gabin.shop.admin.jd.form;

import top.gabin.shop.core.utils.excel.annotation.ExcelField;

import java.io.Serializable;

/**
 *
 * @author linjiabin on  16/8/14
 */
public class PreOrderImportForm implements Serializable {
    @ExcelField(value = "orderNumber", title = "订单号")
    private String orderNumber;
    @ExcelField(value = "remark", title = "预约内容")
    private String remark;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
