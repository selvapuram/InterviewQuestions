package idealo.factory;

import idealo.domain.DiscountType;
import idealo.domain.SkuType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class PriceRule {

    final private ZonedDateTime availDate;
    final private Integer offerQuantity;
    final private SkuType skuType;
    final private DiscountType discountType;
    final private BigDecimal discount;
    final private RuleType ruleType;

    private PriceRule(Builder builder) {
        this.availDate = builder.availDate;
        this.offerQuantity = builder.offerQuantity;
        this.skuType = builder.skuType;
        this.discountType = builder.discountType;
        this.discount = builder.discount;
        this.ruleType = builder.ruleType;
    }

    public ZonedDateTime getAvailDate() {
        return availDate;
    }

    public int getOfferQuantity() {
        return offerQuantity;
    }

    public SkuType getSkuType() {
        return skuType;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public static class Builder {
        private final SkuType skuType;
        private final DiscountType discountType;
        private final RuleType ruleType;
        private ZonedDateTime availDate = null;
        private Integer offerQuantity = null;
        private BigDecimal discount = BigDecimal.ZERO;

        public Builder(SkuType skuType, DiscountType discountType, RuleType ruleType) {
            this.skuType = skuType;
            this.discountType = discountType;
            this.ruleType = ruleType;
        }

        public Builder withAvailDate(ZonedDateTime availDate) {
            this.availDate = availDate;
            return this;
        }

        public Builder withOfferQuantity(Integer offerQuantity) {
            this.offerQuantity = offerQuantity;
            return this;
        }

        public Builder withDiscount(BigDecimal discount) {
            this.discount = discount;
            return this;
        }

        public PriceRule build() {
            return new PriceRule(this);
        }
    }


    public static PriceRule basicPriceRule(SkuType skuId) {
        return new PriceRule.Builder(skuId, DiscountType.BASE, RuleType.BASE).withDiscount(BigDecimal.ZERO).build();
    }

    public static PriceRule quantityRule(SkuType skuId, BigDecimal discount, Integer offerQuantity) {
        return new PriceRule.Builder(skuId, DiscountType.FLAT, RuleType.QUANTITY)
                .withDiscount(discount)
                .withOfferQuantity(offerQuantity)
                .build();
    }

    public static PriceRule blackFridayRule(SkuType skuId, BigDecimal discount, ZonedDateTime availDate) {
        return new PriceRule.Builder(skuId, DiscountType.PERCENTAGE, RuleType.BLACK_FRIDAY_DEAL)
                .withDiscount(discount)
                .withAvailDate(availDate)
                .build();
    }
}
