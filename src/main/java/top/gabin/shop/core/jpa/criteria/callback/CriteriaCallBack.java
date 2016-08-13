package top.gabin.shop.core.jpa.criteria.callback;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * 查询回调，主要用来获取查询条件，可以用来建立外连接查询
 * @author linjiabin  on  15/11/13
 */
public interface CriteriaCallBack {
    List<Predicate> callback(CriteriaBuilder criteriaBuilder, CriteriaQuery query, Root root);
    List<Predicate> callback(CriteriaBuilder criteriaBuilder, CriteriaDelete criteriaDelete, Root root);
}
