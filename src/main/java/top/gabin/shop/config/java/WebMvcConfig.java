/**
 * Copyright (c) 2016 云智盛世
 * Created with MvcConfig.
 */
package top.gabin.shop.config.java;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Locale;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {
        "top.gabin.shop.**.controller",
        "top.gabin.shop.**.aspect"
})
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ViewResolver resolver() {
        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
        // 后缀
        freeMarkerViewResolver.setSuffix(".ftl");
        // 暴露宏命令
        freeMarkerViewResolver.setExposeSpringMacroHelpers(true);
        // 暴露路径变量
        freeMarkerViewResolver.setExposePathVariables(true);
        // 暴露request属性
        freeMarkerViewResolver.setExposeRequestAttributes(true);
        // 暴露session属性
        freeMarkerViewResolver.setExposeSessionAttributes(true);
        // 设置request环境变量名称
        freeMarkerViewResolver.setRequestContextAttribute("request");
        // 设置头信息
        freeMarkerViewResolver.setContentType("text/html;charset=utf-8");
        freeMarkerViewResolver.setOrder(0);
        return freeMarkerViewResolver;
    }

    @Bean
    public FreeMarkerConfig freeMarkerConfig() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/views");
        freeMarkerConfigurer.setDefaultEncoding("utf-8");
        Properties properties = new Properties();
        // 模板缓存
        properties.put("default_encoding", "utf-8");
        properties.put("template_update_delay", "10");
        properties.put("locale", "zh_CN");
        properties.put("datetime_format", "yyyy-MM-dd HH:mm:ss");
        properties.put("number_format", "#################.#################");
        freeMarkerConfigurer.setFreemarkerSettings(properties);
        return freeMarkerConfigurer;
    }

    @Bean
    public LocaleContextResolver localeContextResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(new Locale("cn"));
        return cookieLocaleResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean(name = "messageSource")
    public MessageSource configureMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(5);
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    // file upload
    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

}
