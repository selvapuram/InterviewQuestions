package idealo;

import idealo.api.Checkout;
import idealo.domain.SkuType;
import idealo.factory.PriceRule;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static java.time.ZonedDateTime.now;
import static org.junit.Assert.assertEquals;

public class TestPrice {


    @Test
    public void shouldReturnExpectedTotalsGivenSkuTotals() {
        assertEquals(expected(0), calculatePrice(""));
        assertEquals(expected(40), calculatePrice("A"));
        assertEquals(expected(90), calculatePrice("AB"));
        assertEquals(expected(135), calculatePrice("CDBA"));
        assertEquals(expected(80), calculatePrice("AA"));
        assertEquals(expected(100), calculatePrice("AAA"));
        assertEquals(expected(140), calculatePrice("AAAA"));
        assertEquals(expected(180), calculatePrice("AAAAA"));
        assertEquals(expected(200), calculatePrice("AAAAAA"));
        assertEquals(expected(150), calculatePrice("AAAB"));
        assertEquals(expected(180), calculatePrice("AAABB"));
        assertEquals(expected(200), calculatePrice("AAABBD"));
        assertEquals(expected(200), calculatePrice("DABABA"));
    }


    @Test
    public void shouldReturnExpectedTotalGivenSkuScannedIncrementally() {
        Checkout checkout = new Checkout(buildRules());
        assertEquals(expected(0), checkout.total());
        checkout.scan("A");
        assertEquals(expected(40), checkout.total());
        checkout.scan("B");
        assertEquals(expected(90), checkout.total());
        checkout.scan("A");
        assertEquals(expected(130), checkout.total());
        checkout.scan("A");
        assertEquals(expected(150), checkout.total());
        checkout.scan("B");
        assertEquals(expected(180), checkout.total());
    }

    @Test
    public void shouldReturnExpectedTotalsGivenSkuTotalsWithBlackFridayDeal() {
        assertEquals(expected(0), calculatePrice(""));
        assertEquals(expected(40), calculatePrice("A"));
        assertEquals(expected(90), calculatePrice("AB"));
        assertEquals(expected(135), calculatePrice("CDBA"));
        assertEquals(expected(225), calculatePrice("CDBAE"));
        assertEquals(expected(80), calculatePrice("AA"));
        assertEquals(expected(100), calculatePrice("AAA"));
        assertEquals(expected(140), calculatePrice("AAAA"));
        assertEquals(expected(180), calculatePrice("AAAAA"));
        assertEquals(expected(200), calculatePrice("AAAAAA"));
        assertEquals(expected(290), calculatePrice("AAAAAAE"));
        assertEquals(expected(150), calculatePrice("AAAB"));
        assertEquals(expected(180), calculatePrice("AAABB"));
        assertEquals(expected(200), calculatePrice("AAABBD"));
        assertEquals(expected(200), calculatePrice("DABABA"));
    }


    @Test
    public void shouldReturnExpectedTotalGivenSkuScannedWithBFDealIncrementally() {
        ZonedDateTime purchaseDate = now().minusDays(5);
        Checkout checkout = new Checkout(buildRulesWithPurchaseDate(purchaseDate));
        assertEquals(expected(0), checkout.total(purchaseDate));
        checkout.scan("A");
        assertEquals(expected(40), checkout.total(purchaseDate));
        checkout.scan("B");
        assertEquals(expected(90), checkout.total(purchaseDate));
        checkout.scan("A");
        assertEquals(expected(130), checkout.total(purchaseDate));
        checkout.scan("A");
        assertEquals(expected(150), checkout.total(purchaseDate));
        checkout.scan("B");
        assertEquals(expected(180), checkout.total(purchaseDate));
        checkout.scan("E");
        assertEquals(expected(270), checkout.total(purchaseDate));
    }


    private List<PriceRule> buildRules() {
        return Arrays.asList(
                PriceRule.quantityRule(SkuType.A, expected(20), 3),
                PriceRule.quantityRule(SkuType.B, expected(20), 2),
                PriceRule.blackFridayRule(SkuType.E, expected(10), now(Clock.systemDefaultZone()))
        );
    }

    private List<PriceRule> buildRulesWithPurchaseDate(ZonedDateTime purchaseDate) {
        return Arrays.asList(
                PriceRule.quantityRule(SkuType.A, expected(20), 3),
                PriceRule.quantityRule(SkuType.B, expected(20), 2),
                PriceRule.blackFridayRule(SkuType.E, expected(10), purchaseDate)
        );
    }

    private BigDecimal calculatePrice(String goods) {

        Checkout checkout = new Checkout(buildRules());
        for (int i = 0; i < goods.length(); i++) {
            checkout.scan(String.valueOf(goods.charAt(i)));
        }
        return checkout.total();
    }

    private BigDecimal expected(int value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}