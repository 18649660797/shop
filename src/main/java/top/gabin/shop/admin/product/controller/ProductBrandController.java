/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductBrandController.
 */
package top.gabin.shop.admin.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.gabin.shop.core.jpa.criteria.service.query.CriteriaQueryService;
import top.gabin.shop.core.jpa.criteria.uil.CriteriaQueryUtils;
import top.gabin.shop.core.product.entity.ProductBrand;
import top.gabin.shop.core.product.form.ProductBrandForm;
import top.gabin.shop.core.product.service.ProductBrandService;
import top.gabin.shop.core.utils.RenderUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author linjiabin on  16/8/8
 */
@Controller
@RequestMapping("/admin/product/brand")
public class ProductBrandController {
    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService queryService;
    @Resource
    private ProductBrandService productBrandService;
    private final static String DIR = "admin/product/brand/";

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView viewList() {
        ModelAndView modelAndView = new ModelAndView(DIR + "list");
        return modelAndView;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map dataList(HttpServletRequest request) {
        return queryService.queryPage(ProductBrand.class, request, "id,name");
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView viewEdit(@PathVariable(value = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView(DIR + "edit");
        if (id != null) {
            ProductBrand productBrand = productBrandService.get(id);
            if (productBrand != null) {
                modelAndView.addObject("product", productBrand);
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Map edit(@PathVariable(value = "id") Long id, ProductBrandForm productBrandForm) {
        productBrandService.saveProductBrand(productBrandForm);
        return RenderUtils.getSuccessMap();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map edit(HttpServletRequest request) {
        List<ProductBrand> productBrandList = queryService.query(ProductBrand.class, CriteriaQueryUtils.parseCondition(request));
        productBrandService.delete(productBrandList);
        return RenderUtils.getSuccessMap();
    }

}
