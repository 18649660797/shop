/**
 * Copyright (c) 2015 云智盛世
 * Created with QueryServiceImpl.
 */
package top.gabin.shop.core.jpa.criteria.service.query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.gabin.shop.core.jpa.criteria.condition.CriteriaCondition;
import top.gabin.shop.core.jpa.criteria.dto.PageDTO;
import top.gabin.shop.core.jpa.criteria.uil.CriteriaQueryUtils;
import top.gabin.shop.core.utils.RenderUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Criteria Api查询实现类
 * @author linjiabin  on  15/11/13
 */
@Service("criteriaQueryService")
public class CriteriaQueryServiceImpl implements CriteriaQueryService {
    @PersistenceContext(unitName = "shopPU")
    private EntityManager em;


    public <T> Long count(Class<T> entityClass, CriteriaCondition condition) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.distinct(condition.isDistinct());
        List<Predicate> predicates = null == condition.getCallBack() ? new ArrayList<Predicate>() : condition.getCallBack().callback(cb, query, root);
        predicates.addAll(CriteriaQueryUtils.buildPredicateList(root, cb, condition.getConditions()));
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        return selectCount(cb, root, query);
    }

    private <T> Long selectCount(CriteriaBuilder cb, Root<T> root, CriteriaQuery<T> query) {
        Expression ex = cb.countDistinct(root);
        query.select(ex);
        Long totalSize = (Long) em.createQuery(query).getSingleResult();
        return totalSize;
    }

    public <T> PageDTO<T> queryPage(Class<T> entityClass, CriteriaCondition condition) {
        TypedQuery<T> tTypedQuery = typedQuery(entityClass, condition);
        Integer start = condition.getStart(), limit = condition.getLimit();
        if (limit == null) {
            limit = 15;
        }
        Integer page = (start / limit) + 1;;
        PageDTO<T> pageDTO = new PageDTO<T>(page, limit, 0, tTypedQuery.getResultList());
        Long count = count(entityClass, condition);
        pageDTO.setTotalSize(count);
        return pageDTO;
    }

    public <T> Map<String, Object> queryPage(Class<T> entityClass, HttpServletRequest request, String pros) {
        CriteriaCondition criteriaCondition = CriteriaQueryUtils.parseCondition(request);
        PageDTO<T> adminPageDTO = queryPage(entityClass, criteriaCondition);
        return RenderUtils.filterPageDataResult(adminPageDTO, pros);
    }

    public <T> List<T> query(Class<T> entityClass, CriteriaCondition condition) {
        TypedQuery<T> typedQuery = typedQuery(entityClass, condition);
        return typedQuery.getResultList();
    }

    public <T, Y> Y sum(Class<T> entityClass, Class<Y> targetClass, CriteriaCondition condition) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.distinct(condition.isDistinct());
        List<Predicate> predicates = null == condition.getCallBack() ? new ArrayList<Predicate>() : condition.getCallBack().callback(cb, query, root);
        predicates.addAll(CriteriaQueryUtils.buildPredicateList(root, cb, condition.getConditions()));
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        Y count = selectSum(cb, root, query, condition.getTargetPath());
        return count;
    }

    private <Y> Y selectSum(CriteriaBuilder builder, Root root, CriteriaQuery query, String sumPath) {
        String[] names = StringUtils.splitByWholeSeparator(sumPath, ".");
        Expression ex = builder.sum(CriteriaQueryUtils.getPath(root, names));
        query.select(ex);
        Y totalSize = (Y) em.createQuery(query).getSingleResult();
        return totalSize;
    }

    private <T> TypedQuery<T> typedQuery(Class<T> entityClass, CriteriaCondition condition) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.distinct(condition.isDistinct());
        List<Predicate> predicates = null == condition.getCallBack() ? new ArrayList<Predicate>() : condition.getCallBack().callback(cb, query, root);
        predicates.addAll(CriteriaQueryUtils.buildPredicateList(root, cb, condition.getConditions()));
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        query.select(root);
        List<Order> orders = CriteriaQueryUtils.parseOrderBy(cb, root, condition.getSort());
        if (orders.size() > 0) {
            query.orderBy(orders);
        }
        TypedQuery<T> typedQuery = em.createQuery(query);
        if (condition.getLimit() != null) {
            typedQuery.setMaxResults(condition.getLimit());
        }
        typedQuery.setFirstResult(condition.getStart());
        return typedQuery;
    }

    public <T> T singleQuery(Class<T> entityClass, CriteriaCondition condition) {
        List<T> query = query(entityClass, condition);
        return query == null || query.isEmpty() ? null : query.get(0);
    }

}
