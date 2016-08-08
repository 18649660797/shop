package top.gabin.shop.core.product.service;

import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.product.form.ProductFormBuilder;

/**
 * @author linjiabin on  16/8/4
 */
public interface ProductService {
    Product saveProduct(ProductFormBuilder productFormBuilder);
    Product getProduct(Long productId);
    void delete(Long productId);
}
