package top.gabin.shop.core.jpa.criteria.service.delete;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.shop.core.jpa.criteria.condition.CriteriaCondition;
import top.gabin.shop.core.jpa.criteria.uil.CriteriaQueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * criteria删除接口
 * @author linjiabin  on  15/11/13
 */
@Service
public class CriteriaDeleteServiceImpl implements CriteriaDeleteService {
    @PersistenceContext(unitName = "shopPU")
    private EntityManager em;

    @Transactional
    public <T> int delete(Class<T> entityClass, CriteriaCondition condition) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaDelete<T> criteriaDelete = criteriaBuilder.createCriteriaDelete(entityClass);
        Root<T> from = criteriaDelete.from(entityClass);
        List<Predicate> predicates = null == condition.getCallBack() ? new ArrayList<Predicate>()
                : condition.getCallBack().callback(criteriaBuilder, criteriaDelete, from);
        predicates.addAll(CriteriaQueryUtils.buildPredicateList(from, criteriaBuilder, condition.getConditions()));
        criteriaDelete.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
        Query query = em.createQuery(criteriaDelete);
        return query.executeUpdate();
    }

    @Transactional
    public <T> int delete(Class<T> entityClass, HttpServletRequest request) {
        CriteriaCondition criteriaCondition = CriteriaQueryUtils.parseCondition(request);
        return delete(entityClass, criteriaCondition);
    }

}
