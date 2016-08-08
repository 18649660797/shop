/**
 * Copyright (c) 2016 云智盛世
 * Created with AdminIndexController.
 */
package top.gabin.shop.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author linjiabin on  16/8/4
 */
@Controller
@RequestMapping("/admin")
public class AdminIndexController {
    private static final String DIR = "admin/";
    @RequestMapping({"/", "", "/index"})
    public ModelAndView viewIndex() {
        return new ModelAndView(DIR + "main");
    }
}
