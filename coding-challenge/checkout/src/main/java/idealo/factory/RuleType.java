package idealo.factory;

import idealo.contract.PriceResolver;
import idealo.resolvers.BaseResolver;
import idealo.resolvers.BlackFridayResolver;
import idealo.resolvers.QuantityResolver;

public enum RuleType {
        BASE {
            @Override
            public PriceResolver resolver(PriceRule priceRule) {
                return new BaseResolver();
            }
        },
        QUANTITY {
            @Override
            public PriceResolver resolver(PriceRule priceRule) {
                return new QuantityResolver(priceRule);
            }
        },
        BLACK_FRIDAY_DEAL {
            @Override
            public PriceResolver resolver(PriceRule priceRule) {
                return new BlackFridayResolver(priceRule);
            }
        };

        abstract public PriceResolver resolver(PriceRule priceRule);
    }