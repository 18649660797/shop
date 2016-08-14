/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductBrandForm.
 */
package top.gabin.shop.core.product.form;

import top.gabin.shop.core.product.entity.ProductBrand;

/**
 *
 * @author linjiabin on  16/8/8
 */
public class ProductBrandForm implements ProductBrandBuilder {
    private Long id;
    private String name;

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

    public ProductBrand build(ProductBrand productBrand) {
        productBrand.setName(name);
        return productBrand;
    }
}
