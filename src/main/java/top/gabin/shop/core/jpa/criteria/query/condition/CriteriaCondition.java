/**
 * Copyright (c) 2015 云智盛世
 * Created with CriteriaCondition.
 */
package top.gabin.shop.core.jpa.criteria.query.condition;



import top.gabin.shop.core.jpa.criteria.query.callback.CriteriaCallBack;

import java.util.Map;

/**
 * 查询dto
 * @author linjiabin  on  15/11/13
 */
public class CriteriaCondition {
    // 查询条件参数散列表 格式一般为 key:eq_id,value:1001
    private Map<String, Object> conditions;
    // 查询条件回调，一般用来建立外链接和子查询等比较复杂的逻辑
    private CriteriaCallBack callBack;
    // 目标路径
    private String targetPath;
    // sql start
    private Integer start = 0;
    // sql limit
    private Integer limit;
    // sql distinct
    private boolean distinct = false;
    // sql sort
    private String sort;

    public CriteriaCondition() {
    }

    public CriteriaCondition(Map<String, Object> conditions) {
        this.conditions = conditions;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Map<String, Object> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }

    public CriteriaCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CriteriaCallBack callBack) {
        this.callBack = callBack;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
}
