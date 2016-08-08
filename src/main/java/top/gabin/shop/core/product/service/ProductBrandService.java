/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductBrandService.
 */
package top.gabin.shop.core.product.service;

import top.gabin.shop.core.product.entity.ProductBrand;
import top.gabin.shop.core.product.form.ProductBrandForm;

/**
 *
 * @author linjiabin on  16/8/8
 */
public interface ProductBrandService {
    ProductBrand getProductBrand(Long brandId);
    ProductBrand getByName(String name);
    ProductBrand saveProductBrand(ProductBrandForm productBrandForm);
}
