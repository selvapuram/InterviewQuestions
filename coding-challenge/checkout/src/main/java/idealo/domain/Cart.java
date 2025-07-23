package idealo.domain;

import idealo.factory.PriceLookUpFactory;
import idealo.factory.PriceRule;
import idealo.model.PricingDynamics;
import idealo.model.Sku;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {

    private final Map<SkuType, PriceRule> PRICE_LOOK_UP = PriceLookUpFactory.getInstance().getPriceRuleMap();
    private static final ZonedDateTime cartCreatedAt = ZonedDateTime.now(Clock.systemDefaultZone());
    private Map<SkuType, List<Sku>> skuMap = new ConcurrentHashMap<>();

    private BigDecimal totalPrice;

    public Cart() {
    }

    private void addSku(Sku sku) {
        this.skuMap.merge(sku.getSkuType(), new ArrayList<Sku>() {{
            add(sku);
        }}, (currentList, newList) -> {
            newList.addAll(currentList);
            return newList;
        });
    }

    public void addSku(String skuId) {
        this.addSku(SkuType.build(skuId));
    }

    public BigDecimal calculateTotal(ZonedDateTime purchaseDate) {
        totalPrice = BigDecimal.ZERO;
        this.skuMap.forEach((skuType, skus) -> {
            PriceRule rule = PRICE_LOOK_UP.computeIfAbsent(skuType, PriceRule::basicPriceRule);
            BigDecimal skuPrice = skus.get(0).getPrice();
            PricingDynamics pricingDynamics = new PricingDynamics(skus.size(), purchaseDate, skuPrice);
            totalPrice = totalPrice.add(rule.getRuleType().resolver(rule).resolve(pricingDynamics));
            /*if (RuleType.QUANTITY == rule.getRuleType()) {
                totalPrice = totalPrice.add(rule.getRuleType().resolver(rule).resolve(skus.size(), skuPrice));
            } else if (RuleType.BLACK_FRIDAY_DEAL == rule.getRuleType()) {
                totalPrice = totalPrice.add(rule.getRuleType().resolver(rule).resolve(purchaseDate, skuPrice));
            } else {
                totalPrice = totalPrice.add(rule.getRuleType().resolver(rule).resolve(skuPrice, rule.getDiscount()));
            }*/
        });
        return totalPrice;
    }

    public BigDecimal calculateTotal() {
        return calculateTotal(cartCreatedAt);
    }
}
