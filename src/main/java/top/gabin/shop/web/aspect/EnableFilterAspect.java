/**
 * Copyright (c) 2016 云智盛世
 * Created with EnableFilterAspect.
 */
package top.gabin.shop.web.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.shop.core.utils.spring.SpringBeanUtils;
import top.gabin.shop.web.constant.HibernateFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 切面定义:创建实体管理对象的时候,开启租户过滤
 * 手动开启或关闭请调用:TenancyUtils工具类的方法
 * @author linjiabin on  16/4/16
 */
@Aspect
@Component
public class EnableFilterAspect {
    @PersistenceContext(name = "entityManagerFactory")
    protected EntityManager em;

    /**
     * 打开实体管理器默认启用租户过滤
     * 实体管理器打开 Session打开
     *
     * @param joinPoint
     * @param retVal
     */
    @AfterReturning(pointcut = "bean(entityManagerFactory) && execution(* createEntityManager(..))", returning = "retVal")
    public void getEntityManagerAfter(JoinPoint joinPoint, Object retVal) {
        if (SpringBeanUtils.isInitialize() && retVal != null && EntityManager.class.isInstance(retVal)) {
            EntityManager em = (EntityManager) retVal;
            Session delegate = (Session) em.getDelegate();
            delegate.enableFilter(HibernateFilter.DELETE_STATUS_FILTER);
        }
    }

    /**
     * 打开事务之后，创建连接，启用数据过滤
     * 事务打开，Session打开
     *
     * @param joinPoint
     * @param transactional
     */
    @Before(value = "@annotation(transactional)")
    public void beginTransactionAfter(JoinPoint joinPoint, Transactional transactional) {
        if (SpringBeanUtils.isInitialize()) {
            HibernateEntityManagerFactory entityManagerFactory = SpringBeanUtils.getBean(HibernateEntityManagerFactory.class);
            Session currentSession = entityManagerFactory.getSessionFactory().getCurrentSession();
            Transaction transaction = currentSession.getTransaction();
            if (transaction.isActive()) {
                currentSession.enableFilter(HibernateFilter.DELETE_STATUS_FILTER);
            }
        }
    }


}
