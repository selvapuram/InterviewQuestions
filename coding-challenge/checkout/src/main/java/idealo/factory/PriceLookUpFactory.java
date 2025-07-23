package idealo.factory;

import idealo.domain.SkuType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PriceLookUpFactory {

    private static final Logger LOGGER = Logger.getGlobal();
    private Map<SkuType, PriceRule> priceRuleMap = new HashMap<>();

    private static final PriceLookUpFactory INSTANCE = new PriceLookUpFactory();

    public static PriceLookUpFactory getInstance() {
        return INSTANCE;
    }

    public void buildPriceRuleLookup(List<PriceRule> ruleList, boolean oldValueFirst) {
        BinaryOperator<PriceRule> function = (oldValueFirst ? (oldVal, newVal) -> oldVal : (oldVal, newVal) -> newVal);
        priceRuleMap = ruleList.stream().collect(
                Collectors.toMap(PriceRule::getSkuType, Function.identity(), function)
        );
    }

    public void buildPriceRuleLookup(List<PriceRule> ruleList) {
        buildPriceRuleLookup(ruleList, false);
    }

    public Map<SkuType, PriceRule> getPriceRuleMap() {
        if(priceRuleMap.isEmpty()) {
            LOGGER.warning("price rule is not set, default with unit price");
        }
        return priceRuleMap;
    }

}
