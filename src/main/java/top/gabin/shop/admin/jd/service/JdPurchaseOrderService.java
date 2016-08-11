/**
 * Copyright (c) 2016 云智盛世
 * Created with JsPurchaseOrderService.
 */
package top.gabin.shop.admin.jd.service;

import top.gabin.shop.admin.jd.form.PurchaseOrderImportForm;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author linjiabin on  16/8/10
 */
public interface JdPurchaseOrderService {
    void analysis(List<PurchaseOrderImportForm> dataList, String servletPath) throws IOException;

}
