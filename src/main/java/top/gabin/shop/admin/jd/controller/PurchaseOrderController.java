/**
 * Copyright (c) 2016 云智盛世
 * Created with PurchaseOrderController.
 */
package top.gabin.shop.admin.jd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import top.gabin.shop.admin.jd.form.PurchaseOrderImportForm;
import top.gabin.shop.admin.jd.service.JdPurchaseOrderService;
import top.gabin.shop.core.jpa.criteria.query.service.CriteriaQueryService;
import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.utils.RenderUtils;
import top.gabin.shop.core.utils.excel.ImportExcel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author linjiabin on  16/8/8
 */
@Controller
@RequestMapping("/admin/jd/purchaseOrder/")
public class PurchaseOrderController {
    private final static String IMPORT_DATA = "SESSION_PURCHASE_ORDER_IMPORT";
    private final static String DIR = "admin/jd/purchaseOrder/";

    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService queryService;
    @Resource
    private JdPurchaseOrderService jdPurchaseOrderService;

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

    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public String importView() {
        return DIR + "/import";
    }


    /**
     * 1 上传文件
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public Map productImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            ImportExcel importExcel = new ImportExcel(file, 1, 0);
            List<PurchaseOrderImportForm> dataList = importExcel.getDataList(PurchaseOrderImportForm.class);
            request.getSession().setAttribute(IMPORT_DATA, dataList);
            return RenderUtils.SUCCESS_RESULT;
        } catch (Exception e) {
            e.printStackTrace();
            return RenderUtils.getFailMap("导入数据有异常");
        }
    }

    @RequestMapping(value = "/analysis", method = RequestMethod.GET)
    public void analysis(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            List<PurchaseOrderImportForm> dataList = (List<PurchaseOrderImportForm>) session.getAttribute(IMPORT_DATA);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmSS");
            String path = sdf.format(new Date());
            String servletPath = "download/zip/" + path;
            jdPurchaseOrderService.analysis(dataList, servletPath);
            zipDownload(servletPath, path, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出Excel文件出错", e);
        }
    }

    public void zipDownload(String servletFilePath, String fileName, HttpServletResponse response) throws Exception {
        String zipFileName = servletFilePath + "/" + fileName + ".zip"; //打包后文件名字
        zip(zipFileName, new File(servletFilePath));
        RenderUtils.downloadFile(response, zipFileName, fileName + ".zip");
    }

    private void zip(String zipFileName, File inputFile) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip(out, inputFile, "");
        out.close();
    }

    private void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length;
            i++){
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }

    }


}
