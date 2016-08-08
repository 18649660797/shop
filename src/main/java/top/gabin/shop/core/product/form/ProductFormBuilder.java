package top.gabin.shop.core.product.form;

import top.gabin.shop.core.product.entity.Product;

/**
 * @author linjiabin on  16/8/4
 */
public interface ProductFormBuilder {
    Long getProductId();
    Product build(Product product);
}
