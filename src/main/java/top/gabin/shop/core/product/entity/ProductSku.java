/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductSku.
 */
package top.gabin.shop.core.product.entity;

import top.gabin.shop.common.money.Money;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 *
 * @author linjiabin on  16/8/4
 */
@Entity
@Table(name = "SHOP_PRODUCT_SKU")
public class ProductSku {
    @Id
    @Column(name = "ID")
    @TableGenerator(name = "sku_sequences", table = "shop_sequences", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "sku_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "SALE_PRICE")
    private BigDecimal salePrice;
    @OneToOne(targetEntity = Product.class)
    @JoinColumn(name = "DEFAULT_PRODUCT_ID")
    private Product defaultProduct;
    @ManyToOne(targetEntity = Product.class)
    @JoinTable(name = "SHOP_PRODUCT_SKU_XREF",
            joinColumns = @JoinColumn(name = "SKU_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID"))
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Money getSalePrice() {
        if (salePrice == null) {
            return Money.ZERO;
        }
        return new Money(salePrice);
    }

    public void setSalePrice(Money salePrice) {
        this.salePrice = salePrice.getAmount();
    }

    public Product getDefaultProduct() {
        return defaultProduct;
    }

    public void setDefaultProduct(Product defaultProduct) {
        this.defaultProduct = defaultProduct;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
