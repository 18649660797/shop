/**
 * Copyright (c) 2016 云智盛世
 * Created with JdStockProductService.
 */
package top.gabin.shop.admin.jd.service;

import top.gabin.shop.admin.jd.form.ProductImportForm;

import java.util.List;

/**
 *
 * @author linjiabin on  16/8/8
 */
public interface JdStockProductService {
    void importStockProduct(List<ProductImportForm> dataList);
}
