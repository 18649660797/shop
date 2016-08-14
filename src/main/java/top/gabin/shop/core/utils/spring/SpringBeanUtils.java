package top.gabin.shop.core.utils.spring;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Locale;


/**
 * Created with IntelliJ IDEA.
 * User: 余昌兴
 * Date: 14-12-6
 * Time: 下午1:34
 * To change this template use File | Settings | File Templates.
 *
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext.
 */
@Component
public final class SpringBeanUtils implements ApplicationContextAware, DisposableBean {

	private static Logger logger = LoggerFactory.getLogger(SpringBeanUtils.class);
	
	/** globalApplicationContext */
	private static ApplicationContext globalApplicationContext = null;

	/**
	 * 不可实例化
	 */
	private SpringBeanUtils() {
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		if (SpringBeanUtils.globalApplicationContext != null) {
			logger.info("SpringBeanUtils中的globalApplicationContext被覆盖, 原有globalApplicationContext为:" + SpringBeanUtils.globalApplicationContext);
		}

        SpringBeanUtils.globalApplicationContext = applicationContext;
	}

	public void destroy() throws Exception {
		if (logger.isDebugEnabled()){
			logger.debug("清除SpringBeanUtils中的globalApplicationContext:" + globalApplicationContext);
		}
        globalApplicationContext = null;
	}

	/**
	 * 获取globalApplicationContext
	 * 
	 * @return globalApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		assertContextInjected();
		return globalApplicationContext;
	}
	
	public static String getRootRealPath(){
		String rootRealPath ="";
		try {
			rootRealPath=getApplicationContext().getResource("").getFile().getAbsolutePath();
		} catch (IOException e) {
			logger.warn("获取系统根目录失败");
		}
		return rootRealPath;
	}
	
	/**
	 * 获取实例
	 * 
	 * @param name
	 *            Bean名称
	 * @return 实例
	 */
	public static Object getBean(String name) {
		Assert.hasText(name);
		return globalApplicationContext.getBean(name);
	}
	
	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return globalApplicationContext.getBean(requiredType);
	}
	
	/**
	 * 获取实例
	 * 
	 * @param name
	 *            Bean名称
	 * @param type
	 *            Bean类型
	 * @return 实例
	 */
	public static <T> T getBean(String name, Class<T> type) {
		Assert.hasText(name);
		Assert.notNull(type);
		return globalApplicationContext.getBean(name, type);
	}

    /**
     *  判断 spring 应用上下文是否包含指定 bean
     *
     * @param beanName
     * @return
     */
    public static boolean containsBean(String beanName) {
        Assert.hasText(beanName);
        return globalApplicationContext.containsBean(beanName);
    }

	/**
	 * 获取国际化消息
	 * 
	 * @param code
	 *            代码
	 * @param args
	 *            参数
	 * @return 国际化消息
	 */
	public static String getMessage(String code, Object... args) {
		LocaleResolver localeResolver = getBean("localeResolver", LocaleResolver.class);
		Locale locale = localeResolver.resolveLocale(null);
		return globalApplicationContext.getMessage(code, args, locale);
	}

	/**
	 * 检查ApplicationContext不为空.
	 */
	private static void assertContextInjected() {
		Validate.validState(globalApplicationContext != null, "globalApplicationContext属性未注入, 请在配置文件xml中定义SpringBeanUtils.");
	}

	public static boolean isInitialize() {
		if (globalApplicationContext == null) {
			return false;
		}
		return true;
	}

	public static <T> T initialize(T bean) {
		AutowireCapableBeanFactory beanFactory = globalApplicationContext.getAutowireCapableBeanFactory();
		beanFactory.autowireBean(bean);
		return (T) beanFactory.initializeBean(bean, bean.getClass().getName());
	}
}