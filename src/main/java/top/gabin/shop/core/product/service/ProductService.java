package top.gabin.shop.core.product.service;

import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.product.form.ProductBuilder;

/**
 * @author linjiabin on  16/8/4
 */
public interface ProductService {
    Product saveProduct(ProductBuilder productBuilder);
    Product getProduct(Long productId);
    Product getProductByCommodityCode(String commodityCode);
    void delete(Long productId);
}
