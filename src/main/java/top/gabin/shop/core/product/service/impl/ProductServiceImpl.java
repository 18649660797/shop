/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductServiceImp.
 */
package top.gabin.shop.core.product.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.shop.core.product.dao.ProductDao;
import top.gabin.shop.core.product.dao.ProductSkuDao;
import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.product.entity.ProductSku;
import top.gabin.shop.core.product.form.ProductBuilder;
import top.gabin.shop.core.product.service.ProductService;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author linjiabin on  16/8/4
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductDao productDao;
    @Resource
    private ProductSkuDao productSkuDao;

    @Transactional
    public Product saveProduct(ProductBuilder productFormBuilder) {
        Long productId = productFormBuilder.getId();
        Product product = null;
        if (productId != null) {
            product = productDao.findOne(productId);
        }
        if (product == null) {
            product = new Product();
            product.setTimeWeight(System.currentTimeMillis());
            product = productDao.save(product);
            ProductSku productSku = new ProductSku();
            productSku.setDefaultProduct(product);
            productSku = productSkuDao.save(productSku);
            product.setDefaultSku(productSku);
        }
        Product build = productFormBuilder.build(product);
        return productDao.save(build);
    }

    public Product getProduct(Long productId) {
        return productDao.findOne(productId);
    }

    public Product getProductByCommodityCode(String commodityCode) {
        return productDao.getProductByCommodityCode(commodityCode);
    }

    @Transactional
    public void delete(Long productId) {
        Product one = productDao.findOne(productId);
        if (one != null) {
            one.setDeleteStatus(true);
            productDao.save(one);
        }
    }

    @Transactional
    public void delete(List<Product> productList) {
        productDao.delete(productList);
    }

}
