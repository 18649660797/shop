/**
 * Copyright (c) 2015 云智盛世
 * Created with CriteriaConditionGroup.
 */
package top.gabin.shop.core.jpa.criteria.query.condition;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合查询dto
 * @author linjiabin  on  15/11/13
 */
public class CriteriaConditionGroup {
    public static final String GROUP_OPERATION_OR = "or";
    public static final String GROUP_OPERATION_AND = "and";
    private String groupOperation;
    private List<CriteriaConditionPojo> andConditions = new ArrayList<CriteriaConditionPojo>();
    private String join;

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public String getGroupOperation() {
        return groupOperation;
    }

    public void setGroupOperation(String groupOperation) {
        this.groupOperation = groupOperation;
    }

    public List<CriteriaConditionPojo> getAndConditions() {
        return andConditions;
    }

    public void setAndConditions(List<CriteriaConditionPojo> andConditions) {
        this.andConditions = andConditions;
    }
}
