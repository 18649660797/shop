package basic;
/**
 * Copyright (c) 2016 云智盛世
 * Created with top.gabin.blog.web.basic.BasicTestInterface.
 */

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.shop.config.java.AppConfig;
import top.gabin.shop.config.java.SecurityConfig;
import top.gabin.shop.config.java.WebMvcConfig;

/**
 * Class description
 *
 * @author linjiabin on  16/7/10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class, PersistenceTestConfig.class, SecurityConfig.class, WebMvcConfig.class})
@Transactional
public class BasicTestInterface {

}
