package top.gabin.shop.core.jpa.criteria.service.delete;


import top.gabin.shop.core.jpa.criteria.condition.CriteriaCondition;

import javax.servlet.http.HttpServletRequest;

/**
 * criteria删除接口
 * @author linjiabin  on  15/11/13
 */
public interface CriteriaDeleteService {
    <T> int delete(Class<T> entityClass, CriteriaCondition condition);
    <T> int delete(Class<T> entityClass, HttpServletRequest request);
}
