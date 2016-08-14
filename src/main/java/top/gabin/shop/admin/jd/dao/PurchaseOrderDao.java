package top.gabin.shop.admin.jd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.gabin.shop.admin.jd.entity.PurchaseOrder;

import java.util.List;

/**
 * @author linjiabin on  16/8/13
 */
@Repository
public interface PurchaseOrderDao extends JpaRepository<PurchaseOrder, Long> {
    @Query(value = "FROM PurchaseOrder WHERE orderNumber = :orderNumber")
    PurchaseOrder getByOrderNumber(@Param(value = "orderNumber") String orderNumber);
    @Query(value = "FROM PurchaseOrder WHERE wareHouse.id = :wareHouseId")
    List<PurchaseOrder> findByWareHouse(@Param(value = "wareHouseId") Long wareHouseId);
    @Query(value = "FROM PurchaseOrder WHERE orderNumber in (:orderNumber)")
    List<PurchaseOrder> findByOrderNumber(@Param(value = "orderNumber") List<String> orderNumberList);
}
