package idealo.resolvers;

import idealo.contract.PriceResolver;
import idealo.domain.DiscountType;
import idealo.factory.PriceRule;
import idealo.model.PricingDynamics;

import java.math.BigDecimal;

public class QuantityResolver implements PriceResolver {

    private int offerQuantity;

    private BigDecimal discount;

    private DiscountType discountType;

    // stops no-arg constructor to enforce public arg
    private QuantityResolver() {

    }

    public QuantityResolver(PriceRule priceRule) {
        this.offerQuantity = priceRule.getOfferQuantity();
        this.discount = priceRule.getDiscount();
        this.discountType = priceRule.getDiscountType();
    }

    @Override
    public BigDecimal resolve(PricingDynamics pricingDynamics) {
        BigDecimal totalPrice = pricingDynamics.getSkuPrice().multiply(BigDecimal.valueOf(pricingDynamics.getSkuQuantity()));
        int discountUnit = pricingDynamics.getSkuQuantity() / this.offerQuantity;
        BigDecimal totalDiscount = discount.multiply(BigDecimal.valueOf(discountUnit));
        return this.discountType.apply(totalPrice, totalDiscount);
    }
}
