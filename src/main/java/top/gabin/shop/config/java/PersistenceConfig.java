/**
 * Copyright (c) 2016 云智盛世
 * Created with PersistenceConfig.
 */
package top.gabin.shop.config.java;

/**
 * Class description
 *
 * @author linjiabin on  16/5/4
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableSpringDataWebSupport
@EnableTransactionManagement
@PropertySource(value = {"classpath:application.properties"})
@EnableJpaRepositories(basePackages = {
        "top.gabin.shop.**.dao",
})
public class PersistenceConfig {
    @Autowired
    private Environment env;

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(entityManagerFactory());
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory());
        return jpaTransactionManager;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setDataSource(dataSource());
        factory.setPersistenceUnitName("shopPU");
        factory.setPackagesToScan("top.gabin.shop.**.entity");
        final Properties props = new Properties();
        props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        props.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        factory.setJpaProperties(props);
        factory.afterPropertiesSet();
        factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        return factory.getObject();
    }

    @Bean
    public DataSource dataSource() {
        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        DataSource dataSource = dsLookup.getDataSource("jdbc/web");
        return dataSource;
    }
}
