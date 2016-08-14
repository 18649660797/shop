/**
 * Copyright (c) 2016 云智盛世
 * Created with JsPurchaseOrderService.
 */
package top.gabin.shop.admin.jd.service;

import top.gabin.shop.admin.jd.entity.PurchaseOrder;
import top.gabin.shop.admin.jd.form.PreOrderImportForm;
import top.gabin.shop.admin.jd.form.PurchaseOrderImportForm;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author linjiabin on  16/8/10
 */
public interface JdPurchaseOrderService {
    void analysis(List<PurchaseOrderImportForm> dataList, String servletPath) throws IOException;
    void importOrder(List<PurchaseOrderImportForm> dataList);
    void delete(List<PurchaseOrder> purchaseOrderList);
    void excel(List<PurchaseOrder> purchaseOrderList, String servletPath);
    void importPreOrder(List<PreOrderImportForm> dataList);
}
