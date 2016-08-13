/**
 * Copyright (c) 2016 云智盛世
 * Created with Provider.
 */
package top.gabin.shop.admin.jd.entity;

import top.gabin.shop.core.entity.BasicEntity;

import javax.persistence.*;

/**
 *
 * @author linjiabin on  16/8/9
 */
@Entity
@Table(name = "SHOP_PROVIDER")
public class Provider extends BasicEntity {
    @Id
    @Column(name = "ID")
    @TableGenerator(name = "provider_sequences", table = "SHOP_SEQUENCES", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "provider_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CN")
    private String cn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
}
