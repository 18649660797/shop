/**
 * Copyright (c) 2016 云智盛世
 * Created with PurchaseOrderItemDao.
 */
package top.gabin.shop.admin.jd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.gabin.shop.admin.jd.entity.PurchaseOrderItem;

/**
 *
 * @author linjiabin on  16/8/13
 */
@Repository
public interface PurchaseOrderItemDao extends JpaRepository<PurchaseOrderItem, Long> {
}
