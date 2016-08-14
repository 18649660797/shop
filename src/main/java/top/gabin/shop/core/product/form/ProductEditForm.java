/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductForm.
 */
package top.gabin.shop.core.product.form;

import top.gabin.shop.common.money.Money;
import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.product.entity.ProductBrand;
import top.gabin.shop.core.product.entity.ProductSku;
import top.gabin.shop.core.product.service.ProductBrandService;
import top.gabin.shop.core.utils.spring.SpringBeanUtils;

import java.math.BigDecimal;

/**
 *
 * @author linjiabin on  16/8/4
 */
public class ProductEditForm implements ProductBuilder {
    private Long id;
    private String name;
    private BigDecimal salePrice;
    private String commodityCode;
    private Long brandId;

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

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Product build(Product product) {
        ProductSku defaultSku = product.getDefaultSku();
        defaultSku.setName(getName());
        defaultSku.setSalePrice(new Money(getSalePrice()));
        defaultSku.setCommodityCode(getCommodityCode());
        if (brandId != null) {
            ProductBrandService brandService = SpringBeanUtils.getBean(ProductBrandService.class);
            ProductBrand productBrand = brandService.get(brandId);
            product.setProductBrand(productBrand);
        }
        return product;
    }
}
