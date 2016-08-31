package top.gabin.shop.admin.jd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.gabin.shop.admin.jd.entity.WareHouse;

/**
 * @author linjiabin on  16/8/13
 */
@Repository
public interface WareHouseDao extends JpaRepository<WareHouse, Long> {
    @Query("FROM WareHouse WHERE name = :name")
    WareHouse getByName(@Param(value = "name") String name);
    @Query("FROM WareHouse WHERE city = :city AND name = :name")
    WareHouse get(@Param(value = "city") String city, @Param(value = "name") String name);
}
