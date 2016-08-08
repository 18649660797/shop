/**
/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductServiceImplTest.
 */
package top.gabin.shop.core.product.service;

import basic.BasicTestInterface;
import org.junit.Test;
import org.springframework.util.Assert;
import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.product.form.ProductForm;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 *
 * @author linjiabin on  16/8/4
 */
public class ProductServiceImplTest extends BasicTestInterface {
    @Resource
    private ProductService productService;
    @Test
    public void testSaveProduct() {
        ProductForm productForm = new ProductForm();
        productForm.setSalePrice(new BigDecimal(100));
        String skuName = "Iphone 7s";
        productForm.setSkuName(skuName);
        Product product = productService.saveProduct(productForm);
        Assert.notNull(product.getId());
        Product product1 = productService.getProduct(product.getId());
        Assert.notNull(product1.getDefaultSku());
        Assert.notNull(product1.getDefaultSku().getName());
        Assert.isTrue(product1.getDefaultSku().getName().equals(skuName));
        productService.delete(product1.getId());
        product1 = productService.getProduct(product.getId());
        Assert.isTrue(product1.isDeleteStatus());
    }

}
