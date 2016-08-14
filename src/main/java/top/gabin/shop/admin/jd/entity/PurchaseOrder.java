/**
 * Copyright (c) 2016 云智盛世
 * Created with PurchaseOrder.
 */
package top.gabin.shop.admin.jd.entity;

import top.gabin.shop.core.entity.BasicEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author linjiabin on  16/8/12
 */
@Entity
@Table(name = "SHOP_PURCHASE_ORDER")
public class PurchaseOrder extends BasicEntity {
    @Id
    @Column(name = "ID")
    @TableGenerator(name = "purchase_order_sequences", table = "SHOP_SEQUENCES", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "purchase_order_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Column(name = "TOTAL")
    private BigDecimal total;
    @Column(name = "SUBMIT_DATE")
    private Date submitDate;
    @Column(name = "TIMEOUT_DATE")
    private Date timeoutDate;
    @Column(name = "BUYER")
    private String buyer;
    @Column(name = "ATTRIBUTE")
    private String attribute;
    @Column(name = "REMARK")
    private String remark;
    @OneToMany(targetEntity = PurchaseOrderItem.class, mappedBy = "order", orphanRemoval = true)
    private List<PurchaseOrderItem> purchaseOrderItemList = new ArrayList<PurchaseOrderItem>();
    @ManyToOne(targetEntity = Provider.class)
    @JoinColumn(name = "PROVIDER_ID")
    private Provider provider;
    @ManyToOne(targetEntity = WareHouse.class)
    @JoinColumn(name = "WARE_HOUSE_ID")
    private WareHouse wareHouse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Date getTimeoutDate() {
        return timeoutDate;
    }

    public void setTimeoutDate(Date timeoutDate) {
        this.timeoutDate = timeoutDate;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItemList() {
        return purchaseOrderItemList;
    }

    public void setPurchaseOrderItemList(List<PurchaseOrderItem> purchaseOrderItemList) {
        this.purchaseOrderItemList = purchaseOrderItemList;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public WareHouse getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(WareHouse wareHouse) {
        this.wareHouse = wareHouse;
    }

    public int getCount() {
        int count = 0;
        for (PurchaseOrderItem item : purchaseOrderItemList) {
            count += item.getQuantity();
        }
        return count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
