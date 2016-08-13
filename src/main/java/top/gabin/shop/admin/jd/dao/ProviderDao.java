package top.gabin.shop.admin.jd.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.gabin.shop.admin.jd.entity.Provider;

/**
 * @author linjiabin on  16/8/13
 */
@Repository
public interface ProviderDao extends JpaRepository<Provider, Long> {
    @Query(value = "FROM Provider WHERE cn = :cn")
    Provider getByCn(@Param(value = "cn") String cn);
}
