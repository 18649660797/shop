/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductImportForm.
 */
package top.gabin.shop.admin.jd.form;

import top.gabin.shop.core.utils.excel.annotation.ExcelField;

import java.io.Serializable;

/**
 *
 * @author linjiabin on  16/8/8
 */
public class ProductImportForm implements Serializable {
    // 序号
    @ExcelField(value = "num", title = "序号")
    private Integer num;
    // 品牌名称
    @ExcelField(value = "brandName", title = "品牌名称")
    private String brandName;
    // 商品规格编号
    @ExcelField(value = "commodityCode", title = "商品规格编号")
    private String commodityCode;
    // 规格名称
    @ExcelField(value = "skuName", title = "规格名称")
    private String skuName;
    // 箱规
    @ExcelField(value = "boxSku", title = "箱规")
    private Integer boxSku;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getBoxSku() {
        return boxSku;
    }

    public void setBoxSku(Integer boxSku) {
        this.boxSku = boxSku;
    }
}
