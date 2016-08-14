/**
 * Copyright (c) 2016 云智盛世
 * Created with PurchaseOrderController.
 */
package top.gabin.shop.admin.jd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.gabin.shop.admin.jd.entity.PurchaseOrderItem;
import top.gabin.shop.core.jpa.criteria.service.query.CriteriaQueryService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author linjiabin on  16/8/8
 */
@Controller
@RequestMapping("/admin/jd/purchaseOrder/item")
public class PurchaseOrderItemController {
    private final static String DIR = "admin/jd/purchaseOrder/item";

    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService queryService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map dataList(HttpServletRequest request) {
        return queryService.queryPage(PurchaseOrderItem.class, request, "quantity,realQuantity,salePrice,realPrice," +
                "productSku.name skuName,productSku.commodityCode commodityCode,productSku.boxSku boxSku," +
                "order.orderNumber orderNumber");
    }

}
