/**
 * Copyright (c) 2016 云智盛世
 * Created with PurchaseOrderImportForm.
 */
package top.gabin.shop.admin.jd.form;

import top.gabin.shop.core.utils.excel.annotation.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author linjiabin on  16/8/10
 */
public class PurchaseOrderImportForm implements Serializable {
    @ExcelField(value = "orderNumber", title = "订单号")
    private String orderNumber;
    @ExcelField(value = "providerCN", title = "供应商简码")
    private String providerCN;
    @ExcelField(value = "providerName", title = "供应商名称")
    private String providerName;
    @ExcelField(value = "commodityCode", title = "商品编码")
    private String commodityCode;
    @ExcelField(value = "skuName", title = "商品名称")
    private String skuName;
    @ExcelField(value = "attribute", title = "订单属性")
    private String attribute;
    @ExcelField(value = "provider", title = "分配机构")
    private String provider;
    @ExcelField(value = "warehouseName", title = "仓库")
    private String warehouseName;
    @ExcelField(value = "detailAddress", title = "详细地址")
    private String detailAddress;
    @ExcelField(value = "link", title = "联系人")
    private String link;
    @ExcelField(value = "tel", title = "联系方式")
    private String tel;
    @ExcelField(value = "takePrice", title = "采购价格")
    private BigDecimal takePrice;
    @ExcelField(value = "itemCount", title = "采购数量")
    private Integer itemCount;
    @ExcelField(value = "realItemCount", title = "实收数量")
    private Integer realItemCount;
    @ExcelField(value = "total", title = "采购金额")
    private BigDecimal total;
    @ExcelField(value = "realTotal", title = "实际金额")
    private BigDecimal realTotal;
    @ExcelField(value = "buyer", title = "采购员")
    private String buyer;
    @ExcelField(value = "itemCount", title = "订购时间")
    private Date orderTime;
    @ExcelField(value = "overdueDate", title = "入库到期时间")
    private String overdueDate;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProviderCN() {
        return providerCN;
    }

    public void setProviderCN(String providerCN) {
        this.providerCN = providerCN;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public BigDecimal getTakePrice() {
        return takePrice;
    }

    public void setTakePrice(BigDecimal takePrice) {
        this.takePrice = takePrice;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getRealItemCount() {
        return realItemCount;
    }

    public void setRealItemCount(Integer realItemCount) {
        this.realItemCount = realItemCount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getRealTotal() {
        return realTotal;
    }

    public void setRealTotal(BigDecimal realTotal) {
        this.realTotal = realTotal;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOverdueDate() {
        return overdueDate;
    }

    public void setOverdueDate(String overdueDate) {
        this.overdueDate = overdueDate;
    }
}
