package idealo.resolvers;

import idealo.contract.PriceResolver;
import idealo.domain.DiscountType;
import idealo.factory.PriceRule;
import idealo.model.PricingDynamics;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class BlackFridayResolver implements PriceResolver {

    private ZonedDateTime availDate;

    private BigDecimal discount;

    private DiscountType discountType;


    // stops no-arg constructor to enforce public arg
    private BlackFridayResolver() {

    }

    public BlackFridayResolver(PriceRule priceRule) {
        this.availDate = priceRule.getAvailDate().truncatedTo(ChronoUnit.DAYS);
        this.discount = priceRule.getDiscount();
        this.discountType = priceRule.getDiscountType();
    }

    @Override
    public BigDecimal resolve(PricingDynamics pricingDynamics) {
        if (this.availDate.isEqual(pricingDynamics.getPurchaseDate().truncatedTo(ChronoUnit.DAYS))) {
            return this.discountType.apply(pricingDynamics.getSkuPrice(), this.discount);
        }
        return pricingDynamics.getSkuPrice();
    }
}
