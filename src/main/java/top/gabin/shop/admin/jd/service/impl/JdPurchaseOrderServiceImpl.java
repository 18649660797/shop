/**
 * Copyright (c) 2016 云智盛世
 * Created with JsPurchaseOrderServiceImpl.
 */
package top.gabin.shop.admin.jd.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import top.gabin.shop.admin.jd.form.PurchaseOrderImportForm;
import top.gabin.shop.admin.jd.service.JdPurchaseOrderService;
import top.gabin.shop.core.product.dao.ProductDao;
import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.product.entity.ProductSku;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author linjiabin on  16/8/10
 */
@Service
public class JdPurchaseOrderServiceImpl implements JdPurchaseOrderService {
    @Resource
    private ProductDao productDao;

    public void analysis(List<PurchaseOrderImportForm> dataList, String servletPath) throws IOException {
        new File(servletPath).mkdir();
        Map<String, List<PurchaseOrderImportForm>> group = new HashMap<String, List<PurchaseOrderImportForm>>();
        for (PurchaseOrderImportForm form : dataList) {
            String orderNumber = form.getOrderNumber();
            List<PurchaseOrderImportForm> purchaseOrderImportForms = group.get(orderNumber);
            if (purchaseOrderImportForms == null) {
                purchaseOrderImportForms = new ArrayList<PurchaseOrderImportForm>();
                group.put(orderNumber, purchaseOrderImportForms);
            }
            purchaseOrderImportForms.add(form);
        }
        analysisOrder(group, servletPath);
        analysisBox(group, servletPath);
    }

    private void analysisBox(Map<String, List<PurchaseOrderImportForm>> group, String servletPath) {
        FileOutputStream fileOut = null;
        try {
            servletPath = servletPath + "/装箱明细";
            new File(servletPath).mkdir();
            servletPath += "/";
            for (String key : group.keySet()) {
                if (StringUtils.isBlank(key)) {
                    continue;
                }
                List<PurchaseOrderImportForm> purchaseOrderImportForms = group.get(key);
                HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
                HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
                sheet.setColumnWidth(1, 256 * 15);
                sheet.setColumnWidth(2, 256 * 15);
                sheet.setColumnWidth(3, 256 * 15);
                sheet.setColumnWidth(4, 256 * 15);
                sheet.setColumnWidth(5, 256 * 30);
                sheet.setColumnWidth(6, 256 * 15);
                String provider = null;
                int j = 0;
                int totalBoxCount = 0;
                for (PurchaseOrderImportForm purchaseOrderImportForm : purchaseOrderImportForms) {
                    Integer itemCount = purchaseOrderImportForm.getItemCount();
                    if (itemCount <= 0) {
                        continue;
                    }
                    int boxSku = 0;
                    Product product = productDao.getProductByCommodityCode(purchaseOrderImportForm.getCommodityCode());
                    if (product != null) {
                        ProductSku defaultSku = product.getDefaultSku();
                        boxSku = defaultSku.getBoxSku();
                    }
                    int boxCount = (int) Math.ceil(itemCount * 1D / boxSku);
                    totalBoxCount += boxCount;

                }
                HSSFRow row3 = createRow(sheet, j++);
                setValue(row3, 0, "采购单号");
                setValue(row3, 1, "总箱数");
                setValue(row3, 2, "每箱序号");
                setValue(row3, 3, "SKU编号");
                setValue(row3, 4, "商品名称");
                setValue(row3, 5, "数量");
                setValue(row3, 6, "目的地");
                int num = 1;
                for (PurchaseOrderImportForm purchaseOrderImportForm : purchaseOrderImportForms) {
                    Integer itemCount = purchaseOrderImportForm.getItemCount();
                    if (itemCount <= 0) {
                        continue;
                    }
                    int boxSku = 0;
                    Product product = productDao.getProductByCommodityCode(purchaseOrderImportForm.getCommodityCode());
                    String skuName = "找不到这款商品";
                    if (product != null) {
                        ProductSku defaultSku = product.getDefaultSku();
                        boxSku = defaultSku.getBoxSku();
                        skuName = defaultSku.getName();
                    }
                    int boxCount = (int) Math.ceil(itemCount * 1D / boxSku);
                    int finalBoxQuantity = itemCount % boxSku;
                    for (int i = 1; i <= boxCount; i++) {
                        HSSFRow row = createRow(sheet, j++);
                        if (provider == null) {
                            provider = purchaseOrderImportForm.getProvider();
                        }
                        setValue(row, 0, key);
                        setValue(row, 1, totalBoxCount);
                        setValue(row, 2, num++);
                        setValue(row, 3, purchaseOrderImportForm.getCommodityCode());
                        setValue(row, 4, skuName);
                        if (i == boxCount && finalBoxQuantity != 0) {
                            setValue(row, 5, finalBoxQuantity);
                        } else {
                            setValue(row, 5, boxSku);
                        }
                        setValue(row, 6, provider);
                    }
                }
                String path = servletPath + provider + "仓";
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }
                String filePath = path + "/" + key + provider + "仓 装箱明细.xls";
                fileOut = new FileOutputStream(filePath);
                hssfWorkbook.write(fileOut);
                fileOut.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void analysisOrder(Map<String, List<PurchaseOrderImportForm>> group, String servletPath) {
        FileOutputStream fileOut = null;
        try {
            servletPath = servletPath + "/送货单";
            new File(servletPath).mkdir();
            servletPath += "/";
            for (String key : group.keySet()) {
                if (StringUtils.isBlank(key)) {
                    continue;
                }
                List<PurchaseOrderImportForm> purchaseOrderImportForms = group.get(key);
                HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
                HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
                sheet.setDefaultColumnWidth(12);
                sheet.setColumnWidth(1, 256 * 30);
                sheet.setColumnWidth(2, 256 * 12);
                sheet.setColumnWidth(3, 256 * 12);
                sheet.setColumnWidth(4, 256 * 12);
                String provider = null;
                int j = 0;
                // 合并第一行
                int h = 0;
                //在sheet里增加合并单元格
                sheet.addMergedRegion(new CellRangeAddress(h, h++, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(h, h++, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(h, h, 0, 1));
                sheet.addMergedRegion(new CellRangeAddress(h, h++, 2, 3));
                HSSFRow row0 = createRow(sheet, j++);
                setValue(row0, 0, "商家姓名：福建省鑫森炭业股份有限公司\t\t电话：\t18065992275\t传真： ");
                setValue(row0, 1, " ");
                setValue(row0, 2, " ");
                setValue(row0, 3, " ");
                HSSFRow row1 = createRow(sheet, j++);
                setValue(row1, 0, "联络人:  黄正俊\t\t地址：福建省邵武市经济技术开发区莲富路研发中心 ");
                setValue(row1, 1, " ");
                setValue(row1, 2, " ");
                setValue(row1, 3, " ");
                HSSFRichTextString ts = new HSSFRichTextString("采购订单号：" + key);
                HSSFFont font1 = hssfWorkbook.createFont();
                font1.setColor(Font.COLOR_RED);
                font1.setFontName("宋体"); // 字体
                font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
                ts.applyFont(6, ts.length(), font1);
                HSSFRow row2 = createRow(sheet, j++);
                setValue(row2, 0, "产品名称:");
                setValue(row2, 1, "");
                setValue(row2, 2, ts);
                row2.getCell(2).getCellStyle().setBorderLeft(HSSFCellStyle.BORDER_NONE);
                setValue(row2, 3, " ");
                row2.getCell(1).getCellStyle().setBorderRight(HSSFCellStyle.BORDER_NONE);
                row2.getCell(3).getCellStyle().setBorderLeft(HSSFCellStyle.BORDER_NONE);
                HSSFRow row3 = createRow(sheet, j++);
                setValue(row3, 0, "序号");
                setValue(row3, 1, "商品名称");
                setValue(row3, 2, "商品编码");
                setValue(row3, 3, "数量");
                int totalCount = 0;
                int num = 0;
                for (PurchaseOrderImportForm purchaseOrderImportForm : purchaseOrderImportForms) {
                    Integer itemCount = purchaseOrderImportForm.getItemCount();
                    if (itemCount <= 0) {
                        continue;
                    }
                    HSSFRow row = createRow(sheet, j++);
                    if (provider == null) {
                        provider = purchaseOrderImportForm.getProvider();
                    }
                    setValue(row, 0, ++num);
                    Product product = productDao.getProductByCommodityCode(purchaseOrderImportForm.getCommodityCode());
                    setValue(row, 1, product == null ? purchaseOrderImportForm.getSkuName() + "(查找不到此商品)" : product.getDefaultSku().getName());
                    setValue(row, 2, purchaseOrderImportForm.getCommodityCode());
                    setValue(row, 3, itemCount);
                    totalCount += itemCount;
                }
                int tmp = 0;
                while (tmp++ < 3) {
                    HSSFRow rowTmp = createRow(sheet, j++);
                    setValue(rowTmp, 0, "");
                    setValue(rowTmp, 1, "");
                    setValue(rowTmp, 2, "");
                    setValue(rowTmp, 3, "");
                }
                HSSFRow rowFoot = createRow(sheet, j++);
                setValue(rowFoot, 0, "合计");
                setValue(rowFoot, 1, "");
                setValue(rowFoot, 2, "");
                setValue(rowFoot, 3, totalCount);
                sheet.addMergedRegion(new CellRangeAddress(j, j, 0, 3));
                HSSFRow row = createRow(sheet, j);
                setValue(row, 0, "预约成功！请牢记预约单号：16081400441" +
                        "\r送货时间：2016-08-14 15:00-17:30" +
                        "\r\r" +
                        "机构库房：北京-百货服装仓A1库（新）" +
                        "\r\r" +
                        "库房地址：北京市通州区张家湾镇张辛庄村东方化工厂南门200米" +
                        "\r\r" +
                        "库房电话：57835260-8030");
                setValue(row, 1, "");
                setValue(row, 2, "");
                setValue(row, 3, "");
                row.getCell(0).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
                row.getCell(0).getCellStyle().setWrapText(true);
                row.getCell(0).getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
                row.getCell(1).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
                row.getCell(1).getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
                row.getCell(2).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
                row.getCell(2).getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
                row.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
                row.getCell(3).getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
                row.setHeight((short) (256 * 9));
                String path = servletPath + provider + "仓";
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }
                String filePath = path + "/" + key + provider + "仓 送货单.xls";
                fileOut = new FileOutputStream(filePath);
                hssfWorkbook.write(fileOut);
                fileOut.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static HSSFRow createRow(HSSFSheet sheet, int index) {
        HSSFRow row = sheet.createRow(index);
//        row.setRowStyle(getBorderStyle(sheet.getWorkbook()));
        return row;
    }

    private static void setValue(HSSFRow row1, int idx, Object o) {
        HSSFCell cell = row1.getCell(idx);
        if (cell == null) {
            cell = row1.createCell(idx);
        }
        cell.setCellStyle(getBorderStyle(row1.getSheet().getWorkbook()));
        if (o instanceof HSSFRichTextString) {
            cell.setCellValue((HSSFRichTextString) o);
        } else {
            String content = o == null ? "" : o.toString();
            cell.setCellValue(content);
        }
    }

    private static CellStyle getBorderStyle(HSSFWorkbook hssfWorkbook) {
        HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
        cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
        cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
        cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

}
