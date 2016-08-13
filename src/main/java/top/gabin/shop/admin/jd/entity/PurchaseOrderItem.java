/**
 * Copyright (c) 2016 云智盛世
 * Created with PurchaseOrderItem.
 */
package top.gabin.shop.admin.jd.entity;

import top.gabin.shop.common.money.Money;
import top.gabin.shop.core.entity.BasicEntity;
import top.gabin.shop.core.product.entity.ProductSku;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 *
 * @author linjiabin on  16/8/13
 */
@Entity
@Table(name = "SHOP_PURCHASE_ORDER_ITEM")
public class PurchaseOrderItem extends BasicEntity {
    @Id
    @Column(name = "ID")
    @TableGenerator(name = "purchase_order_sequences", table = "SHOP_SEQUENCES", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "purchase_order_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "QUANTITY")
    private int quantity;
    @Column(name = "REAL_QUANTITY")
    private int realQuantity;
    @Column(name = "SALE_PRICE")
    private BigDecimal salePrice;
    @Column(name = "REAL_PRICE")
    private BigDecimal realPrice;
    @ManyToOne(targetEntity = ProductSku.class)
    @JoinColumn(name = "SKU_ID")
    private ProductSku productSku;
    @ManyToOne(targetEntity = PurchaseOrder.class)
    @JoinColumn(name = "ORDER_ID")
    private PurchaseOrder order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Money getSalePrice() {
        return new Money(salePrice);
    }

    public void setSalePrice(Money salePrice) {
        this.salePrice = salePrice.getAmount();
    }

    public Money getRealPrice() {
        return new Money(realPrice);
    }

    public void setRealPrice(Money realPrice) {
        this.realPrice = realPrice.getAmount();
    }

    public ProductSku getProductSku() {
        return productSku;
    }

    public void setProductSku(ProductSku productSku) {
        this.productSku = productSku;
    }

    public PurchaseOrder getOrder() {
        return order;
    }

    public void setOrder(PurchaseOrder order) {
        this.order = order;
    }

    public int getRealQuantity() {
        return realQuantity;
    }

    public void setRealQuantity(int realQuantity) {
        this.realQuantity = realQuantity;
    }
}
