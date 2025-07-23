package atlassian;

import atlassian.contract.RunningCommodityPrice;
import atlassian.impl.RunningCommodityPriceImpl;
import org.junit.Assert;
import org.junit.Test;

public class TestRunningCommodityPrice {

    @Test
    public void testGetMaxCommodityPrice() {
        RunningCommodityPrice r = new RunningCommodityPriceImpl();
        r.upsertCommodityPrice(4, 27);
        r.upsertCommodityPrice(6, 26);
        r.upsertCommodityPrice(9, 25);

        Assert.assertEquals(27, r.getMaxCommodityPrice());
    }

    @Test
    public void testGetMaxCommodityPriceWhenDuplicatedTimeStamp() {
        RunningCommodityPrice r = new RunningCommodityPriceImpl();
        r.upsertCommodityPrice(4, 27);
        r.upsertCommodityPrice(6, 26);
        r.upsertCommodityPrice(9, 25);

        Assert.assertEquals(27, r.getMaxCommodityPrice());

        r.upsertCommodityPrice(4, 28);

        Assert.assertEquals(28, r.getMaxCommodityPrice());
        //fail();
    }

    @Test
    public void testGetMostRecentCommodityPrice() {
        RunningCommodityPrice r = new RunningCommodityPriceImpl();
        r.upsertCommodityPrice(4, 27);
        r.upsertCommodityPrice(6, 26);
        r.upsertCommodityPrice(9, 25);
        r.upsertCommodityPrice(4, 20);

        Assert.assertEquals(25, r.getMostRecentCommodityPrice());
    }

    @Test
    public void testShouldRecentCommodityPriceWhenDuplicated() {
        RunningCommodityPrice r = new RunningCommodityPriceImpl();
        r.upsertCommodityPrice(4, 27);
        r.upsertCommodityPrice(6, 26);
        r.upsertCommodityPrice(9, 25);
        r.upsertCommodityPrice(4, 20);
        r.upsertCommodityPrice(9, 17);
        Assert.assertEquals(17, r.getMostRecentCommodityPrice());
    }

    @Test
    public void testGetCurrentPrice() {
        RunningCommodityPrice r = new RunningCommodityPriceImpl();
        r.upsertCommodityPrice(4, 27);
        r.upsertCommodityPrice(6, 26);
        r.upsertCommodityPrice(9, 25);
        r.upsertCommodityPrice(4, 20);
        r.upsertCommodityPrice(9, 17);
        Assert.assertEquals(20, r.getCurrentPrice(4));
    }
}
