package idealo.api;

import idealo.domain.Cart;
import idealo.factory.PriceLookUpFactory;
import idealo.factory.PriceRule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

public class Checkout {

    private Cart cart;

    private Checkout() {}

    public Checkout(List<PriceRule> priceRuleList) {
        PriceLookUpFactory.getInstance().buildPriceRuleLookup(priceRuleList);
        cart = new Cart();
    }

    public void scan(String skuId) {
        this.cart.addSku(skuId);
    }

    public BigDecimal total() {
        return this.cart.calculateTotal().setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal total(ZonedDateTime purchaseDate) {
        return this.cart.calculateTotal(purchaseDate).setScale(2, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        LocalTime currentTime = LocalTime.now(Clock.systemDefaultZone());
        System.out.println("The current local time is: " + currentTime);
        System.out.println("The checkout module currently not exposed as consumable api");
        System.out.println("The checkout module can be tested with running TestPrice class");
    }
}
