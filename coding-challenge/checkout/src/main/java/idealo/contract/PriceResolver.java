package idealo.contract;

import idealo.model.PricingDynamics;

import java.math.BigDecimal;

public interface PriceResolver {

    BigDecimal resolve(PricingDynamics pricingDynamics);
}
