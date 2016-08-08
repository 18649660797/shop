/**
 * Copyright (c) 2015 云智盛世
 * Created with ExportExcel.
 *
 */
package top.gabin.shop.core.utils.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

/**
 * Class description
 *
 * @author sunpl on 15-11-16 上午11:09
 */
public class ExportExcel {

    /**
     * 这是一个通用的方法,将数据以EXCEL的形式输出
     * @param title
     *      表格标题名
     * @param headers
     *      表格属性列名数组
     * @param cellTypes
     *      单元格类型
     * @param dataList
     *      需要显示的数据集合。此方法支持的
     */
    public static HSSFWorkbook exportExcel(String title,String[] headers, int [] cellTypes, List<String[]> dataList){
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        //设置表格默认列宽度为18个字节
        sheet.setDefaultColumnWidth(18);
        //生成标题样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //生成标题字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short)12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //字体应用
        style.setFont(font);

        //生成文本内容样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        /*style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);*/
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //生成文本内容字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        //应用字体
        style2.setFont(font2);

        //产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for(int i = 0; i < headers.length; i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);//设置单元格样式(包含字体)
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);//把数据放到单元格中
        }

        //遍历集合数据,产生数据行
        for(int i = 0; i < dataList.size(); i++){
            row = sheet.createRow((i + 1));
            Object[] record = dataList.get(i);
            for(int j = 0 ; j < record.length; j++){
                HSSFCell cell = row.createCell(j);
                int cellType = cellTypes[j];
                cell.setCellType(cellType);
                String textValue = record[j].toString();
                if(cellType == Cell.CELL_TYPE_NUMERIC){
                    cell.setCellValue(Double.parseDouble(textValue));
                } else {
                    HSSFRichTextString richString = new HSSFRichTextString(textValue);
                    HSSFDataFormat format = workbook.createDataFormat();
                    style2.setDataFormat(format.getFormat("@"));
                    cell.setCellStyle(style2);
                    cell.setCellValue(richString);
                }
            }
        }

        return workbook;
    }
}
