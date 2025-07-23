package idealo.model;

import idealo.domain.SkuType;

import java.math.BigDecimal;

public class Sku {

    SkuType skuType;
    BigDecimal price;
    BigDecimal discount;

    private Sku() {

    }

    private Sku(SkuType skuType, BigDecimal price) {
        this.skuType = skuType;
        this.price = price;
        this.discount = BigDecimal.ZERO;
    }

    public static Sku with(SkuType skuType, BigDecimal price) {
        return new Sku(skuType, price);
    }

    public SkuType getSkuType() {
        return skuType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    // for pro-ration logic - not used so far
    public BigDecimal getDiscount() {
        return discount;
    }
}
