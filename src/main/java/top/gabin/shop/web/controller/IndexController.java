/**
 * Copyright (c) 2016 云智盛世
 * Created with IndexController.
 */
package top.gabin.shop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author linjiabin on  16/8/3
 */
@Controller
@RequestMapping("")
public class IndexController {

    @RequestMapping({"/", "", "/index"})
    public ModelAndView viewIndex() {
        return new ModelAndView("index");
    }
}
