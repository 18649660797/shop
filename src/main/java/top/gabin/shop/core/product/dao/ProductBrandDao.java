/**
 * Copyright (c) 2016 云智盛世
 * Created with ProductBrandDao.
 */
package top.gabin.shop.core.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.gabin.shop.core.product.entity.ProductBrand;

/**
 *
 * @author linjiabin on  16/8/8
 */
@Repository
public interface ProductBrandDao extends JpaRepository<ProductBrand, Long> {
    @Query(value = "FROM ProductBrand WHERE name = :name")
    ProductBrand getByName(@Param(value = "name") String name);
}
