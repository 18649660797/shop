/**
 * Copyright (c) 2016 云智盛世
 * Created with AppConfig.
 */
package top.gabin.shop.config.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Class description
 *
 * @author linjiabin on  16/5/4
 */
@Configuration
@ComponentScan(basePackages = {
        "top.gabin.core.jpa.criteria",
        "top.gabin.shop",
        "top.gabin.shop.**.service.impl",
        "top.gabin.shop.**.aspect"
})
@EnableScheduling
@EnableAspectJAutoProxy
@EnableCaching(proxyTargetClass = true)
public class AppConfig  {
    private static Logger logger = LoggerFactory.getLogger(AppConfig.class);
    @Autowired
    private Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public CacheManager cacheManager() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("override-ehcache.xml"));
        ehCacheManagerFactoryBean.setShared(true);
        ehCacheManagerFactoryBean.afterPropertiesSet();
        EhCacheCacheManager cacheCacheManager = new EhCacheCacheManager(ehCacheManagerFactoryBean.getObject());
        return cacheCacheManager;
    }

}