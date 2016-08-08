package top.gabin.shop.core.product.constant;

/**
 * @author linjiabin on  16/8/8
 */
public enum ProductType {
    NORMAL("普通商品"),
    STOCK("仓储商品")
    ;

    private String label;

    ProductType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
