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
import top.gabin.shop.admin.jd.entity.PurchaseOrder;
import top.gabin.shop.admin.jd.entity.WareHouse;
import top.gabin.shop.admin.jd.form.PurchaseOrderImportForm;
import top.gabin.shop.admin.jd.service.JdPurchaseOrderService;
import top.gabin.shop.core.jpa.criteria.condition.CriteriaCondition;
import top.gabin.shop.core.jpa.criteria.dto.PageDTO;
import top.gabin.shop.core.jpa.criteria.service.delete.CriteriaDeleteService;
import top.gabin.shop.core.jpa.criteria.service.query.CriteriaQueryService;
import top.gabin.shop.core.jpa.criteria.uil.CriteriaQueryUtils;
import top.gabin.shop.core.utils.RenderUtils;
import top.gabin.shop.core.utils.excel.ImportExcel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    private CriteriaDeleteService deleteService;
    @Resource
    private JdPurchaseOrderService jdPurchaseOrderService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView viewList() {
        ModelAndView modelAndView = new ModelAndView(DIR + "list");
        List<WareHouse> wareHouses = queryService.query(WareHouse.class, new CriteriaCondition());
        modelAndView.addObject("wareHouses", wareHouses);
        return modelAndView;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map delete(HttpServletRequest request) {
        List<PurchaseOrder> orders = queryService.query(PurchaseOrder.class, CriteriaQueryUtils.parseCondition(request));
        jdPurchaseOrderService.delete(orders);
        return RenderUtils.getSuccessMap();
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map dataList(HttpServletRequest request) {
        return queryService.queryPage(PurchaseOrder.class, request, "id,orderNumber,submitDate,total,buyer,timeoutDate," +
                "provider.name provider,wareHouse.name warehouse,wareHouse.city city,wareHouse.detailAddress detailAddress," +
                "wareHouse.contact contact,wareHouse.telephone telephone,count");
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

    @RequestMapping(value = "/previewCheck")
    @ResponseBody
    public Map<String, Object> previewCheck(HttpServletRequest request) {
        try {
            List<Object> dataList = (List) request.getSession().getAttribute(IMPORT_DATA);
            PageDTO<Object> objectPageDTO = new PageDTO<Object>(1, 30000, dataList.size(), dataList);
            return RenderUtils.filterPageDataResult(objectPageDTO, "orderNumber,providerCN,providerName,commodityCode," +
                    "skuName,attribute,city,warehouseName,detailAddress,link,tel,takePrice," +
                    "itemCount,realItemCount,total,realTotal,buyer,orderTime,overdueDate");
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
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> check(HttpServletRequest request, String jsonData) {
//        JsonData data = JsonUtils.json2Bean(JsonData.class, jsonData);
        List<PurchaseOrderImportForm> dataList = (List<PurchaseOrderImportForm>) request.getSession().getAttribute(IMPORT_DATA);
        jdPurchaseOrderService.importOrder(dataList);
        request.getSession().removeAttribute(IMPORT_DATA);
        return RenderUtils.SUCCESS_RESULT;
    }

    @RequestMapping(value = "/analysis", method = RequestMethod.GET)
    public void analysis(HttpServletResponse response, HttpSession session) {
        try {
            List<PurchaseOrderImportForm> dataList = (List<PurchaseOrderImportForm>) session.getAttribute(IMPORT_DATA);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmSS");
            String path = sdf.format(new Date());
            mkdir("download");
            mkdir("download/zip");
            mkdir("download/tmp");
            String servletPath = "download/tmp/" + path;
            jdPurchaseOrderService.analysis(dataList, servletPath);
            zipDownload(servletPath, path, response);
            clearFile(new File("download/zip"));
            clearFile(new File("download/tmp"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出Excel文件出错", e);
        }
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public void excel(HttpServletResponse response, HttpSession session) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmSS");
            String path = sdf.format(new Date());
            mkdir("download");
            mkdir("download/zip");
            mkdir("download/tmp");
            String servletPath = "download/tmp/" + path;
            jdPurchaseOrderService.excel(servletPath);
            zipDownload(servletPath, path, response);
            clearFile(new File("download/zip"));
            clearFile(new File("download/tmp"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出Excel文件出错", e);
        }
    }

    private void mkdir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private void clearFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                clearFile(f);
            }
            file.delete();
        } else {
            file.delete();
        }
    }

    public void zipDownload(String servletFilePath, String fileName, HttpServletResponse response) throws Exception {
        String zipFileName = "download/zip/" + fileName + ".zip"; //打包后文件名字
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

    private class JsonData {
        private List<PurchaseOrderImportForm> data;

        public List<PurchaseOrderImportForm> getData() {
            return data;
        }

        public void setData(List<PurchaseOrderImportForm> data) {
            this.data = data;
        }
    }

}
