/**
 * Copyright (c) 2016 云智盛世
 * Created with ListernerInitializer.
 */
package top.gabin.shop.config.init;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import top.gabin.shop.config.listener.ApplicationFlywayListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Class description
 *
 * @author linjiabin on  16/5/5
 */
@Order(1)
public class WebApplicationInizer implements WebApplicationInitializer {
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(ApplicationFlywayListener.class);

    }
}
