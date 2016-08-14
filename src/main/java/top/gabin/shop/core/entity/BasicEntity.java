/**
 * Copyright (c) 2016 云智盛世
 * Created with BasicEntity.
 */
package top.gabin.shop.core.entity;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import top.gabin.shop.web.constant.HibernateFilter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础类,存放基础属性
 * @author linjiabin on  16/8/4
 */
// hibernate过滤器,在适当的地方会加上过滤条件
@FilterDef(name = HibernateFilter.DELETE_STATUS_FILTER)
@Filters({ @Filter(name = HibernateFilter.DELETE_STATUS_FILTER, condition = HibernateFilter.DELETE_STATUS_MP_NAME + " = 0") })
// 将所有注解映射到子类去,父类不会产生新表
@MappedSuperclass
public class BasicEntity implements Serializable {
    @Column(name = "CREATE_TIME")
    protected Date createTime;
    @Column(name = "UPDATE_TIME")
    protected Date updateTime;
    @Column(name = "DELETE_STATUS")
    protected Boolean deleteStatus = false;
    @PrePersist
    private void prePersist() {
        createTime = updateTime = new Date();
    }

    @PreUpdate
    private void preUpdate() {
        updateTime = new Date();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public boolean isDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}

