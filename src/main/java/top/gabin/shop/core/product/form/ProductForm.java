/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductForm.
 */
package top.gabin.shop.core.product.form;

import top.gabin.shop.common.money.Money;
import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.product.entity.ProductSku;

import java.math.BigDecimal;

/**
 *
 * @author linjiabin on  16/8/4
 */
public class ProductForm implements ProductBuilder {
    private Long productId;
    private String skuName;
    private BigDecimal salePrice;

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Long getProductId() {
        return productId;
    }

    public Product build(Product product) {
        ProductSku defaultSku = product.getDefaultSku();
        defaultSku.setName(getSkuName());
        defaultSku.setSalePrice(new Money(getSalePrice()));
        return product;
    }
}
