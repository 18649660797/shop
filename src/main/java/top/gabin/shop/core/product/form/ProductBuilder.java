package top.gabin.shop.core.product.form;

import top.gabin.shop.core.product.entity.Product;

import java.io.Serializable;

/**
 * @author linjiabin on  16/8/4
 */
public interface ProductBuilder extends Serializable {
    Long getId();
    Product build(Product product);
}
