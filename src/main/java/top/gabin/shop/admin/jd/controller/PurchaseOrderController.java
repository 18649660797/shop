/**
 * Copyright (c) 2016 云智盛世
 * Created with PurchaseOrderController.
 */
package top.gabin.shop.admin.jd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.gabin.shop.core.jpa.criteria.query.service.CriteriaQueryService;
import top.gabin.shop.core.product.entity.Product;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @author linjiabin on  16/8/8
 */
@Controller
@RequestMapping("/admin/jd/purchaseOrder/")
public class PurchaseOrderController {

    private final static String DIR = "admin/jd/purchaseOrder/";

    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService queryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView viewList() {
        ModelAndView modelAndView = new ModelAndView(DIR + "list");
        return modelAndView;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map dataList(HttpServletRequest request) {
        return queryService.queryPage(Product.class, request, "id,defaultSku.name name,defaultSku.salePrice salePrice");
    }

}
