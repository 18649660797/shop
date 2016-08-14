/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductBrandService.
 */
package top.gabin.shop.core.product.service;

import top.gabin.shop.core.product.entity.ProductBrand;
import top.gabin.shop.core.product.form.ProductBrandBuilder;

import java.util.List;

/**
 *
 * @author linjiabin on  16/8/8
 */
public interface ProductBrandService {
    ProductBrand get(Long brandId);
    ProductBrand getByName(String name);
    ProductBrand saveProductBrand(ProductBrandBuilder productBrandForm);
    List<ProductBrand> findAll();
    void delete(List<ProductBrand> productBrandList);
}
