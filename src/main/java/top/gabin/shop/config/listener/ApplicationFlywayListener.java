package top.gabin.shop.config.listener;

import org.apache.commons.io.IOUtils;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.io.InputStream;

/**
 * User: altchen
 * Date: 15/1/11
 * Time: 上午11:25
 */
public class ApplicationFlywayListener implements ServletContextListener {
    private static Logger logger = LoggerFactory.getLogger(ApplicationFlywayListener.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            InitialContext ctx = new InitialContext();
            DataSource dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/web");
            Flyway flyway = new Flyway();
            flyway.setBaselineOnMigrate(true);//指定生成一条初始版本的记录
            flyway.setBaselineVersion("2015.01.01.01.01.00");
            flyway.setBaselineDescription("初始版本");
            flyway.setValidateOnMigrate(true);
            flyway.setOutOfOrder(true);//指定如果有版本比现在早的也同时执行
            flyway.setEncoding("utf-8");
            flyway.setDataSource(dataSource);
            if (existsFile("fixed.txt")) {//如果有fixed.txt的标志代表需要repair
                flyway.repair();
            }
            flyway.migrate();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("执行数据库更新时出错。请处理，不然启动不了", e);
        }

    }

    private boolean existsFile(String fileName) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/db/migration/" + fileName);
        if (is != null) {
            IOUtils.closeQuietly(is);
            return true;
        } else {
            return false;
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
