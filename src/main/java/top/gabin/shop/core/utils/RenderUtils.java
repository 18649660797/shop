/**
 * Copyright (c) 2016 云智盛世
 * Created with RenderUtils.
 */
package top.gabin.shop.core.utils;

/**
 * Class description
 *
 * @author linjiabin on  16/5/5
 */

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.ModelAndView;
import top.gabin.shop.core.jpa.criteria.query.dto.PageDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpServletResponse 数据输出 工具类
 */
public class RenderUtils {
    public static final Map<String, Object> SUCCESS_RESULT = getSuccessMap();
    public static final String TEXT_TYPE = "text/plain";
    //-- header 常量定义 --//
    private static final String HEADER_ENCODING = "encoding";
    private static final String HEADER_NOCACHE = "no-cache";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final boolean DEFAULT_NOCACHE = true;

    private static final String EXPIRES = "Expires";
    private static final String PRAGMA = "Pragma";
    private static final String CACHE_CONTROL = "Cache-Control";

    /**
     * 通过指定转成的属性,props：多个以,分隔
     *
     * @param objList
     * @param props
     * @return
     */
    public static List<Map<String, Object>> transListProp(List<? extends Object> objList, String props) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        if (objList != null) {
            for (Object obj : objList) {
                Map<String, Object> map = objectChangeToMap(obj, props);
                mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * 把 obj 按照 属性-->值 放到 map里
     *
     * @param obj
     * @param props
     * @return
     */
    public static Map<String, Object> objectChangeToMap(Object obj, String props) {
        Map<String, Object> map = new HashMap<String, Object>();
        String[] propArr = props.split(",");
        for (String prop : propArr) {
            if (StringUtils.isNotBlank(prop)) {
                try {
                    String key = prop;
                    if (prop.indexOf(" ") > -1) {
                        String[] split = prop.split(" ");
                        if (StringUtils.isNotBlank(split[0]) && StringUtils.isNotBlank(split[1])) {
                            key = split[1];
                            prop = split[0];
                        }
                    }
                    key = key.trim();
                    prop = prop.trim();
                    map.put(key, PropertyUtils.getProperty(obj, prop));
                } catch (NestedNullException exception) {
                    map.put(prop, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return map;
    }

    /**
     * 返回包含 分页信息 的 map
     *
     * @param list
     * @param props
     * @return
     */
    public static List filterPageData(List list, String props) {
        if (StringUtils.isNotBlank(props)) {
            list = transListProp(list, props);
        }
        return list;
    }

    public static Map<String, Object> filterPageDataResult(PageDTO<? extends Object> page, String props) {
        return filterPageDataResult(page, props, SUCCESS_RESULT);
    }

    /**
     * 返回分页信息 的 map
     *
     * @param page
     * @param props
     * @param message
     * @return
     */
    private static Map<String, Object> filterPageDataResult(PageDTO<? extends Object> page, String props, Map<String, Object> message) {
        Map<String, Object> result = new HashMap<String, Object>();
        List gridData = filterPageData(page.getContent(), props);
        result.put("rows", gridData);
        message.put("results", page.getTotalSize());
        if (message != null && !message.isEmpty()) result.putAll(message);
        return result;
    }

    /**
     * 返回 成功的 map
     *
     * @return
     */
    public static Map<String, Object> getSuccessMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", true);
        return result;
    }

    /**
     * 返回 失败的 结果 map
     *
     * @param message
     * @return
     */
    public static Map<String, Object> getFailMap(String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", false);
        result.put("message", message);
        return result;
    }

    /**
     * 返回 数据map
     *
     * @param data
     * @return
     */
    public static Map<String, Object> getSuccessResult(Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", true);
        result.put("data", data);
        return result;
    }

    /**
     * 导出excel
     *
     * @param response
     * @param workbook
     * @param fileName
     * @throws IOException
     */
    public static void renderExcel(HttpServletResponse response, HSSFWorkbook workbook, String fileName) throws IOException {
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
        OutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
        workbook.write(bufferedOutputStream);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    /**
     * 直接输出文本.
     *
     * @see (String, String, String...)
     */
    public static void renderText(HttpServletResponse response, final String text, final String... headers) {
        render(response, TEXT_TYPE, text, headers);
    }

    public static void render(HttpServletResponse response, final String contentType, final String content,
                              final String... headers) {
        initResponseHeader(response, contentType, headers);
        try {
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static HttpServletResponse initResponseHeader(HttpServletResponse response, final String contentType,
                                                         final String... headers) {
        //分析headers参数
        String encoding = DEFAULT_ENCODING;
        boolean noCache = DEFAULT_NOCACHE;
        for (String header : headers) {
            String headerName = StringUtils.substringBefore(header, ":");
            String headerValue = StringUtils.substringAfter(header, ":");

            if (StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
                encoding = headerValue;
            } else if (StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
                noCache = Boolean.parseBoolean(headerValue);
            } else {
                throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
            }
        }

        //设置headers参数
        String fullContentType = contentType + ";charset=" + encoding;
        response.setContentType(fullContentType);
        if (noCache) {
            setNoCacheHeader(response);
        }

        return response;
    }

    public static void setNoCacheHeader(HttpServletResponse response) {
        // Http 1.0 header
        response.setDateHeader(EXPIRES, 1L);
        response.addHeader(PRAGMA, "no-cache");
        // Http 1.1 header
        response.setHeader(CACHE_CONTROL, "no-cache, no-store, max-age=0");
    }

    public static void downloadFile(HttpServletResponse response, String realPath, String fileName) {
        try {
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            InputStream in = new FileInputStream(new File(realPath));
            OutputStream os = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len ;
            while ((len=in.read(buf))!=-1){
                os.write(buf,0,len);
            }
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
