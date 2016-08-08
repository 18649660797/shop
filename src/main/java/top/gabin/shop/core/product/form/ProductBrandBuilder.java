package top.gabin.shop.core.product.form;

import top.gabin.shop.core.product.entity.ProductBrand;

/**
 * @author linjiabin on  16/8/8
 */
public interface ProductBrandBuilder {
    Long getProductBrandId();
    ProductBrand build(ProductBrand productBrand);
}
