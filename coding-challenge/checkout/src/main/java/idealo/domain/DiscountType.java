package idealo.domain;

import java.math.BigDecimal;

public enum DiscountType {
    FLAT {
        @Override
        public BigDecimal apply(BigDecimal price, BigDecimal value) {
            return price.subtract(value);
        }
    },
    PERCENTAGE {
        @Override
        public BigDecimal apply(BigDecimal price, BigDecimal value) {
            return price.subtract(price.scaleByPowerOfTen(-2).multiply(value));
        }
    },

    BASE {
        @Override
        public BigDecimal apply(BigDecimal price, BigDecimal value) {
            return price;
        }
    };

    abstract public BigDecimal apply(BigDecimal price, BigDecimal value);
}
