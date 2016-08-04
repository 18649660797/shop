/**
 * Copyright (c) 2016 云智盛世
 * Created with WebInitializer.
 */
package top.gabin.shop.config.init;

import org.springframework.web.filter.CharacterEncodingFilter;
import top.gabin.shop.config.filter.SiteMeshFilter;
import top.gabin.shop.config.java.AppConfig;
import top.gabin.shop.config.java.PersistenceConfig;
import top.gabin.shop.config.java.SecurityConfig;
import top.gabin.shop.config.java.WebMvcConfig;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

@Order(3)
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class[] getRootConfigClasses() {
        return new Class[]{AppConfig.class, SecurityConfig.class, PersistenceConfig.class};
    }

    @Override
    protected Class[] getServletConfigClasses() {
        return new Class[]{WebMvcConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/*", "/*", "/*"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{
                new CharacterEncodingFilter("UTF-8", true),
                new OpenEntityManagerInViewFilter(),
                new SiteMeshFilter()
        };
    }

}
