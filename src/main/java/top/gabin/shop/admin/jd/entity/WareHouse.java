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
@Table(name = "SHOP_WAREHOUSE")
public class WareHouse extends BasicEntity {
    @Id
    @Column(name = "ID")
    @TableGenerator(name = "warehouse_sequences", table = "SHOP_SEQUENCES", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "warehouse_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CITY")
    private String city;
    @Column(name = "DETAIL_ADDRESS")
    private String detailAddress;
    @Column(name = "CONCAT")
    private String contact;
    @Column(name = "TELEPHONE")
    private String telephone;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
