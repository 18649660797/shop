package top.gabin.shop.core.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.gabin.shop.core.product.entity.Product;

/**
 * @author linjiabin on  16/8/4
 */
@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
    @Query(value = "FROM Product WHERE defaultSku.commodityCode = :commodityCode")
    Product getProductByCommodityCode(@Param(value = "commodityCode") String commodityCode);
}
