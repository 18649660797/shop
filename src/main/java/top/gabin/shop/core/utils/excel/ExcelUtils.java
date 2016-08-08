/**
 * Copyright (c) 2016 云智盛世
 * Created with ExcelUtils.
 */
package top.gabin.shop.core.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import top.gabin.shop.core.utils.RenderUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Class description
 *
 * @author linjiabin on  16/6/6
 */
public class ExcelUtils {

    public static void excel(HttpServletResponse response, List<Map<String, Object>> dataList, String[] pros) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("请假外出");
        int j = 0;
        for (Map<String, Object> data : dataList) {
            HSSFRow row = sheet.createRow(j++);
            int i = 0;
            for (String pro : pros) {
                Object o = data.get(pro);
                setValue(row, i++, o == null ? "" : o.toString());
            }
        }
        RenderUtils.renderExcel(response, workbook, "请假外出_" + System.currentTimeMillis());
    }

    private static void setValue(HSSFRow row1, int idx, Object o) {
        HSSFCell cell = row1.getCell(idx);
        if (cell == null) {
            cell = row1.createCell(idx);
        }
        String content = o == null ? "" : o.toString();
        cell.setCellValue(content);
    }

}
