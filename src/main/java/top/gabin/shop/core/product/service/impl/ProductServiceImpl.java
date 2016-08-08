/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductServiceImp.
 */
package top.gabin.shop.core.product.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.shop.core.product.dao.ProductDao;
import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.product.entity.ProductSku;
import top.gabin.shop.core.product.form.ProductFormBuilder;
import top.gabin.shop.core.product.service.ProductService;

import javax.annotation.Resource;

/**
 *
 * @author linjiabin on  16/8/4
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductDao productDao;

    @Transactional
    public Product saveProduct(ProductFormBuilder productFormBuilder) {
        Long productId = productFormBuilder.getProductId();
        Product product = null;
        if (productId != null) {
            product = productDao.findOne(productId);
        }
        if (product == null) {
            product = new Product();
            product.setTimeWeight(System.currentTimeMillis());
            ProductSku productSku = new ProductSku();
            product.setDefaultSku(productSku);
        }
        Product build = productFormBuilder.build(product);
        return productDao.save(build);
    }

    public Product getProduct(Long productId) {
        return productDao.findOne(productId);
    }

    public void delete(Long productId) {
        Product one = productDao.findOne(productId);
        if (one != null) {
            one.setDeleteStatus(true);
            productDao.save(one);
        }
    }
}
