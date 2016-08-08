package top.gabin.shop.core.jpa.criteria.query.service;



import top.gabin.shop.core.jpa.criteria.query.condition.CriteriaCondition;
import top.gabin.shop.core.jpa.criteria.query.dto.PageDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * criteria查询接口
 * @author linjiabin  on  15/11/13
 */
public interface CriteriaQueryService {
    /**
     * @param entityClass 目标类，如ProductImpl.class，绑定实现类
     * @param condition   查询dto
     * @param <T>         目标类的泛型
     * @return 总记录数
     */
    <T> Long count(Class<T> entityClass, CriteriaCondition condition);

    /**
     * @param entityClass 目标类，如ProductImpl.class，绑定实现类
     * @param condition   查询dto
     * @param <T>         目标类的泛型
     * @return PageDTO    分页对象
     */
    <T> PageDTO<T> queryPage(Class<T> entityClass, CriteriaCondition condition);

    <T> Map<String, Object> queryPage(Class<T> entityClass, HttpServletRequest request, String pros);

    /**
     * 查询集合列表
     * @param entityClass 目标类，如ProductImpl.class，绑定实现类
     * @param condition   查询dto
     * @param <T>         目标类的泛型
     * @return 查询集合列表
     */
    <T> List<T> query(Class<T> entityClass, CriteriaCondition condition);

    <T> T singleQuery(Class<T> entityClass, CriteriaCondition condition);

    /**
     * 查询统计总数
     * @param entityClass 目标类，如ProductImpl.class，绑定实现类
     * @param targetClass 返回类，如BigDecimal
     * @param condition   查询dto
     * @param <T>         目标类的泛型
     * @param <Y>         返回类的泛型
     * @return
     */
    <T, Y> Y sum(Class<T> entityClass, Class<Y> targetClass, CriteriaCondition condition);
}
