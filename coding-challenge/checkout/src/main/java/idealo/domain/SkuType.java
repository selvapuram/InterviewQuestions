package idealo.domain;

import idealo.model.Sku;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

public enum SkuType {

    A(BigDecimal.valueOf(40)),
    B(BigDecimal.valueOf(50)),
    C(BigDecimal.valueOf(25)),
    D(BigDecimal.valueOf(20)),
    E(BigDecimal.valueOf(100));

    final BigDecimal unitPrice;

    SkuType(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public static Sku build(String skuId) {
        try {
            SkuType skuType = SkuType.valueOf(skuId);
            return Sku.with(skuType, skuType.unitPrice);
        } catch (IllegalArgumentException exception) {
            throw new NoSuchElementException("SKU Information not found");
        }
    }

}
