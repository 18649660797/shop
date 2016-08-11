/**
 * Copyright (c) 2016 云智盛世
 * Created with BasicEntity.
 */
package top.gabin.shop.core.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础类,存放基础属性
 * @author linjiabin on  16/8/4
 */
public abstract class BasicEntity implements Serializable {
    @Column(name = "CREATE_TIME")
    private Date createTime;
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    @Column(name = "DELETE_STATUS")
    private boolean deleteStatus = false;
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

