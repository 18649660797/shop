/**
 * Copyright (c) 2015 云智盛世
 * Created with CriteriaQueryUtils.
 */
package top.gabin.shop.core.jpa.criteria.uil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.util.WebUtils;
import top.gabin.shop.core.jpa.criteria.condition.CriteriaCondition;
import top.gabin.shop.core.jpa.criteria.condition.CriteriaConditionGroup;
import top.gabin.shop.core.jpa.criteria.condition.CriteriaConditionPojo;
import top.gabin.shop.core.jpa.criteria.constant.CriteriaOperation;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * 用来组装查询条件Map
 * @author linjiabin  on  15/11/13
 */
public class CriteriaQueryUtils {

    private static final Set<String> PARSE_PATTERNS = new HashSet<String>();
    static {
        PARSE_PATTERNS.add("yyyy-MM-dd");
        PARSE_PATTERNS.add("yyyy-MM-dd HH:mm:ss.SSS");
        PARSE_PATTERNS.add("yyyy-MM-dd HH:mm:ss");
        PARSE_PATTERNS.add("yyyyMMddHHmmss");
    }

    public static CriteriaCondition parseCondition(HttpServletRequest request, Map other) {
        CriteriaCondition condition = new CriteriaCondition();
        Map params = buildPageQueryMap(request, other);
        condition.setConditions(params);
        if (request != null) {
            condition.setDistinct("true".equals(request.getParameter("distinct")));
            condition.setSort(request.getParameter("sort"));
            String limitStr = request.getParameter("limit");
            if (StringUtils.isNotBlank(limitStr) && StringUtils.isNumeric(limitStr)) {
                condition.setLimit(Integer.parseInt(limitStr));
            }
            String startStr = request.getParameter("start");
            if (StringUtils.isNotBlank(startStr) && StringUtils.isNumeric(startStr)) {
                condition.setStart(Integer.parseInt(startStr));
            }
            String targetPath = request.getParameter("targetPath");
            if (StringUtils.isNotBlank(targetPath)) {
                condition.setTargetPath(targetPath);
            }
        }
        return condition;
    }

    public static CriteriaCondition parseCondition(HttpServletRequest request) {
        Map<String, Object> params = null;
        return parseCondition(request, params);
    }

    public static CriteriaCondition parseCondition(HttpServletRequest request, String... other) {
        Map<String, String> otherMap = new HashMap<String, String>();
        if (other != null) {
            int size = other.length / 2;
            for (int i = 0; i < size; i++) {
                otherMap.put(other[i * 2], other[i * 2 + 1]);
            }
        }
        return parseCondition(request, otherMap);
    }

    public static CriteriaCondition parseCondition(HttpServletRequest request, Map otherHashMap, String... other) {
        Map<String, String> otherMap = new HashMap<String, String>();
        if (other != null) {
            int size = other.length / 2;
            for (int i = 0; i < size; i++) {
                otherMap.put(other[i * 2], other[i * 2 + 1]);
            }
        }
        if (otherHashMap != null) {
            otherMap.putAll(otherHashMap);
        }
        return parseCondition(request, otherMap);
    }

    public static Map buildPageQueryMap(HttpServletRequest request, Map other) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (request != null) {
            params = WebUtils.getParametersStartingWith(request, "");
        }
        if (other != null) {
            params.putAll(other);
        }
        return params;
    }
    public static Map buildPageQueryMap(HttpServletRequest request) {
        Map params = null;
        return buildPageQueryMap(request, params);
    }
    public static Map buildPageQueryMap(HttpServletRequest request, String... other) {
        Map<String, String> otherMap = new HashMap<String, String>();
        if (other != null) {
            int size = other.length / 2;
            for (int i = 0; i < size; i++) {
                otherMap.put(other[i * 2], other[i * 2 + 1]);
            }
        }
        return buildPageQueryMap(request, otherMap);
    }

    public static Map buildPageQueryMap(HttpServletRequest request, Map otherHashMap, String... other) {
        Map<String, String> otherMap = new HashMap<String, String>();
        if (other != null) {
            int size = other.length / 2;
            for (int i = 0; i < size; i++) {
                otherMap.put(other[i * 2], other[i * 2 + 1]);
            }
        }
        if (otherHashMap != null) {
            otherMap.putAll(otherHashMap);
        }
        return buildPageQueryMap(request, otherMap);
    }

    public static List<Predicate> buildPredicateList(Root root, CriteriaBuilder cb, Map<String, Object> conditions) {
        List<Predicate> predicateList = new ArrayList<Predicate>();
        List<CriteriaConditionGroup> groups = buildConditionGroups(conditions);
        for (CriteriaConditionGroup group : groups) {
            List<Predicate> predicateList1 = buildPredicateList(root, cb, group);
            if (predicateList1 != null) {
                predicateList.addAll(predicateList1);
            }
        }
        return predicateList;
    }

    public static List<Predicate> buildPredicateList(Root root, CriteriaBuilder criteriaBuilder, CriteriaConditionGroup criteriaConditionGroup) {
        if (criteriaConditionGroup == null) {
            return null;
        }
        List<Predicate> predicateList = new ArrayList<Predicate>();
        String join = criteriaConditionGroup.getJoin();
        From from = StringUtils.isNotBlank(join) ? root.join(join, JoinType.LEFT) : root;
        for (CriteriaConditionPojo conditionEntity : criteriaConditionGroup.getAndConditions()) {
            String fieldName = conditionEntity.getField();
            CriteriaOperation operation = conditionEntity.getOperation();
            Object fieldValue = conditionEntity.getValue();
            String[] names = StringUtils.splitByWholeSeparator(fieldName, ".");
            Path expression = getPath(from, names);
            Predicate predicate = buildPredicate(criteriaBuilder, expression, operation, fieldValue);
            predicateList.add(predicate);
        }
        if (CriteriaConditionGroup.GROUP_OPERATION_OR.equals(criteriaConditionGroup.getGroupOperation())) {
            List<Predicate> tmpList = new ArrayList<Predicate>();
            tmpList.add(criteriaBuilder.or(predicateList.toArray(new Predicate[predicateList.size()])));
            return tmpList;
        }
        return predicateList;
    }

    public static Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Path expression, CriteriaOperation operation, Object fieldValue) {

        switch (operation) {
            case between:
                String[] values = StringUtils.splitByWholeSeparator((String)fieldValue, ",");
                if (Date.class.equals(expression.getJavaType())) {
                    Date startDate = parseDate(values[0]);
                    Date endDate = parseDate(values[1]);
                    return criteriaBuilder.between(expression, startDate, endDate);
                }
                return criteriaBuilder.between(expression, values[0], values[1]);
            case equals:
            case eq:
                if (Boolean.class.equals(expression.getJavaType())) {
                    Boolean dataValue = Boolean.parseBoolean((String) fieldValue);
                    return criteriaBuilder.equal(expression, dataValue);
                }
                if (Character.class.equals(expression.getJavaType())) {
                    Character dataValue = (fieldValue + "").charAt(0);
                    return criteriaBuilder.equal(expression, dataValue);
                }
                return criteriaBuilder.equal(expression, fieldValue);
            case en:
                if (Boolean.class.equals(expression.getJavaType())) {
                    Boolean dataValue = Boolean.parseBoolean((String) fieldValue);
                    return criteriaBuilder.equal(expression, dataValue);
                }
                if (Character.class.equals(expression.getJavaType())) {
                    Character dataValue = (fieldValue + "").charAt(0);
                    return criteriaBuilder.equal(expression, dataValue);
                }
                return criteriaBuilder.or(criteriaBuilder.equal(expression, fieldValue), criteriaBuilder.isNull(expression));
            case notEqual:
            case ne:
                if (Boolean.class.equals(expression.getJavaType())) {
                    Boolean dataValue = Boolean.parseBoolean((String) fieldValue);
                    return criteriaBuilder.notEqual(expression, dataValue);
                }
                if (Character.class.equals(expression.getJavaType())) {
                    Character dataValue = (fieldValue + "").charAt(0);
                    return criteriaBuilder.notEqual(expression, dataValue);
                }
                return criteriaBuilder.notEqual(expression, fieldValue);
            case greaterThan:
            case gt:
                if (Date.class.equals(expression.getJavaType())) {
                    Date dataValue = parseDate((String) fieldValue);
                    return criteriaBuilder.greaterThan(expression, dataValue);
                }
                return criteriaBuilder.greaterThan(expression, (Comparable) fieldValue);
            case greaterThanEqualTo:
            case ge:
                if (Date.class.equals(expression.getJavaType())) {
                    Date dataValue = parseDate((String) fieldValue);
                    return criteriaBuilder.greaterThanOrEqualTo(expression, dataValue);
                }
                return criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) fieldValue);
            case in:
                return expression.in(StringUtils.splitByWholeSeparator((String) fieldValue, ","));
            case notIn:
                return criteriaBuilder.not(expression.in(StringUtils.splitByWholeSeparator((String) fieldValue, ",")));
            case lessThan:
            case lt:
                if (Date.class.equals(expression.getJavaType())) {
                    Date dataValue = parseDate((String) fieldValue);
                    return criteriaBuilder.lessThan(expression, dataValue);
                }
                return criteriaBuilder.lessThan(expression, (Comparable) fieldValue);
            case lessThanEqualTo:
            case le:
                if (Date.class.equals(expression.getJavaType())) {
                    Date dataValue = parseDate((String) fieldValue);
                    return criteriaBuilder.lessThanOrEqualTo(expression, dataValue);
                }
                return criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) fieldValue);
            case like:
                return criteriaBuilder.like(expression, "%" + fieldValue + "%");
            case bw:
                return criteriaBuilder.like(expression, fieldValue + "%");
            case ew:
                return criteriaBuilder.like(expression, "%" + fieldValue);
            case isNull:
                return criteriaBuilder.isNull(expression);
            case isNotNull:
                return criteriaBuilder.isNotNull(expression);
            case isTrue:
                return criteriaBuilder.isTrue(expression);
            case isFalse:
                return criteriaBuilder.isFalse(expression);
            case gn:
                if (Date.class.equals(expression.getJavaType())) {
                    Date dataValue = parseDate((String) fieldValue);
                    return criteriaBuilder.greaterThanOrEqualTo(expression, dataValue);
                }
                return criteriaBuilder.or(criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) fieldValue), criteriaBuilder.isNull(expression));
        }
        return null;
    }

    public static Path getPath(From from, String[] names) {
        Path expression = from.get(names[0]);
        for (int i = 1; i < names.length; i++) {
            expression = expression.get(names[i]);
        }
        return expression;
    }

    public static Date parseDate(String str) {
        try {
            return DateUtils.parseDate(str, PARSE_PATTERNS.toArray(new String[PARSE_PATTERNS.size()]));
        } catch (ParseException e) {
            throw new RuntimeException("无法解析日期：" + str, e);
        }
    }

    public static List<CriteriaConditionGroup> buildConditionGroups(Map<String, Object> conditions) {
        List<CriteriaConditionGroup> groups = new ArrayList<CriteriaConditionGroup>();
        if (conditions == null) {
            return groups;
        }
        for (String key : conditions.keySet()) {
            Object value = conditions.get(key);
            if (value == null || StringUtils.isBlank(value.toString())) {
                continue;
            }
            groups.add(parsePojo(key, value.toString()));
        }
        return groups;
    }

    public static CriteriaConditionGroup parsePojo(String key, String value) {
        CriteriaConditionGroup group = new CriteriaConditionGroup();
        if (key.indexOf("_join_") > -1) {
            String[] tmpArr = StringUtils.splitByWholeSeparator(key, "_join_");
            group.setJoin(tmpArr[1]);
            key = tmpArr[0];
        }
        if (key.indexOf("_or_") > -1) {
            group.setGroupOperation(CriteriaConditionGroup.GROUP_OPERATION_OR);
            List<CriteriaConditionPojo> entities = buildOrConditionEntity(key, value);
            group.getAndConditions().addAll(entities);
            return group;
        } else {
            group.setGroupOperation(CriteriaConditionGroup.GROUP_OPERATION_AND);
            CriteriaConditionPojo entity;
            if (key.indexOf("_and_") > -1) {
                String[] tmpArr = StringUtils.splitByWholeSeparator(key, "_and_");
                String[] tmpArr2 = StringUtils.splitByWholeSeparator(value.toString(), "##");
                for (int i = 0; i < tmpArr.length; i++) {
                    entity = buildConditionEntity(tmpArr[i], tmpArr2[i]);
                    if (entity != null) {
                        group.getAndConditions().add(entity);
                    }
                }
                return group;
            } else {
                entity = buildConditionEntity(key, value);
                if (entity != null) {
                    group.getAndConditions().add(entity);
                    return group;
                }
            }
        }
        return null;
    }

    public static CriteriaConditionPojo buildConditionEntity(String key, Object value) {
        String[] tmpArr = StringUtils.splitByWholeSeparator(key, "_");
        CriteriaConditionPojo entity = new CriteriaConditionPojo();
        switch (tmpArr.length) {
            case 2:
                entity.setOperation(CriteriaOperation.valueOf(tmpArr[0]));
                entity.setField(tmpArr[1]);
                entity.setValue(value);
                return entity;
        }
        return null;
    }

    public static List<CriteriaConditionPojo> buildOrConditionEntity(String key, Object value) {
        List<CriteriaConditionPojo> entities = new ArrayList<CriteriaConditionPojo>();
        if (StringUtils.isNotBlank(key) && key.indexOf("_or_") > -1) {
            String[] tmpArr = key.split("_or_");
            String[] tmpArr2 = value.toString().split("##");
            CriteriaConditionPojo entity;
            for (int i = 0; i < tmpArr.length; i++) {
                entity = buildConditionEntity(tmpArr[i], tmpArr2[i]);
                if (entity != null) {
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    public static List<Order> parseOrderBy(CriteriaBuilder builder, Root root, String sort) {
        List<Order> orders = new ArrayList<Order>();
        if (StringUtils.isNotBlank(sort)) {
            String[] arr = sort.split(",");
            for (String oneSort : arr) {
                String[] oneSortArr = oneSort.split(" ");
                if (oneSortArr.length == 2) {
                    String name = oneSortArr[0];
                    String dir = oneSortArr[1];
                    String[] names = StringUtils.splitByWholeSeparator(name, ".");
                    Path path = getPath(root, names);
                    Order order = "asc".equals(dir) ? builder.asc(path) : builder.desc(path);
                    orders.add(order);
                }
            }
        }
        return orders;
    }

}
