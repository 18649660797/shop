package top.gabin.shop.core.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.gabin.shop.core.product.entity.Product;
import top.gabin.shop.core.product.entity.ProductSku;

/**
 * @author linjiabin on  16/8/4
 */
@Repository
public interface ProductSkuDao extends JpaRepository<ProductSku, Long> {
    @Query(value = "FROM ProductSku WHERE commodityCode = :commodityCode")
    ProductSku getByCommodityCode(@Param(value = "commodityCode") String commodityCode);
}
