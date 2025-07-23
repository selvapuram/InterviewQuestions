package idealo.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class PricingDynamics {

    private Integer skuQuantity;
    private ZonedDateTime purchaseDate;
    private BigDecimal skuPrice;

    public PricingDynamics(Integer skuQuantity, ZonedDateTime purchaseDate, BigDecimal skuPrice) {
        this.skuQuantity = skuQuantity;
        this.purchaseDate = purchaseDate;
        this.skuPrice = skuPrice;
    }

    public Integer getSkuQuantity() {
        return skuQuantity;
    }

    public ZonedDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public BigDecimal getSkuPrice() {
        return skuPrice;
    }
}
