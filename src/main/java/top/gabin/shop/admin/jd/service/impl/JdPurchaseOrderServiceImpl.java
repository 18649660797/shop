/**
 * Copyright (c) 2016 云智盛世
 * Created with JsPurchaseOrderServiceImpl.
 */
package top.gabin.shop.admin.jd.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.shop.admin.jd.dao.ProviderDao;
import top.gabin.shop.admin.jd.dao.PurchaseOrderDao;
import top.gabin.shop.admin.jd.dao.PurchaseOrderItemDao;
import top.gabin.shop.admin.jd.dao.WareHouseDao;
import top.gabin.shop.admin.jd.entity.Provider;
import top.gabin.shop.admin.jd.entity.PurchaseOrder;
import top.gabin.shop.admin.jd.entity.PurchaseOrderItem;
import top.gabin.shop.admin.jd.entity.WareHouse;
import top.gabin.shop.admin.jd.form.PreOrderImportForm;
import top.gabin.shop.admin.jd.form.PurchaseOrderImportForm;
import top.gabin.shop.admin.jd.service.JdPurchaseOrderService;
import top.gabin.shop.common.money.Money;
import top.gabin.shop.core.product.dao.ProductDao;
import top.gabin.shop.core.product.dao.ProductSkuDao;
import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.product.entity.ProductSku;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author linjiabin on  16/8/10
 */
@Service
public class JdPurchaseOrderServiceImpl implements JdPurchaseOrderService {
    private static short commonFontSize = 11;

    @Resource
    private ProductDao productDao;
    @Resource
    private ProductSkuDao skuDao;
    @Resource
    private ProviderDao providerDao;
    @Resource
    private WareHouseDao wareHouseDao;
    @Resource
    private PurchaseOrderDao purchaseOrderDao;
    @Resource
    private PurchaseOrderItemDao orderItemDao;

    public void analysis(List<PurchaseOrderImportForm> dataList, String servletPath) throws IOException {
        new File(servletPath).mkdir();
        Map<String, List<PurchaseOrderImportForm>> group = buildGroup(dataList);
        analysisOrder(group, servletPath);
        analysisBox(group, servletPath);
    }

    private void analysisBox(Map<String, List<PurchaseOrderImportForm>> group, String servletPath) {
        commonFontSize = (short) 11;
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
                HSSFFont fontAt = hssfWorkbook.getFontAt((short) 0);
                fontAt.setFontHeightInPoints(commonFontSize);
                fontAt.setFontName("宋体");
                HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
                HSSFCellStyle defaultCellStyle = hssfWorkbook.createCellStyle();
                defaultCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                sheet.setDefaultColumnStyle(0, defaultCellStyle);
                sheet.setDefaultColumnStyle(1, defaultCellStyle);
                sheet.setDefaultColumnStyle(2, defaultCellStyle);
                sheet.setDefaultColumnStyle(3, defaultCellStyle);
                sheet.setDefaultRowHeight((short) (256 * 2));
                sheet.setColumnWidth(0, 256 * 18);
                sheet.setColumnWidth(1, 256 * 16);
                sheet.setColumnWidth(2, 256 * 18);
                sheet.setColumnWidth(3, 256 * 16);
                sheet.setColumnWidth(4, 256 * 30);
                sheet.setColumnWidth(5, 256 * 15);
                sheet.setColumnWidth(6, 256 * 15);
                String city = null;
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
                        if (city == null) {
                            city = purchaseOrderImportForm.getCity();
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
                        setValue(row, 6, city);
                    }
                }
                String path = servletPath + city + "仓";
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }
                String filePath = path + "/" + key + city + "仓 装箱明细.xls";
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
        commonFontSize = (short) 12;
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
                HSSFCellStyle defaultCellStyle = hssfWorkbook.createCellStyle();
                defaultCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                sheet.setDefaultColumnStyle(0, defaultCellStyle);
                sheet.setDefaultColumnStyle(1, defaultCellStyle);
                sheet.setDefaultColumnStyle(2, defaultCellStyle);
                sheet.setDefaultColumnStyle(3, defaultCellStyle);
                HSSFFont fontAt = hssfWorkbook.getFontAt((short) 0);
                fontAt.setFontHeightInPoints(commonFontSize);
                fontAt.setFontName("宋体");
                sheet.setDefaultRowHeight((short) (256 * 2));
                sheet.setDefaultColumnWidth(12);
                sheet.setColumnWidth(0, 256 * 12);
                sheet.setColumnWidth(1, 256 * 40);
                sheet.setColumnWidth(2, 256 * 12);
                sheet.setColumnWidth(3, 256 * 30);
                String city = null;
                int j = 0;
                // 合并第一行
                int h = 0;
                //在sheet里增加合并单元格
                sheet.addMergedRegion(new CellRangeAddress(h, h++, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(h, h++, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(h, h++, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(h, h++, 0, 3));
                HSSFRow row = createRow(sheet, j++);
                setValue(row, 0, "入  库  单");
                setValue(row, 1, " ");
                setValue(row, 2, " ");
                setValue(row, 3, " ");
                HSSFCellStyle cellStyle = row.getCell(0).getCellStyle();
                cellStyle.setFont(createHeadTitleFont(hssfWorkbook));
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                HSSFRow row0 = createRow(sheet, j++);
                setValue(row0, 0, "商家姓名：福建省鑫森炭业股份有限公司          电话： 18065992275     传真： ");
                row0.getCell(0).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                setValue(row0, 1, " ");
                setValue(row0, 2, " ");
                setValue(row0, 3, " ");
                HSSFRow row1 = createRow(sheet, j++);
                setValue(row1, 0, "联络人:  黄正俊        地址：福建省邵武市经济技术开发区莲富路研发中心");
                row1.getCell(0).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                setValue(row1, 1, " ");
                setValue(row1, 2, " ");
                setValue(row1, 3, " ");
                HSSFRichTextString ts = new HSSFRichTextString("产品名称：                                        采购订单号：" + key);
                HSSFFont font1 = hssfWorkbook.createFont();
                font1.setColor(Font.COLOR_RED);
                font1.setFontName("宋体"); // 字体
                font1.setFontHeightInPoints(commonFontSize);
                ts.applyFont(51, ts.length(), font1);
                HSSFRow row2 = createRow(sheet, j++);
                setValue(row2, 0, ts);
                row2.getCell(0).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                setValue(row2, 1, "");
                setValue(row2, 2, "");
                row2.getCell(2).getCellStyle().setBorderLeft(HSSFCellStyle.BORDER_NONE);
                setValue(row2, 3, " ");
                row2.getCell(1).getCellStyle().setBorderRight(HSSFCellStyle.BORDER_NONE);
                row2.getCell(3).getCellStyle().setBorderLeft(HSSFCellStyle.BORDER_NONE);
                HSSFRow row3 = createRow(sheet, j++);
                setValue(row3, 0, "序号");
                setValue(row3, 1, "商品名称");
                setValue(row3, 2, "商品编码");
                setValue(row3, 3, "数量");
                row3.getCell(0).getCellStyle().setFont(createBoldFont(hssfWorkbook));
                row3.getCell(1).getCellStyle().setFont(createBoldFont(hssfWorkbook));
                row3.getCell(2).getCellStyle().setFont(createBoldFont(hssfWorkbook));
                row3.getCell(3).getCellStyle().setFont(createBoldFont(hssfWorkbook));
                int totalCount = 0;
                int num = 0;
                for (PurchaseOrderImportForm purchaseOrderImportForm : purchaseOrderImportForms) {
                    Integer itemCount = purchaseOrderImportForm.getItemCount();
                    if (itemCount <= 0) {
                        continue;
                    }
                    HSSFRow row4 = createRow(sheet, j++);
                    if (city == null) {
                        city = purchaseOrderImportForm.getCity();
                    }
                    setValue(row4, 0, ++num);
                    Product product = productDao.getProductByCommodityCode(purchaseOrderImportForm.getCommodityCode());
                    setValue(row4, 1, product == null ? purchaseOrderImportForm.getSkuName() + "(查找不到此商品)" : product.getDefaultSku().getName());
                    setValue(row4, 2, purchaseOrderImportForm.getCommodityCode());
                    setValue(row4, 3, itemCount);
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
                HSSFRow row5 = createRow(sheet, j);
                setValue(row5, 0, "预约成功！请牢记预约单号：16081400441" +
                        "\r送货时间：2016-08-14 15:00-17:30" +
                        "\r\r" +
                        "机构库房：北京-百货服装仓A1库（新）" +
                        "\r\r" +
                        "库房地址：北京市通州区张家湾镇张辛庄村东方化工厂南门200米" +
                        "\r\r" +
                        "库房电话：57835260-8030");
                HSSFCellStyle cellStyle1 = row5.getCell(0).getCellStyle();
                cellStyle1.setFont(createBoldFont(hssfWorkbook));
                cellStyle1.getFont(hssfWorkbook).setFontHeightInPoints((short) 12);
                setValue(row5, 1, "");
                setValue(row5, 2, "");
                setValue(row5, 3, "");
                row5.getCell(0).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                row5.getCell(0).getCellStyle().setWrapText(true);
                row5.getCell(0).getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
                row5.getCell(1).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                row5.getCell(1).getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
                row5.getCell(2).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                row5.getCell(2).getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
                row5.getCell(3).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                row5.getCell(3).getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
                row5.setHeight((short) (256 * 10));
                String path = servletPath + city + "仓";
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }
                String filePath = path + "/" + key + city + "仓 送货单.xls";
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

    private Font createHeadTitleFont(HSSFWorkbook hssfWorkbook) {
        Font headTitleFont = hssfWorkbook.createFont();
        headTitleFont.setUnderline(Font.U_SINGLE); //下划线;
        headTitleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headTitleFont.setFontName("宋体");
        headTitleFont.setFontHeightInPoints((short) 18);// 设置字体大小
        return headTitleFont;
    }


    private Font createBoldFont(HSSFWorkbook hssfWorkbook) {
        Font boldFont = hssfWorkbook.createFont();
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        boldFont.setFontName("宋体");
        boldFont.setFontHeightInPoints(commonFontSize);// 设置字体大小
        return boldFont;
    }

    private HSSFRow createRow(HSSFSheet sheet, int index) {
        return createRow(sheet, index, (short)400);
    }

    private HSSFRow createRow(HSSFSheet sheet, int index, short height) {
        HSSFRow row = sheet.createRow(index);
        row.setHeight(height);
        return row;
    }

    private void setValue(HSSFRow row1, int idx, Object o) {
        Cell cell = setValueNotStyle(row1, idx, o);
        cell.setCellStyle(getBorderStyle(row1.getSheet().getWorkbook()));

    }

    private Cell setValueNotStyle(HSSFRow row1, int idx, Object o) {
        Cell cell = row1.getCell(idx);
        if (cell == null) {
            cell = row1.createCell(idx);
        }
        cell.setCellStyle(getBorderStyle(row1.getSheet().getWorkbook()));
        if (o instanceof HSSFRichTextString) {
            cell.setCellValue((HSSFRichTextString) o);
        } else {
            if (o instanceof Boolean) {
                cell.setCellValue((Boolean) o);
            } else if (o instanceof Number) {
                cell.setCellValue(((Number) o).doubleValue());
            } else if (o instanceof Calendar) {
                cell.setCellValue((Calendar) o);
            } else {
                String content = o == null ? "" : o.toString();
                cell.setCellValue(content);
            }
        }
        return cell;
    }

    private Map<HSSFWorkbook, CellStyle> cacheMap = new HashMap<HSSFWorkbook, CellStyle>();

    private CellStyle getLeftBorderStyle(HSSFWorkbook hssfWorkbook) {
        CellStyle borderStyle = hssfWorkbook.createCellStyle();
        borderStyle.setAlignment(CellStyle.ALIGN_LEFT);
        borderStyle.setTopBorderColor(HSSFColor.BLACK.index);
        borderStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        borderStyle.setRightBorderColor(HSSFColor.BLACK.index);
        borderStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        borderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        return borderStyle;
    }

    private CellStyle getBorderStyle(HSSFWorkbook hssfWorkbook) {
        if (cacheMap.containsKey(hssfWorkbook)) {
            return cacheMap.get(hssfWorkbook);
        }
        CellStyle borderStyle = hssfWorkbook.createCellStyle();
        borderStyle.setAlignment(CellStyle.ALIGN_CENTER);
        borderStyle.setTopBorderColor(HSSFColor.BLACK.index);
        borderStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        borderStyle.setRightBorderColor(HSSFColor.BLACK.index);
        borderStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        borderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cacheMap.put(hssfWorkbook, borderStyle);
        return borderStyle;
    }


    private Map<String, List<PurchaseOrderImportForm>> buildGroup(List<PurchaseOrderImportForm> dataList) {
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
        return group;
    }

    @Transactional
    public void importOrder(List<PurchaseOrderImportForm> dataList) {
        Map<String, Provider> cacheProviderMap = new HashMap<String, Provider>();
        Map<String, WareHouse> cacheWareHouseMap = new HashMap<String, WareHouse>();
        Map<String, ProductSku> cacheWSkuMap = new HashMap<String, ProductSku>();
        Map<String, List<PurchaseOrderImportForm>> group = buildGroup(dataList);
        List<PurchaseOrderItem> purchaseOrderItemList = new ArrayList<PurchaseOrderItem>();
        for (String orderNumber : group.keySet()) {
            List<PurchaseOrderImportForm> purchaseOrderImportForms = group.get(orderNumber);
            PurchaseOrder purchaseOrder = purchaseOrderDao.getByOrderNumber(orderNumber);
            if (purchaseOrder != null) {
                continue;
            }
            purchaseOrder = new PurchaseOrder();
            purchaseOrder.setOrderNumber(orderNumber);
            PurchaseOrderImportForm one = purchaseOrderImportForms.get(0);
            purchaseOrder.setSubmitDate(one.getOrderTime());
            purchaseOrder.setTimeoutDate(one.getOverdueDate());
            String providerCN = one.getProviderCN();
            Provider provider = cacheProviderMap.get(providerCN);
            if (provider == null) {
                provider = providerDao.getByCn(providerCN);
            }
            if (provider == null) {
                provider = new Provider();
                provider.setName(one.getProviderName());
                provider.setCn(providerCN);
                provider = providerDao.save(provider);
                cacheProviderMap.put(providerCN, provider);
            } else {
                cacheProviderMap.put(providerCN, provider);
            }
            String warehouseName = one.getWarehouseName();
            String city = one.getCity();
            WareHouse wareHouse = cacheWareHouseMap.get(warehouseName);
            if (wareHouse == null) {
                wareHouse = wareHouseDao.getByName(warehouseName);
            }
            if (wareHouse == null) {
                wareHouse = new WareHouse();
                wareHouse.setName(warehouseName);
                wareHouse.setCity(city);
                wareHouse.setDetailAddress(one.getDetailAddress());
                wareHouse.setContact(one.getLink());
                wareHouse.setTelephone(one.getTel());
                wareHouse = wareHouseDao.save(wareHouse);
                cacheWareHouseMap.put(warehouseName, wareHouse);
            } else {
                cacheWareHouseMap.put(warehouseName, wareHouse);
            }
            purchaseOrder.setProvider(provider);
            purchaseOrder.setWareHouse(wareHouse);
            Set<String> attributeSet = new HashSet<String>();
            Set<String> buyerSet = new HashSet<String>();
            BigDecimal total = BigDecimal.ZERO;
            purchaseOrder = purchaseOrderDao.save(purchaseOrder);
            for (PurchaseOrderImportForm formDTO : purchaseOrderImportForms) {
                attributeSet.add(formDTO.getAttribute());
                buyerSet.add(formDTO.getBuyer());
                String commodityCode = formDTO.getCommodityCode();
                ProductSku productSku = cacheWSkuMap.get(commodityCode);
                if (productSku == null) {
                    productSku = skuDao.getByCommodityCode(commodityCode);
                }
                if (productSku == null) {
                    continue;
                }  else {
                    cacheWSkuMap.put(commodityCode, productSku);
                }
                productSku.setSalePrice(new Money(formDTO.getTakePrice()));
                PurchaseOrderItem item = new PurchaseOrderItem();
                item.setOrder(purchaseOrder);
                item.setProductSku(productSku);
                item.setSalePrice(new Money(formDTO.getTotal()));
                item.setRealPrice(new Money(formDTO.getRealTotal()));
                item.setQuantity(formDTO.getItemCount());
                item.setRealQuantity(formDTO.getRealItemCount());
                purchaseOrderItemList.add(item);
                total = total.add(formDTO.getTotal());
            }
            purchaseOrder.setAttribute(StringUtils.join(attributeSet, ","));
            purchaseOrder.setBuyer(StringUtils.join(buyerSet, ","));
            purchaseOrder.setTotal(total);
            purchaseOrderDao.save(purchaseOrder);
        }
        orderItemDao.save(purchaseOrderItemList);
    }

    @Transactional
    public void delete(List<PurchaseOrder> purchaseOrderList) {
        purchaseOrderDao.delete(purchaseOrderList);
    }

    public void excel(List<PurchaseOrder> purchaseOrderList, String servletPath) {
        new File(servletPath).mkdir();
        excelBox(purchaseOrderList, servletPath);
        excelOrder(purchaseOrderList, servletPath);
    }

    @Transactional
    public void importPreOrder(List<PreOrderImportForm> dataList) {
        List<String> orderNumberList = new ArrayList<String>();
        Map<String, PreOrderImportForm> preOrderImportFormMap = new HashMap<String, PreOrderImportForm>();
        for (PreOrderImportForm preOrderImportForm : dataList) {
            String orderNumber = preOrderImportForm.getOrderNumber();
            orderNumber = StringUtils.trim(orderNumber);
            orderNumberList.add(orderNumber);
            preOrderImportFormMap.put(orderNumber, preOrderImportForm);
        }
        List<PurchaseOrder> purchaseOrderList = purchaseOrderDao.findByOrderNumber(orderNumberList);
        for (PurchaseOrder purchaseOrder : purchaseOrderList) {
            String orderNumber = purchaseOrder.getOrderNumber();
            if (preOrderImportFormMap.containsKey(orderNumber)) {
                purchaseOrder.setRemark(preOrderImportFormMap.get(orderNumber).getRemark());
                purchaseOrderDao.save(purchaseOrder);
            }
        }
    }

    private void excelBox(List<PurchaseOrder> purchaseOrderList, String servletPath) {
        commonFontSize = (short) 12;
        FileOutputStream fileOut = null;
        servletPath = servletPath + "/装箱明细";
        new File(servletPath).mkdir();
        servletPath += "/";
        try {
            for (PurchaseOrder purchaseOrder : purchaseOrderList) {
                HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
                HSSFFont fontAt = hssfWorkbook.getFontAt((short) 0);
                fontAt.setFontHeightInPoints(commonFontSize);
                fontAt.setFontName("宋体");
                HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
                HSSFCellStyle defaultCellStyle = hssfWorkbook.createCellStyle();
                defaultCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                sheet.setDefaultColumnStyle(0, defaultCellStyle);
                sheet.setDefaultColumnStyle(1, defaultCellStyle);
                sheet.setDefaultColumnStyle(2, defaultCellStyle);
                sheet.setDefaultColumnStyle(3, defaultCellStyle);
                sheet.setDefaultRowHeight((short) (256 * 2));
                sheet.setColumnWidth(0, 256 * 15);
                sheet.setColumnWidth(1, 256 * 16);
                sheet.setColumnWidth(2, 256 * 18);
                sheet.setColumnWidth(3, 256 * 16);
                sheet.setColumnWidth(4, 256 * 30);
                sheet.setColumnWidth(5, 256 * 12);
                sheet.setColumnWidth(6, 256 * 12);
                String city = purchaseOrder.getWareHouse().getCity();
                String orderNumber = purchaseOrder.getOrderNumber();
                int j = 0;
                int totalBoxCount = 0;
                for (PurchaseOrderItem item : purchaseOrder.getPurchaseOrderItemList()) {
                    Integer itemCount = item.getQuantity();
                    ProductSku productSku = item.getProductSku();
                    int boxSku = productSku.getBoxSku();
                    if (itemCount <= 0) {
                        continue;
                    }
                    int boxCount = (int) Math.ceil(itemCount * 1D / boxSku);
                    totalBoxCount += boxCount;
                }
                HSSFRow row3 = createRow(sheet, j++, (short) 300);
                setValueNotStyle(row3, 0, "采购单号");
                setValueNotStyle(row3, 1, "总箱数");
                setValueNotStyle(row3, 2, "每箱序号");
                setValueNotStyle(row3, 3, "SKU编号");
                setValueNotStyle(row3, 4, "商品名称");
                setValueNotStyle(row3, 5, "数量");
                setValueNotStyle(row3, 6, "目的地");
                int num = 1;
                for (PurchaseOrderItem item : purchaseOrder.getPurchaseOrderItemList()) {
                    Integer itemCount = item.getQuantity();
                    if (itemCount <= 0) {
                        continue;
                    }
                    ProductSku productSku = item.getProductSku();
                    String skuName = productSku.getName();
                    int  boxSku = productSku.getBoxSku();
                    int boxCount = (int) Math.ceil(itemCount * 1D / boxSku);
                    int finalBoxQuantity = itemCount % boxSku;
                    for (int i = 1; i <= boxCount; i++) {
                        HSSFRow row = createRow(sheet, j++, (short) 300);
                        setValueNotStyle(row, 0, orderNumber);
                        setValueNotStyle(row, 1, totalBoxCount);
                        setValueNotStyle(row, 2, num++);
                        setValueNotStyle(row, 3, productSku.getCommodityCode());
                        setValueNotStyle(row, 4, skuName);
                        if (i == boxCount && finalBoxQuantity != 0) {
                            setValueNotStyle(row, 5, finalBoxQuantity);
                        } else {
                            setValueNotStyle(row, 5, boxSku);
                        }
                        setValueNotStyle(row, 6, city);
                    }
                }
                String path = servletPath + city + "仓";
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }
                String filePath = path + "/" + orderNumber + city + "仓 装箱明细.xls";
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


    private void excelOrder(List<PurchaseOrder> purchaseOrderList, String servletPath) {
        FileOutputStream fileOut = null;
        commonFontSize = (short) 12;
        servletPath = servletPath + "/送货单";
        new File(servletPath).mkdir();
        try {
            servletPath += "/";
            for (PurchaseOrder purchaseOrder : purchaseOrderList) {
                HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
                HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
                HSSFCellStyle defaultCellStyle = hssfWorkbook.createCellStyle();
                defaultCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                sheet.setDefaultColumnStyle(0, defaultCellStyle);
                sheet.setDefaultColumnStyle(1, defaultCellStyle);
                sheet.setDefaultColumnStyle(2, defaultCellStyle);
                sheet.setDefaultColumnStyle(3, defaultCellStyle);
                HSSFFont fontAt = hssfWorkbook.getFontAt((short) 0);
                fontAt.setFontHeightInPoints(commonFontSize);
                fontAt.setFontName("宋体");
                sheet.setDefaultRowHeight((short) (256 * 1.5));
                sheet.setDefaultColumnWidth(12);
                sheet.setColumnWidth(0, 256 * 12);
                sheet.setColumnWidth(1, 256 * 37);
                sheet.setColumnWidth(2, 256 * 12);
                sheet.setColumnWidth(3, 256 * 18);
                String city = purchaseOrder.getWareHouse().getCity();
                String orderNumber = purchaseOrder.getOrderNumber();
                int j = 0;
                // 合并第一行
                int h = 0;
                //在sheet里增加合并单元格
                sheet.addMergedRegion(new CellRangeAddress(h, h++, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(h, h++, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(h, h++, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(h, h++, 0, 3));
                HSSFRow row = createRow(sheet, j++);
                setValue(row, 0, "入  库  单");
                setValue(row, 1, " ");
                setValue(row, 2, " ");
                setValue(row, 3, " ");
                HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
                cellStyle.cloneStyleFrom(row.getCell(0).getCellStyle());
                cellStyle.setFont(createHeadTitleFont(hssfWorkbook));
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                row.getCell(0).setCellStyle(cellStyle);
                HSSFRow row0 = createRow(sheet, j++);
                setValue(row0, 0, "商家姓名：福建省鑫森炭业股份有限公司          电话： 18065992275     传真： ");
                row0.getCell(0).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                setValue(row0, 1, " ");
                setValue(row0, 2, " ");
                setValue(row0, 3, " ");
                HSSFRow row1 = createRow(sheet, j++);
                setValue(row1, 0, "联络人:  黄正俊        地址：福建省邵武市经济技术开发区莲富路研发中心");
                row1.getCell(0).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                setValue(row1, 1, " ");
                setValue(row1, 2, " ");
                setValue(row1, 3, " ");
                HSSFRichTextString ts = new HSSFRichTextString("产品名称：                                        采购订单号：" + orderNumber);
                HSSFFont font1 = hssfWorkbook.createFont();
                font1.setColor(Font.COLOR_RED);
                font1.setFontName("宋体"); // 字体
                font1.setFontHeightInPoints(commonFontSize);
                ts.applyFont(51, ts.length(), font1);
                HSSFRow row2 = createRow(sheet, j++);
                setValue(row2, 0, ts);
                row2.getCell(0).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                setValue(row2, 1, "");
                setValue(row2, 2, "");
                setValue(row2, 3, " ");
                HSSFRow row3 = createRow(sheet, j++);
                setValue(row3, 0, "序号");
                setValue(row3, 1, "商品名称");
                setValue(row3, 2, "商品编码");
                setValue(row3, 3, "数量");
                row3.getCell(0).getCellStyle().setFont(createBoldFont(hssfWorkbook));
                row3.getCell(1).getCellStyle().setFont(createBoldFont(hssfWorkbook));
                row3.getCell(2).getCellStyle().setFont(createBoldFont(hssfWorkbook));
                row3.getCell(3).getCellStyle().setFont(createBoldFont(hssfWorkbook));
                int totalCount = 0;
                int num = 0;
                for (PurchaseOrderItem item : purchaseOrder.getPurchaseOrderItemList()) {
                    Integer itemCount = item.getQuantity();
                    if (itemCount <= 0) {
                        continue;
                    }
                    HSSFRow row4 = createRow(sheet, j++);
                    setValue(row4, 0, ++num);
                    ProductSku productSku = item.getProductSku();
                    setValue(row4, 1, productSku.getName());
                    setValue(row4, 2, productSku.getCommodityCode());
                    setValue(row4, 3, itemCount);
                    totalCount += itemCount;
                }
                int tmp = 0;
                while (tmp++ < 4) {
                    HSSFRow rowTmp = createRow(sheet, j++);
                    int ii = 0;
                    setValue(rowTmp, ii++, " ");
                    setValue(rowTmp, ii++, " ");
                    setValue(rowTmp, ii++, " ");
                    setValue(rowTmp, ii++, " ");
                }
                HSSFRow rowFoot = createRow(sheet, j++);
                setValue(rowFoot, 0, "合计");
                setValue(rowFoot, 1, "");
                setValue(rowFoot, 2, "");
                setValue(rowFoot, 3, totalCount);
                sheet.addMergedRegion(new CellRangeAddress(j, j, 0, 3));
                HSSFRow row5 = createRow(sheet, j);
//                setValue(row5, 0, "预约成功！请牢记预约单号：16081400441" +
//                        "\r送货时间：2016-08-14 15:00-17:30" +
//                        "\r\r" +
//                        "机构库房：北京-百货服装仓A1库（新）" +
//                        "\r\r" +
//                        "库房地址：北京市通州区张家湾镇张辛庄村东方化工厂南门200米" +
//                        "\r\r" +
//                        "库房电话：57835260-8030");
                setValue(row5, 0, purchaseOrder.getRemark());
                HSSFCellStyle cellStyle4 = row5.getCell(0).getCellStyle();
                cellStyle4.setFont(createBoldFont(hssfWorkbook));
                cellStyle4.getFont(hssfWorkbook).setFontHeightInPoints((short) 12);
                setValue(row5, 1, "");
                setValue(row5, 2, "");
                setValue(row5, 3, "");
                row5.getCell(0).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                row5.getCell(0).getCellStyle().setWrapText(true);
                row5.getCell(0).getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
                row5.getCell(1).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                row5.getCell(1).getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
                row5.getCell(2).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                row5.getCell(2).getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
                row5.getCell(3).setCellStyle(getLeftBorderStyle(hssfWorkbook));
                row5.getCell(3).getCellStyle().setVerticalAlignment(CellStyle.ALIGN_CENTER);
                row5.setHeight((short) (256 * 10));
                String path = servletPath + city + "仓";
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }
                String filePath = path + "/" + orderNumber + city + "仓 送货单.xls";
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

}
