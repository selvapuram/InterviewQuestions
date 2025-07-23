package idealo.resolvers;

import idealo.contract.PriceResolver;
import idealo.model.PricingDynamics;

import java.math.BigDecimal;

public class BaseResolver implements PriceResolver {

    public BaseResolver() {
    }

    @Override
    public BigDecimal resolve(PricingDynamics pricingDynamics) {
        return pricingDynamics.getSkuPrice();
    }
}
