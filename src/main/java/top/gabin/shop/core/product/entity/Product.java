/**
 * Copyright (c) 2016 云智盛世
 * Created with Product.
 */
package top.gabin.shop.core.product.entity;

import top.gabin.shop.core.constant.ArchiveStatus;
import top.gabin.shop.core.entity.BasicEntity;

import javax.persistence.*;
import java.util.List;

/**
 *
 * @author linjiabin on  16/8/4
 */
@Entity
@Table(name = "SHOP_PRODUCT")
public class Product extends BasicEntity {
    @Id
    @Column(name = "ID")
    @TableGenerator(name = "product_sequences", table = "shop_sequences", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "product_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Embedded
    private ArchiveStatus archiveStatus = new ArchiveStatus();
    @Column(name = "HIDDEN")
    private boolean hidden;
    @Column(name = "TIME_WEIGHT")
    private Long timeWeight;
    @OneToOne(targetEntity = ProductSku.class, mappedBy = "defaultProduct")
    private ProductSku defaultSku;
    @OneToMany(targetEntity = ProductSku.class, mappedBy="product")
    private List<ProductSku> allSkuList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArchiveStatus getArchiveStatus() {
        return archiveStatus;
    }

    public void setArchiveStatus(ArchiveStatus archiveStatus) {
        this.archiveStatus = archiveStatus;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Long getTimeWeight() {
        return timeWeight;
    }

    public void setTimeWeight(Long timeWeight) {
        this.timeWeight = timeWeight;
    }

    public ProductSku getDefaultSku() {
        return defaultSku;
    }

    public void setDefaultSku(ProductSku defaultSku) {
        this.defaultSku = defaultSku;
    }

    public List<ProductSku> getAllSkuList() {
        return allSkuList;
    }

    public void setAllSkuList(List<ProductSku> allSkuList) {
        this.allSkuList = allSkuList;
    }
}
