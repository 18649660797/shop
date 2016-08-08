/**
 * Copyright (c) 2016 云智盛世
 * Created with JdStockProductService.
 */
package top.gabin.shop.admin.jd.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.shop.admin.jd.form.ProductImportForm;
import top.gabin.shop.admin.jd.service.JdStockProductService;
import top.gabin.shop.common.money.Money;
import top.gabin.shop.core.product.constant.ProductType;
import top.gabin.shop.core.product.dao.ProductBrandDao;
import top.gabin.shop.core.product.dao.ProductDao;
import top.gabin.shop.core.product.dao.ProductSkuDao;
import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.product.entity.ProductBrand;
import top.gabin.shop.core.product.entity.ProductSku;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author linjiabin on  16/8/8
 */
@Service
public class JdStockProductServiceImpl implements JdStockProductService {

    @Resource
    private ProductDao productDao;
    @Resource
    private ProductSkuDao productSkuDao;
    @Resource
    private ProductBrandDao productBrandDao;

    @Transactional
    public void importStockProduct(List<ProductImportForm> dataList) {
        Map<String, ProductBrand> brandMap = new HashMap<String, ProductBrand>();
        List<Product> productList = new ArrayList<Product>();
        for (ProductImportForm productImportForm : dataList) {
            String commodityCode = productImportForm.getCommodityCode();
            Product product = productDao.getProductByCommodityCode(commodityCode);
            if (product == null || product.getProductType() != ProductType.STOCK) {
                product = new Product();
                product.setProductType(ProductType.STOCK);
                product.setTimeWeight(System.currentTimeMillis());
                product = productDao.save(product);
                ProductSku productSku = new ProductSku();
                productSku.setDefaultProduct(product);
                productSku.setSalePrice(Money.ZERO);
                productSku.setName(productImportForm.getSkuName());
                productSku = productSkuDao.save(productSku);
                product.setDefaultSku(productSku);
            }
            ProductSku productSku = product.getDefaultSku();
            productSku.setName(productImportForm.getSkuName());
            productSku.setCommodityCode(commodityCode);
            productSku.setBoxSku(productImportForm.getBoxSku());
            String brandName = productImportForm.getBrandName();
            ProductBrand productBrand = brandMap.get(brandName);
            if (productBrand == null) {
                productBrand = productBrandDao.getByName(brandName);
            }
            if (productBrand == null) {
                productBrand = new ProductBrand();
                productBrand.setName(brandName);
                productBrand = productBrandDao.save(productBrand);
            }
            if (productBrand != null) {
                product.setProductBrand(productBrand);
                brandMap.put(brandName, productBrand);
            }
            productList.add(product);
        }
        productDao.save(productList);
    }

}
