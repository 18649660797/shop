/**
 * Copyright (c) 2016 云智盛世
 * Created with StockProductController.
 */
package top.gabin.shop.admin.jd.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import top.gabin.shop.admin.jd.form.ProductImportForm;
import top.gabin.shop.admin.jd.service.JdStockProductService;
import top.gabin.shop.core.jpa.criteria.dto.PageDTO;
import top.gabin.shop.core.jpa.criteria.service.query.CriteriaQueryService;
import top.gabin.shop.core.jpa.criteria.uil.CriteriaQueryUtils;
import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.product.service.ProductService;
import top.gabin.shop.core.utils.RenderUtils;
import top.gabin.shop.core.utils.excel.ImportExcel;
import top.gabin.shop.core.utils.json.JsonUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author linjiabin on  16/8/8
 */
@Controller
@RequestMapping("/admin/jd/stockProduct/")
public class StockProductController {
    private final static String DIR = "admin/jd/stockProduct/";
    private final static String IMPORT_DATA = "SESSION_STOCK_PRODUCT_IMPORT";

    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService queryService;
    @Resource
    private JdStockProductService jdStockProductService;
    @Resource
    private ProductService productService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView viewList() {
        ModelAndView modelAndView = new ModelAndView(DIR + "list");
        return modelAndView;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map dataList(HttpServletRequest request) {
        return queryService.queryPage(Product.class, request, "id,defaultSku.name name,defaultSku.boxSku boxSku,defaultSku.commodityCode commodityCode,productBrand.name brandName");
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map delete(HttpServletRequest request) {
        List<Product> productList = queryService.query(Product.class, CriteriaQueryUtils.parseCondition(request));
        productService.delete(productList);
        return RenderUtils.getSuccessMap();
    }

    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public String importView() {
        return DIR + "/import";
    }

    /**
     * 1 上传文件
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public Map productImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            ImportExcel importExcel = new ImportExcel(file, 1, 0);
            List<ProductImportForm> dataList = importExcel.getDataList(ProductImportForm.class);
            String brandName = null;
            for (ProductImportForm productImportForm : dataList) {
                if (StringUtils.isNoneBlank(productImportForm.getBrandName())) {
                    brandName = productImportForm.getBrandName();
                } else {
                    productImportForm.setBrandName(brandName);
                }
            }
            request.getSession().setAttribute(IMPORT_DATA, dataList);
            return RenderUtils.SUCCESS_RESULT;
        } catch (Exception e) {
            e.printStackTrace();
            return RenderUtils.getFailMap("导入数据有异常");
        }
    }

    @RequestMapping(value = "/previewCheck")
    @ResponseBody
    public Map<String, Object> previewCheck(HttpServletRequest request) {
        try {
            List<Object> dataList = (List) request.getSession().getAttribute(IMPORT_DATA);
            PageDTO<Object> objectPageDTO = new PageDTO<Object>(1, 1000, dataList.size(), dataList);
            return RenderUtils.filterPageDataResult(objectPageDTO, "num,brandName,skuName,boxSku,commodityCode");
        } catch (Exception e) {
            e.printStackTrace();
            return RenderUtils.getFailMap("获取数据有异常");
        }
    }

    // 2 预览数据
    @RequestMapping(value = "/preview", method = RequestMethod.GET)
    public ModelAndView preview() {
        return new ModelAndView(DIR + "/preview");
    }

    // 3 确认导入
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> check(HttpServletRequest request, String jsonData) {
        JsonData data = JsonUtils.json2Bean(JsonData.class, jsonData);
        jdStockProductService.importStockProduct(data.getData());
        request.getSession().removeAttribute(IMPORT_DATA);
        return RenderUtils.SUCCESS_RESULT;
    }

    private static class JsonData {
        private List<ProductImportForm> data;

        public List<ProductImportForm> getData() {
            return data;
        }

        public void setData(List<ProductImportForm> data) {
            this.data = data;
        }
    }

}
