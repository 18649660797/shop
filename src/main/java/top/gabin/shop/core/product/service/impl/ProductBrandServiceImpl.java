/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductBrandServiceImpl.
 */
package top.gabin.shop.core.product.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.shop.core.product.dao.ProductBrandDao;
import top.gabin.shop.core.product.entity.ProductBrand;
import top.gabin.shop.core.product.form.ProductBrandForm;
import top.gabin.shop.core.product.service.ProductBrandService;

import javax.annotation.Resource;

/**
 *
 * @author linjiabin on  16/8/8
 */
@Service
public class ProductBrandServiceImpl implements ProductBrandService {

    @Resource
    private ProductBrandDao productBrandDao;

    public ProductBrand getProductBrand(Long brandId) {
        return productBrandDao.getOne(brandId);
    }

    public ProductBrand getByName(String name) {
        return productBrandDao.getByName(name);
    }

    @Transactional
    public ProductBrand saveProductBrand(ProductBrandForm productBrandForm) {
        ProductBrand productBrand = null;
        if (productBrandForm.getId() != null) {
            productBrand = productBrandDao.getOne(productBrandForm.getId());
        }
        if (productBrand == null) {
            productBrand = new ProductBrand();
        }
        productBrand.setName(productBrandForm.getName());
        return productBrandDao.save(productBrand);
    }

}
